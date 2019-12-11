package Core;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
    private static       Account                     currentUser;
    private static final Logger                      log     =
        LogManager.getLogger(DriverFactory.class.getName());
    private static       HashMap<Account, WebDriver> drivers = new HashMap<>();

    public static synchronized void setCurrentParam(Account user) {
        currentUser = user;
    }

    public static synchronized WebDriver getDriver() {
        if (!drivers.containsKey(currentUser)) {
            drivers.put(
                currentUser,
                WebDriverConfig.initialize()
            );
//            log.debug(currentUser.toString());
        }
        return drivers.get(currentUser);
    }

    public static synchronized WebDriver getDriver(String mode) {
        if (!drivers.containsKey(currentUser)) {
            drivers.put(
                currentUser,
                WebDriverConfig.initialize(mode)
            );
            log.info(currentUser.toString());
        }
        return drivers.get(currentUser);
    }

    public static void destroyDriver() throws InterruptedException {
        WebDriver driver = getDriver();
        if (drivers.containsKey(currentUser)) {
            //TODO ФИКСИТЬ , ЗАКРЫТ ДАННЫЙ  СКРИПТ НА БИНАРКЕ
            //            if(Core.Config.getProperty("platformName").equals("Binary")){
            //            JavascriptExecutor executor = (JavascriptExecutor) driver;
            //            executor.executeScript(
            //                    "window.localStorage.clear();\n" +
            //                            "var a = new Connector();\n" +
            //                            "var keyUserName = a.facadeAPI.getLocalStorageManager().getStateLocalStorage('userName');\n" +
            //                            "a.facadeAPI.getLocalStorageManager().removeUserItem(keyUserName);\n" +
            //                            "a.facadeAPI.setUserLocalSettings();");
            log.info("Clear localStorage & Refresh page");
            //            }
            // NEW примочка для завершения драйвера
            try {
                if (driver.getWindowHandles().size() > 1) {
                    driver.close();
                } else {
                    driver.quit();
                }
            } catch (Exception e) {
                System.out.println("can`t kill driver , error " + e);
            }
            log.info("Driver Destroyed");
            drivers.remove(currentUser);
            log.info("Current User Removed");
        }
        try {
            driver.close();
            driver.quit();
        } catch (Exception e) {
            System.out.println("can`t kill driver , error " + e);
        }
    }

    public static String getBrowserInfo() {
        log.debug("Getting browser info");
        // we have to cast WebDriver object to RemoteWebDriver here, because the first one does not have a method
        // that would tell you which browser it is driving. (sick!)
        Capabilities cap =
            ((RemoteWebDriver) getDriver()).getCapabilities(); //преваедение к ремоут вебдрайверу
        log.info("Capabilities" + cap);
        return String.format(
            "%s v:%s %s",
            cap.getBrowserName(),
            cap.getVersion(),
            cap.getPlatform()
        );
    }

    public static Account getCurrentUser() {
        return currentUser;
    }
}
