package Dou.Interface;

import java.util.List;
import org.openqa.selenium.WebElement;

public interface IMainPage extends IPage {
    List<WebElement> getNewsTitle();

    WebElement getNewsHead();

    List<WebElement> getEventInfoRowDt();

    List<WebElement> getEventInfoRowDd();

    List<WebElement> getNewsLinksBody();

    WebElement getNewsBody();
}
