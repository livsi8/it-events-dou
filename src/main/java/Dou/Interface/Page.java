package Dou.Interface;

import Core.DriverFactory;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class Page {
    protected final Logger log = LogManager.getLogger(this.getClass());

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    protected WebElement getWebElement(List<WebElement> elementList) {
        WebElement result = null;
        for (WebElement webElement : elementList) {
            if (webElement.isDisplayed()) {
                result = webElement;
                break;
            }
        }
        return result;
    }

    protected WebDriver switchPage() {
        WebDriver driver = null;
        for (String winHandle : getDriver().getWindowHandles()) {
            driver = getDriver().switchTo().window(winHandle);
        }
        return driver;
    }

    protected WebDriver switchToFrame(WebElement element) {
        return getDriver().switchTo().frame(element);
    }
}
