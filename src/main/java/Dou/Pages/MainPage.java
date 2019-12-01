package Dou.Pages;

import Dou.Interface.IMainPage;
import Dou.Interface.Page;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class MainPage extends Page implements IMainPage {
    public MainPage() {
        PageFactory.initElements(
            getDriver(),
            this
        );
    }

    @FindBy(css = ".title a")
    List<WebElement> titleNews;
    @FindBy(css = ".page-head")
    WebElement       headNews;
//    @FindBy(css = "[id='btLogin']")
//    WebElement loginButton;
}
