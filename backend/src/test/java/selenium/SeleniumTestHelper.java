/*
 * Proyecto: GreenHouse Manager
 * Archivo: SeleniumTestHelper.java
 * Descripcion: Utilidades para autenticacion y datos de prueba en Selenium.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package selenium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Helper utilities for Selenium tests.
 */
public final class SeleniumTestHelper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    private static AuthSession cachedSession;

    private SeleniumTestHelper() {
        // Utility class
    }

    /**
     * Gets an authenticated session using the test auth endpoint.
     *
     * @param apiUrl backend base URL
     * @return auth session
     */
    public static AuthSession getAuthSession(String apiUrl) {
        if (cachedSession != null) {
            return cachedSession;
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("email", System.getenv().getOrDefault("TEST_USER_EMAIL", "qa@greenhouse.local"));
        payload.put("role", System.getenv().getOrDefault("TEST_USER_ROLE", "ADMIN"));
        JsonNode response = postJson(apiUrl + "/api/auth/test-token", null, payload);
        cachedSession = new AuthSession(
            response.get("token").asText(),
            response.get("userId").asLong(),
            response.get("email").asText(),
            response.get("role").asText()
        );
        return cachedSession;
    }

    /**
     * Writes auth data into local storage for UI authentication.
     *
     * @param driver web driver
     * @param baseUrl ui base url
     * @param session auth session
     */
    public static void authenticateUi(WebDriver driver, String baseUrl, AuthSession session) {
        driver.get(baseUrl + "/login");
        String userJson = toJson(Map.of(
            "id", session.userId,
            "email", session.email,
            "role", session.role
        ));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("localStorage.setItem('ghm_token', arguments[0]);", session.token);
        js.executeScript("localStorage.setItem('ghm_user', arguments[0]);", userJson);
        driver.navigate().refresh();
    }

    /**
     * Creates a greenhouse via API.
     *
     * @param apiUrl backend base URL
     * @param session auth session
     * @param nombre greenhouse name
     * @return greenhouse id
     */
    public static long createGreenhouse(String apiUrl, AuthSession session, String nombre) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("usuarioId", session.userId);
        payload.put("nombre", nombre);
        payload.put("ubicacion", "Lote Selenium");
        payload.put("descripcion", "Creado por selenium");
        payload.put("areaM2", 100);
        JsonNode response = postJson(apiUrl + "/api/invernaderos", session.token, payload);
        return response.get("id").asLong();
    }

    /**
     * Creates a plant via API.
     *
     * @param apiUrl backend base URL
     * @param session auth session
     * @param greenhouseId greenhouse id
     * @param nombre plant name
     * @return plant id
     */
    public static long createPlant(
        String apiUrl,
        AuthSession session,
        long greenhouseId,
        String nombre
    ) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("invernaderoId", greenhouseId);
        payload.put("nombreComun", nombre);
        payload.put("frecuenciaRiegoDias", 2);
        payload.put("frecuenciaFertilizacionDias", 7);
        payload.put("estadoActual", "OPTIMO");
        payload.put("activo", true);
        JsonNode response = postJson(apiUrl + "/api/plantas", session.token, payload);
        return response.get("id").asLong();
    }

    /**
     * Creates an alert via API.
     *
     * @param apiUrl backend base URL
     * @param session auth session
     * @param plantId plant id
     * @param greenhouseId greenhouse id
     * @param tipo alert type
     * @return alert id
     */
    public static long createAlert(
        String apiUrl,
        AuthSession session,
        long plantId,
        long greenhouseId,
        String tipo
    ) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("plantaId", plantId);
        payload.put("invernaderoId", greenhouseId);
        payload.put("tipo", tipo);
        payload.put("severidad", "MEDIA");
        payload.put("mensaje", "alerta.riego.pendiente");
        payload.put("activa", true);
        JsonNode response = postJson(apiUrl + "/api/alertas", session.token, payload);
        return response.get("id").asLong();
    }

    /**
     * Deletes a plant via API.
     *
     * @param apiUrl backend base URL
     * @param session auth session
     * @param plantId plant id
     */
    public static void deletePlant(String apiUrl, AuthSession session, long plantId) {
        delete(apiUrl + "/api/plantas/" + plantId, session.token);
    }

    /**
     * Deletes a greenhouse via API.
     *
     * @param apiUrl backend base URL
     * @param session auth session
     * @param greenhouseId greenhouse id
     */
    public static void deleteGreenhouse(String apiUrl, AuthSession session, long greenhouseId) {
        delete(apiUrl + "/api/invernaderos/" + greenhouseId, session.token);
    }

    /**
     * Deletes an alert via API.
     *
     * @param apiUrl backend base URL
     * @param session auth session
     * @param alertId alert id
     */
    public static void deleteAlert(String apiUrl, AuthSession session, long alertId) {
        delete(apiUrl + "/api/alertas/" + alertId, session.token);
    }

    private static JsonNode postJson(String url, String token, Object payload) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(toJson(payload)));
            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }
            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 300) {
                throw new IllegalStateException("Request failed: " + response.body());
            }
            return mapper.readTree(response.body());
        } catch (IOException | InterruptedException ex) {
            throw new IllegalStateException("Request failed", ex);
        }
    }

    private static void delete(String url, String token) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .DELETE();
            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }
            client.send(builder.build(), HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException ex) {
            throw new IllegalStateException("Delete failed", ex);
        }
    }

    private static String toJson(Object payload) {
        try {
            return mapper.writeValueAsString(payload);
        } catch (IOException ex) {
            throw new IllegalStateException("JSON serialization failed", ex);
        }
    }

    /**
     * Container for authentication values.
     */
    public static class AuthSession {
        public final String token;
        public final long userId;
        public final String email;
        public final String role;

        /**
         * Creates a new auth session instance.
         *
         * @param token jwt token
         * @param userId user id
         * @param email user email
         * @param role user role
         */
        public AuthSession(String token, long userId, String email, String role) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.role = role;
        }
    }
}
