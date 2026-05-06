/*
 * Proyecto: GreenHouse Manager
 * Archivo: DashboardPage.java
 * Descripcion: Page Object para el dashboard.
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
 * Page object for the dashboard screen.
 */
public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    /**
     * Creates a new DashboardPage instance.
     *
     * @param driver web driver
     * @param baseUrl ui base url
     */
    public DashboardPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Opens the dashboard page.
     */
    public void open() {
        driver.get(baseUrl + "/");
    }

    /**
     * Gets the current alerts count.
     *
     * @return alerts count
     */
    public int getAlertasCount() {
        return getMetricValue("Alertas activas");
    }

    /**
     * Gets the current plants count.
     *
     * @return plants count
     */
    public int getPlantasCount() {
        return getMetricValue("Total plantas");
    }

    private int getMetricValue(String label) {
        String xpath = "//div[contains(@class,'card')]"
            + "//div[contains(@class,'muted') and normalize-space(text())='" + label + "']"
            + "/following-sibling::div[contains(@class,'metric-value')]";
        WebElement value = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return Integer.parseInt(value.getText().trim());
    }
}
