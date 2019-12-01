package Core;

import Dou.Interface.IPage;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * created for Docker integtation
 */
public class ConfigBrowsers {
    private static final Logger     log = LogManager.getLogger(ConfigBrowsers.class.getName());
    private static       Properties PROPERTIES;

    public static String getProperty(String key) {
        init();
        return PROPERTIES.getProperty(key);
    }

    private static void init() {
        if (PROPERTIES == null) {
            PROPERTIES = new Properties();
            URL
                configProperties = ClassLoader.getSystemResource("browsers.properties");
            try {
                PROPERTIES.load(configProperties.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static IPage getPageByName(String pageName) {
        init();
        String platformName = PROPERTIES.getProperty("platformName");
        try {
            Class<?> pageClass = Class.forName(platformName + "." + pageName);
            return (IPage) pageClass.newInstance();
        } catch (ClassNotFoundException e) {
            log.warn("Class no found: " + platformName + "." + pageName);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
