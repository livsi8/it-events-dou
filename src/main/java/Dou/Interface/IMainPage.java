package Dou.Interface;

import java.util.List;
import org.openqa.selenium.WebElement;

public interface IMainPage extends IPage {
    List<WebElement> getTitleNews();

    WebElement getHeadNews();
}
