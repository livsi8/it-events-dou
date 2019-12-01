package Dou.Pages;

import Core.Config;
import Dou.Interface.ILoginPage;
import Dou.Interface.Page;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends Page implements ILoginPage {
    FluentWait<WebDriver> waitLogin;

    public LoginPage() {
        PageFactory.initElements(
            getDriver(),
            this
        );
        this.waitLogin = new WebDriverWait(
            getDriver(),
            40,
            0
        ).pollingEvery(Duration.ofMillis(100));
    }

    @FindBy(id = "login")
    WebElement loginField;
    @FindBy(id = "password")
    WebElement passwordField;
    @FindBy(css = "[id='btLogin']")
    WebElement loginButton;

    @Override
    public ILoginPage open() {
        getDriver().get(Config.getProperty("url"));
        return new LoginPage();
    }

    @Override
    public ILoginPage openPlatform(String url) {
        getDriver().manage().timeouts().pageLoadTimeout(
            300,
            TimeUnit.SECONDS
        );
        getDriver().get(url);
        return new LoginPage();
    }
}
