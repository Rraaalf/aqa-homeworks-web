package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.DebitCardForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DebitCardFormTests {
    public final String VALID_NAME = "Иван Петров-Сидоров";
    public final String VALID_PHONE = "+12345678901";
    private final DebitCardForm debitCardForm = new DebitCardForm();
    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    private void clickElement(By element) {
        driver.findElement(element).click();
    }

    private void fillField(By field, String string) {
        driver.findElement(field).sendKeys(string);
    }

    public String getMessageText(By message) {
        return driver.findElement(message).getText().trim();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void positiveTest() {
        fillField(debitCardForm.NAME_FIELD, VALID_NAME);
        fillField(debitCardForm.PHONE_FIELD, VALID_PHONE);
        clickElement(debitCardForm.CHECKBOX);
        clickElement(debitCardForm.BUTTON);
        String successMessage = getMessageText(By.xpath("//p[@data-test-id='order-success']"));
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, successMessage);
    }

    @Test
    public void invalidNameTest() {
        fillField(debitCardForm.NAME_FIELD, "Ivan");
        fillField(debitCardForm.PHONE_FIELD, VALID_PHONE);
        clickElement(debitCardForm.CHECKBOX);
        clickElement(debitCardForm.BUTTON);
        String nameFieldMessage = getMessageText(debitCardForm.NAME_FIELD_MESSAGE);
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, nameFieldMessage);
    }

    @Test
    public void emptyNameTest() {
        fillField(debitCardForm.PHONE_FIELD, VALID_PHONE);
        clickElement(debitCardForm.CHECKBOX);
        clickElement(debitCardForm.BUTTON);
        String nameFieldMessage = getMessageText(debitCardForm.NAME_FIELD_MESSAGE);
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, nameFieldMessage);
    }

    @Test
    public void invalidPhoneTest() {
        fillField(debitCardForm.NAME_FIELD, VALID_NAME);
        fillField(debitCardForm.PHONE_FIELD, "+7657");
        clickElement(debitCardForm.CHECKBOX);
        clickElement(debitCardForm.BUTTON);
        String phoneFieldMessage = getMessageText(debitCardForm.PHONE_FIELD_MESSAGE);
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, phoneFieldMessage);
    }

    @Test
    public void emptyPhoneTest() {
        fillField(debitCardForm.NAME_FIELD, VALID_NAME);
        clickElement(debitCardForm.CHECKBOX);
        clickElement(debitCardForm.BUTTON);
        String phoneFieldMessage = getMessageText(debitCardForm.PHONE_FIELD_MESSAGE);
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, phoneFieldMessage);
    }

    @Test
    public void checkboxTest() {
        fillField(debitCardForm.NAME_FIELD, VALID_NAME);
        fillField(debitCardForm.PHONE_FIELD, VALID_PHONE);
        clickElement(debitCardForm.BUTTON);
        assertTrue(driver.findElement(By.xpath("//label[@data-test-id='agreement'][contains(@class, 'input_invalid')]"))
                .isDisplayed());
    }
}