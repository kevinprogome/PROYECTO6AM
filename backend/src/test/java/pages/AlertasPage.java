/*
 * Proyecto: GreenHouse Manager
 * Archivo: AlertasPage.java
 * Descripcion: Page Object para la pantalla de alertas.
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page object for the alerts screen.
 */
public class AlertasPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    /**
     * Creates a new AlertasPage instance.
     *
     * @param driver web driver
     * @param baseUrl ui base url
     */
    public AlertasPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    /**
     * Opens the alerts page.
     */
    public void open() {
        driver.get(baseUrl + "/alertas");
    }

    /**
     * Resolves an alert by id when present.
     *
     * @param id alert id
     */
    public void resolverAlerta(String id) {
        if (!driver.findElements(By.xpath("//tr[td[normalize-space(text())='" + id + "']]")).isEmpty()) {
            WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[td[normalize-space(text())='" + id + "']]")));
            WebElement resolveButton = row.findElement(By.xpath(".//button[contains(text(),'Resolver')]"));
            resolveButton.click();
            return;
        }
        WebElement resolveButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Resolver')]")
        ));
        resolveButton.click();
    }

    /**
     * Filters alerts by greenhouse name.
     *
     * @param nombre greenhouse name
     */
    public void filtrarPorInvernadero(String nombre) {
        Select greenhouseSelect = new Select(wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("select.select"))));
        greenhouseSelect.selectByVisibleText(nombre);
    }
}
