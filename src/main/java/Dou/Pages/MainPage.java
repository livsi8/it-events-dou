package Dou.Pages;

import Dou.Interface.IMainPage;
import Dou.Interface.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends Page implements IMainPage {
    public MainPage() {
        PageFactory.initElements(
            getDriver(),
            this
        );
    }

    @FindBy(id = "login")
    WebElement loginField;
    @FindBy(id = "password")
    WebElement passwordField;
    @FindBy(css = "[id='btLogin']")
    WebElement loginButton;
}
