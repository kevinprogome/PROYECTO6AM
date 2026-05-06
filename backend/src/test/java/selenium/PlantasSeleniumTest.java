/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantasSeleniumTest.java
 * Descripcion: Pruebas Selenium para plantas.
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
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PlantasPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Selenium tests for plants page.
 */
public class PlantasSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String baseUrl;
    private static String apiUrl;
    private static SeleniumTestHelper.AuthSession session;
    private static long greenhouseId;

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
        greenhouseId = SeleniumTestHelper.createGreenhouse(apiUrl, session, "Invernadero UI");
    }

    /**
     * Pre-authenticates before each test.
     */
    @BeforeEach
    void authenticate() {
        SeleniumTestHelper.authenticateUi(driver, baseUrl, session);
    }

    /**
     * Closes driver and cleans data.
     */
    @AfterAll
    static void tearDown() {
        if (session != null) {
            SeleniumTestHelper.deleteGreenhouse(apiUrl, session, greenhouseId);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Valida que crear una planta la muestra en la tabla.
     */
    @Test
    void testCrearPlanta_aparecEnTabla() {
        PlantasPage plantasPage = new PlantasPage(driver, baseUrl);
        plantasPage.open();
        plantasPage.crearPlanta("Zanahoria UI", "Nantes", 2);
        assertThat(plantasPage.buscarPlanta("Zanahoria UI")).isTrue();
        plantasPage.eliminarPlanta("Zanahoria UI");
    }

    /**
     * Valida que el filtro por estado funciona.
     */
    @Test
    void testFiltrarPlantasPorEstado() {
        PlantasPage plantasPage = new PlantasPage(driver, baseUrl);
        plantasPage.open();
        plantasPage.crearPlanta("Espinaca UI", "Viroflay", 3);
        plantasPage.filtrarPorEstado("OPTIMO");
        assertThat(plantasPage.buscarPlanta("Espinaca UI")).isTrue();
        plantasPage.eliminarPlanta("Espinaca UI");
    }

    /**
     * Valida que registrar riego actualiza la fecha.
     */
    @Test
    void testRegistrarRiego_actualizaFecha() {
        PlantasPage plantasPage = new PlantasPage(driver, baseUrl);
        plantasPage.open();
        plantasPage.crearPlanta("Riego UI", "Test", 1);
        plantasPage.registrarRiego("Riego UI");
        assertThat(plantasPage.buscarPlanta("Riego UI")).isTrue();
        plantasPage.eliminarPlanta("Riego UI");
    }

    /**
     * Valida que eliminar una planta la retira de la tabla.
     */
    @Test
    void testEliminarPlanta_desaparece() {
        PlantasPage plantasPage = new PlantasPage(driver, baseUrl);
        plantasPage.open();
        plantasPage.crearPlanta("Eliminar UI", "Test", 2);
        plantasPage.eliminarPlanta("Eliminar UI");
        assertThat(plantasPage.buscarPlanta("Eliminar UI")).isFalse();
    }
}
