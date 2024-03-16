package pages;

import org.openqa.selenium.By;

public class DebitCardForm {
    public final By NAME_FIELD = By.xpath("//span[@data-test-id='name']//input");
    public final By NAME_FIELD_MESSAGE = By.xpath("//span[@data-test-id='name']//span[@class='input__sub']");
    public final By PHONE_FIELD = By.xpath("//span[@data-test-id='phone']//input");
    public final By PHONE_FIELD_MESSAGE = By.xpath("//span[@data-test-id='phone']//span[@class='input__sub']");
    public final By CHECKBOX = By.xpath("//label[@data-test-id='agreement']");
    public final By BUTTON = By.xpath("//button");

}
