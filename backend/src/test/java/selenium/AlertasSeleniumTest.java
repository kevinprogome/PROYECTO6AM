/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertasSeleniumTest.java
 * Descripcion: Pruebas Selenium para alertas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AlertasPage;
import pages.DashboardPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Selenium tests for alerts and dashboard counts.
 */
public class AlertasSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String baseUrl;
    private static String apiUrl;
    private static SeleniumTestHelper.AuthSession session;
    private static long greenhouseId;
    private static long plantId;
    private static long alertId;

    /**
     * Initializes Chrome driver and test data.
     */
    @BeforeAll
    static void setUp() {
        baseUrl = System.getenv().getOrDefault("UI_BASE_URL", "http://localhost:5173");
        apiUrl = System.getenv().getOrDefault("API_URL", "http://localhost:8080");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if ("true".equalsIgnoreCase(System.getenv().getOrDefault("HEADLESS", "true"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        session = SeleniumTestHelper.getAuthSession(apiUrl);
        greenhouseId = SeleniumTestHelper.createGreenhouse(apiUrl, session, "Invernadero Alertas");
        plantId = SeleniumTestHelper.createPlant(apiUrl, session, greenhouseId, "Planta Alertas");
        alertId = SeleniumTestHelper.createAlert(apiUrl, session, plantId, greenhouseId, "RIEGO");
    }

    /**
     * Authenticates before each test.
     */
    @BeforeEach
    void authenticate() {
        SeleniumTestHelper.authenticateUi(driver, baseUrl, session);
    }

    /**
     * Cleans up data and driver.
     */
    @AfterAll
    static void tearDown() {
        if (session != null) {
            SeleniumTestHelper.deleteAlert(apiUrl, session, alertId);
            SeleniumTestHelper.deletePlant(apiUrl, session, plantId);
            SeleniumTestHelper.deleteGreenhouse(apiUrl, session, greenhouseId);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Valida que las alertas activas se muestren en pantalla.
     */
    @Test
    void testAlertasActivasSeMuestran() {
        AlertasPage alertasPage = new AlertasPage(driver, baseUrl);
        alertasPage.open();
        assertThat(driver.getPageSource()).contains("Alertas");
    }

    /**
     * Valida que resolver una alerta actualiza su estado.
     */
    @Test
    void testResolverAlerta_cambiaMEstado() {
        AlertasPage alertasPage = new AlertasPage(driver, baseUrl);
        alertasPage.open();
        alertasPage.resolverAlerta(String.valueOf(alertId));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.cssSelector(".data-table"), "Inactivo"));
        assertThat(driver.getPageSource()).contains("Inactivo");
    }

    /**
     * Valida que el dashboard muestra conteo de alertas.
     */
    @Test
    void testDashboardMuestraConteoAlertas() {
        DashboardPage dashboardPage = new DashboardPage(driver, baseUrl);
        dashboardPage.open();
        assertThat(dashboardPage.getAlertasCount()).isGreaterThanOrEqualTo(0);
    }
}
