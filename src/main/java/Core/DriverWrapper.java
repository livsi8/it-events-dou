package Core;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverWrapper extends RemoteWebDriver implements WebDriver, TakesScreenshot {
    public static        WebDriver             driver;
    private              WebDriverWait         wrapperWait;
    private              FluentWait<WebDriver> wrapperFluent;
    private static final Logger                log =
        LogManager.getLogger(DriverWrapper.class.getName());

    public DriverWrapper(WebDriver driver) {
        DriverWrapper.driver = driver;
        wrapperWait          = new WebDriverWait(
            driver,
            5
        );
        wrapperFluent        = new WebDriverWait(
            driver,
            5
        ).pollingEvery(Duration.ofMillis(100));
    }

    public DriverWrapper(Capabilities capabilities) {
        super(capabilities);
    }

    @Override
    public void get(String s) {
        driver.get(s);
        log.info("Page load readyState: " +
            ((JavascriptExecutor) driver).executeScript("return document.readyState.toString();"));
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    @Override
    public Object executeScript(
        String script,
        Object... args
    ) {
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) driver).executeScript(
                script,
                args
            );
        } else {
            throw new WebDriverException(
                "Wrapped webdriver does not implement JavascriptExecutor: " + driver);
        }
    }

    @Override
    public Object executeAsyncScript(
        String script,
        Object... args
    ) {
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) driver).executeAsyncScript(
                script,
                args
            );
        } else {
            throw new WebDriverException(
                "Wrapped webdriver does not implement JavascriptExecutor: " + driver);
        }
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        Augmenter augmenter = new Augmenter();
        return ((RemoteWebDriver) augmenter.augment(driver)).getScreenshotAs(outputType);
    }
    //TODO: придумать как вытянуть методы ремоут вебдрайвера во враппер
    //       @Override
    //    public Capabilities getCapabilities()
    //    {
    //        Capabilities cap = ((RemoteWebDriver)driver).getCapabilities(); //преваедение к ремоут вебдрайверу
    ////        System.out.println(cap);
    //        return  cap;
    //    }
}
