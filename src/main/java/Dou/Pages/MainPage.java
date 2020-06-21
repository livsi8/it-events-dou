package Dou.Pages;

import Dou.Interface.IMainPage;
import Dou.Interface.Page;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class MainPage extends Page implements IMainPage {
    public MainPage() {
        PageFactory.initElements(
            getDriver(),
            this
        );
    }

    @FindBy(css = ".b-postcard .title a")
    List<WebElement> newsTitle;
    @FindBy(css = ".page-head")
    WebElement       newsHead;
    @FindBy(css = "article.b-typo")
    WebElement loginButton;
    @FindBy(css = ".event-info-row .dt")
    List<WebElement> eventInfoRowDt;
    @FindBy(css = ".event-info-row .dd")
    List<WebElement> eventInfoRowDd;
    @FindBy(css = ".b-typo")
    WebElement newsBody;
    @FindBy(css = ".event-info + .b-typo a")
    List<WebElement> newsLinksBody;
    @FindBy(css = ".b-paging a")
    List<WebElement> nextList;
}
