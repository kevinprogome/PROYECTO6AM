/*
 * Proyecto: GreenHouse Manager
 * Archivo: LoginSeleniumTest.java
 * Descripcion: Pruebas Selenium para login.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Selenium tests for login page.
 */
public class LoginSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String baseUrl;

    /**
     * Initializes the Chrome driver.
     */
    @BeforeAll
    static void setUp() {
        baseUrl = System.getenv().getOrDefault("UI_BASE_URL", "http://localhost:5173");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if ("true".equalsIgnoreCase(System.getenv().getOrDefault("HEADLESS", "true"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Closes the driver.
     */
    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Valida que la pagina de login carga correctamente.
     */
    @Test
    void testPaginaLoginCarga_correctamente() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        loginPage.open();
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    /**
     * Valida que el boton de Google es visible.
     */
    @Test
    void testBotonGoogleEsVisible() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        loginPage.open();
        assertThat(loginPage.isLoggedIn()).isFalse();
        assertThat(loginPage.isGoogleButtonVisible()).isTrue();
    }

    /**
     * Valida que sin autenticacion se redirige al login.
     */
    @Test
    void testRedireccionSinLogin_aLoginPage() {
        driver.get(baseUrl + "/");
        wait.until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
    }
}
