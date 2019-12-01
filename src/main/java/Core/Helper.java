package Core;

import static Core.DriverFactory.getDriver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;

public class Helper {
    public static void goToNewTabAndOpenUrl (String urlWithNews) {
        ((JavascriptExecutor) getDriver()).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<> (getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1)); //switches to new tab
        getDriver().get(urlWithNews);

        getDriver().manage().timeouts().pageLoadTimeout(
            300,
            TimeUnit.SECONDS
        );
    }

    public static void closeCurrentTabAndBackToBeforeTab () {
//        ArrayList<String> tabs = new ArrayList<> (getDriver().getWindowHandles());
        getDriver().close();
        getDriver().switchTo().defaultContent();
//        getDriver().switchTo().window(tabs.get(0)); // switch back to main screen
    }

}
