import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DebitCardFormTests {
    private final By NAME_FIELD = By.xpath("//span[@data-test-id='name']//input");
    private final By NAME_FIELD_MESSAGE = By.xpath("//span[@data-test-id='name']//span[@class='input__sub']");
    private final By PHONE_FIELD = By.xpath("//span[@data-test-id='phone']//input");
    private final By PHONE_FIELD_MESSAGE = By.xpath("//span[@data-test-id='phone']//span[@class='input__sub']");
    private final By CHECKBOX = By.xpath("//label[@data-test-id='agreement']");
    private final By BUTTON = By.xpath("//button");
    private final String VALID_NAME = "Иван Петров-Сидоров";
    private final String VALID_PHONE = "+12345678901";
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
        driver.get("http://localhost:0000");
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void positiveTest() {
        fillField(NAME_FIELD, VALID_NAME);
        fillField(PHONE_FIELD, VALID_PHONE);
        clickElement(CHECKBOX);
        clickElement(BUTTON);
        String successMessage = getMessageText(By.xpath("//p[@data-test-id='order-success']"));
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, successMessage);
    }

    @Test
    public void invalidNameTest() {
        fillField(NAME_FIELD, "Ivan");
        fillField(PHONE_FIELD, VALID_PHONE);
        clickElement(CHECKBOX);
        clickElement(BUTTON);
        String nameFieldMessage = getMessageText(NAME_FIELD_MESSAGE);
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, nameFieldMessage);
    }

    @Test
    public void emptyNameTest() {
        fillField(PHONE_FIELD, VALID_PHONE);
        clickElement(CHECKBOX);
        clickElement(BUTTON);
        String nameFieldMessage = getMessageText(NAME_FIELD_MESSAGE);
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, nameFieldMessage);
    }

    @Test
    public void invalidPhoneTest() {
        fillField(NAME_FIELD, VALID_NAME);
        fillField(PHONE_FIELD, "+7657");
        clickElement(CHECKBOX);
        clickElement(BUTTON);
        String phoneFieldMessage = getMessageText(PHONE_FIELD_MESSAGE);
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, phoneFieldMessage);
    }

    @Test
    public void emptyPhoneTest() {
        fillField(NAME_FIELD, VALID_NAME);
        clickElement(CHECKBOX);
        clickElement(BUTTON);
        String phoneFieldMessage = getMessageText(PHONE_FIELD_MESSAGE);
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, phoneFieldMessage);
    }

    @Test
    public void checkboxTest() {
        fillField(NAME_FIELD, VALID_NAME);
        fillField(PHONE_FIELD, VALID_PHONE);
        clickElement(BUTTON);
        assertTrue(driver.findElement(By.xpath("//label[@data-test-id='agreement'][contains(@class, 'input_invalid')]"))
                        .isDisplayed());
    }
}