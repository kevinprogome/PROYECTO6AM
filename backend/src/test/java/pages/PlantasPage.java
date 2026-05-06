/*
 * Proyecto: GreenHouse Manager
 * Archivo: PlantasPage.java
 * Descripcion: Page Object para la pantalla de plantas.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package pages;

import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page object for the plants screen.
 */
public class PlantasPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    /**
     * Creates a new PlantasPage instance.
     *
     * @param driver web driver
     * @param baseUrl ui base url
     */
    public PlantasPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    /**
     * Opens the plants page.
     */
    public void open() {
        driver.get(baseUrl + "/plantas");
    }

    /**
     * Creates a new plant using the modal form.
     *
     * @param nombre common name
     * @param tipo variety
     * @param frecRiego irrigation frequency
     */
    public void crearPlanta(String nombre, String tipo, int frecRiego) {
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".page-header .btn.btn-primary")));
        createButton.click();

        Select greenhouseSelect = new Select(wait.until(ExpectedConditions.elementToBeClickable(
            By.name("invernaderoId"))));
        greenhouseSelect.selectByIndex(1);

        WebElement nombreInput = driver.findElement(By.name("nombreComun"));
        nombreInput.clear();
        nombreInput.sendKeys(nombre);

        WebElement variedadInput = driver.findElement(By.name("variedad"));
        variedadInput.clear();
        variedadInput.sendKeys(tipo);

        WebElement riegoInput = driver.findElement(By.name("frecuenciaRiegoDias"));
        riegoInput.clear();
        riegoInput.sendKeys(String.valueOf(frecRiego));

        WebElement fertInput = driver.findElement(By.name("frecuenciaFertilizacionDias"));
        fertInput.clear();
        fertInput.sendKeys(String.valueOf(Math.max(1, frecRiego + 2)));

        WebElement saveButton = driver.findElement(By.cssSelector(".modal .btn.btn-primary"));
        saveButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal")));
    }

    /**
     * Searches for a plant in the table.
     *
     * @param nombre plant name
     * @return true if present
     */
    public boolean buscarPlanta(String nombre) {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input[placeholder='Buscar por nombre o variedad']")));
        searchInput.clear();
        searchInput.sendKeys(nombre);
        return !driver.findElements(By.xpath("//td[normalize-space(text())='" + nombre + "']")).isEmpty();
    }

    /**
     * Deletes a plant by name.
     *
     * @param nombre plant name
     */
    public void eliminarPlanta(String nombre) {
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//tr[td[normalize-space(text())='" + nombre + "']]")));
        WebElement deleteButton = row.findElement(By.cssSelector("button.btn-danger"));
        deleteButton.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//tr[td[normalize-space(text())='" + nombre + "']]")));
    }

    /**
     * Registers irrigation for a plant.
     *
     * @param nombre plant name
     */
    public void registrarRiego(String nombre) {
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//tr[td[normalize-space(text())='" + nombre + "']]")));
        WebElement riegoButton = row.findElement(By.xpath(".//button[contains(text(),'Registrar riego')]"));
        riegoButton.click();

        WebElement fechaInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.name("fecha")));
        fechaInput.clear();
        fechaInput.sendKeys("2026-05-06T08:00");

        WebElement saveButton = driver.findElement(By.cssSelector(".modal .btn.btn-primary"));
        saveButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal")));
    }

    /**
     * Filters plants by crop status.
     *
     * @param estado status value
     */
    public void filtrarPorEstado(String estado) {
        Select statusSelect = new Select(wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("select.select"))));
        statusSelect.selectByValue(estado);
    }
}
