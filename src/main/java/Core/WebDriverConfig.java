package Core;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class WebDriverConfig {
    public static        ArrayList<WebDriver> activeDrivers     = new ArrayList<>();
    private static final String               HUB_SELENOID      =
        ConfigBrowsers.getProperty("selenoid.hub.url");
    private static final String               SCREEN_RESOLUTION =
        ConfigBrowsers.getProperty("screen.resolution");
    private static final String               WDM_VERSION       =
        ConfigBrowsers.getProperty("wdm.version");
    private static final Logger               log               =
        LogManager.getLogger(WebDriverConfig.class.getName());
    public static        Capabilities         ChromeCapabilities;
    private static       String               browser;

    static synchronized WebDriver initialize() {
        // String browser = System.getProperty("browser");
        String    browser = "chrome";
        WebDriver driver  = null;
        switch (browser) {
            case "chrome":
                driver = initChrome();
                break;
            case "firefox":
                driver = initFireFox();
                break;
            case "edge":
                driver = initEdge();
                break;
            case "opera":
                driver = initOpera();
                break;
            case "safari":
                driver = initSafari();
                break;
        }
        activeDrivers.add(driver);
        return driver;
    }

    static synchronized WebDriver initChrome() {
        //        WebDriverManager.chromedriver().version("74").setup();
        if (!WDM_VERSION.isEmpty()) {
            WebDriverManager.chromedriver().version(WDM_VERSION).setup();
        } else {
            WebDriverManager.chromedriver().setup();
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
//        options.addArguments("start-maximized");
        WebDriver driver = new ChromeDriver(options);
        //Лог для версии драйвера и браузера
        Capabilities        cap = ((RemoteWebDriver) driver).getCapabilities();
        Map<String, String> a   = (Map<String, String>) options.merge(cap).getCapability("chrome");
        log.debug(String.format(
            "Driver version: %s",
            a.get("chromedriverVersion")
        ));
        log.debug("Chrome version: " + options.getVersion());
        browser = System.getProperty("browser");
        log.debug("" + browser);
        ChromeCapabilities = cap;
        driver.manage().timeouts().setScriptTimeout(
            20,
            TimeUnit.SECONDS
        );
        //TODO: оменял вейт на 300 сек - причина: трейдинг подключил Google Tag Manager для сбора статистики
        driver.manage().timeouts().pageLoadTimeout(
            45,
            TimeUnit.SECONDS
        );
        //Добавил имплисит
        driver.manage().timeouts().implicitlyWait(
            4,
            TimeUnit.SECONDS
        );
        return new DriverWrapper(driver);
    }

    static synchronized WebDriver initialize(String mode) {
        if (WDM_VERSION.isEmpty()) {
            WebDriverManager.chromedriver().setup();
        } else {
            WebDriverManager.chromedriver().version(WDM_VERSION).setup();
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-gpu");  // selenium BUG need added to work
        if (mode.equals("incognito")) {
            options.addArguments("--incognito");
        }
        if (mode.equals("filedownload")) {
            String downloadFilePath = System.getProperty("user.dir");
            System.out.println("DOWNLOAD FILEPATH" + downloadFilePath);
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put(
                "profile.default_content_settings.popups",
                0
            );
            chromePrefs.put(
                "download.default_directory",
                downloadFilePath
            );
            chromePrefs.put(
                "safebrowsing.enabled",
                "true"
            );
            options.setExperimentalOption(
                "prefs",
                chromePrefs
            );
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            cap.setCapability(
                CapabilityType.ACCEPT_SSL_CERTS,
                true
            );
            cap.setCapability(
                ChromeOptions.CAPABILITY,
                options
            );
        }
        WebDriver driver = new ChromeDriver(options);
        //Лог для версии драйвера и браузера
        Capabilities        cap = ((RemoteWebDriver) driver).getCapabilities();
        Map<String, String> a   = (Map<String, String>) options.merge(cap).getCapability("chrome");
        log.info(String.format(
            "Driver version: %s",
            a.get("chromedriverVersion")
        ));
        log.info("Chrome version: " + options.getVersion());
        ChromeCapabilities = cap;
        return new DriverWrapper(driver);
    }

    static synchronized WebDriver inizializateRemoteMobileChrome() {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put(
            "deviceName",
            "Nexus 5"
        );
        DesiredCapabilities browser = new DesiredCapabilities();
        browser.setBrowserName("chrome");
        browser.setVersion("74");
        browser.setCapability(
            "enableVNC",
            true
        );
        browser.setCapability(
            "screenResolution",
            SCREEN_RESOLUTION
        );
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("disable-infobars");
        options.setExperimentalOption(
            "mobileEmulation",
            mobileEmulation
        );
        browser.setCapability(
            ChromeOptions.CAPABILITY,
            options
        );
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                URI.create(HUB_SELENOID).toURL(),
                browser
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return driver;
    }

    static synchronized WebDriver inizializateRemoteChrome() {
        DesiredCapabilities browser = new DesiredCapabilities();
        browser.setBrowserName("chrome");
        browser.setVersion("67.0");
        browser.setCapability(
            "enableVNC",
            true
        );
        browser.setCapability(
            "screenResolution",
            SCREEN_RESOLUTION
        );
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("disable-infobars");
        browser.setCapability(
            ChromeOptions.CAPABILITY,
            options
        );
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                URI.create(HUB_SELENOID).toURL(),
                browser
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return driver;
    }

    static WebDriver initRemoteFireFox() throws Exception {
        DesiredCapabilities browser = new DesiredCapabilities();
        browser.setBrowserName("firefox");
        browser.setVersion("60.0");
        browser.setCapability(
            "screenResolution",
            SCREEN_RESOLUTION
        );
        browser.setCapability(
            "enableVNC",
            true
        );
        browser.setCapability(
            "acceptInsecureCerts",
            true
        );
        ProfilesIni    profile   = new ProfilesIni();
        FirefoxProfile myprofile = profile.getProfile("created Profile Name");
        browser.setCapability(
            FirefoxOptions.FIREFOX_OPTIONS,
            myprofile
        );
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                URI.create(HUB_SELENOID).toURL(),
                browser
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return driver;
    }

    static WebDriver initAndroidDriver() {
        DesiredCapabilities device = new DesiredCapabilities();
        device.setBrowserName("android");
        device.setVersion("5.1");
        //        device.setCapability("appPackage", "com.google.android.apps.chrome");
        //        device.setCapability("appActivity", "com.google.android.apps.chrome.Main");
        device.setCapability(
            "enableVNC",
            true
        );
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                URI.create(HUB_SELENOID).toURL(),
                device
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    static WebDriver initEdge() {
        WebDriverManager.edgedriver().setup();
        //   version("6.17134").setup();
        //        System.setProperty("webdriver.edge.driver","C:\\Users\\Andrew\\Downloads\\MicrosoftWebDriver.exe");
        WebDriver driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(
            6,
            TimeUnit.SECONDS
        );
        driver.manage().timeouts().setScriptTimeout(
            40,
            TimeUnit.SECONDS
        );
        driver.manage().timeouts().pageLoadTimeout(
            130,
            TimeUnit.SECONDS
        );
        return driver;
    }

    static WebDriver initOpera() {
        WebDriverManager.operadriver().setup();
        WebDriver driver = new OperaDriver();
        driver.manage().timeouts().setScriptTimeout(
            20,
            TimeUnit.SECONDS
        );
        driver.manage().timeouts().pageLoadTimeout(
            45,
            TimeUnit.SECONDS
        );
        return driver;
    }

    static WebDriver initFireFox() {
        WebDriverManager.firefoxdriver().setup();
        //arch64()
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().setScriptTimeout(
            20,
            TimeUnit.SECONDS
        );
        driver.manage().timeouts().pageLoadTimeout(
            45,
            TimeUnit.SECONDS
        );
        return driver;
    }

    static WebDriver initSafari() {
        WebDriver driver = new SafariDriver();
        driver.manage().timeouts().setScriptTimeout(
            20,
            TimeUnit.SECONDS
        );
        driver.manage().timeouts().pageLoadTimeout(
            45,
            TimeUnit.SECONDS
        );
        //        WebDriverWait wrapperWait = new WebDriverWait(driver, 5);
        //        FluentWait<WebDriver> wrapperFluent = new WebDriverWait(driver, 5).pollingEvery(Duration.ofMillis(100));
        return driver;
    }
}
