/*
 * Proyecto: GreenHouse Manager
 * Archivo: LoginPage.java
 * Descripcion: Page Object para la pantalla de login.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page object for the login screen.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    /**
     * Creates a new LoginPage instance.
     *
     * @param driver web driver
     * @param baseUrl ui base url
     */
    public LoginPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Opens the login page.
     */
    public void open() {
        driver.get(baseUrl + "/login");
    }

    /**
     * Clicks the Google login button.
     */
    public void clickLoginGoogle() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.btn-primary")));
        button.click();
    }

    /**
     * Checks if the Google login button is visible.
     *
     * @return true if visible
     */
    public boolean isGoogleButtonVisible() {
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("button.btn-primary")));
        return button.isDisplayed();
    }

    /**
     * Determines whether the user appears authenticated.
     *
     * @return true when nav items are visible
     */
    public boolean isLoggedIn() {
        return !driver.findElements(By.cssSelector(".nav-links")).isEmpty();
    }
}
