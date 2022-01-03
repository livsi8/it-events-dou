package Core;

import cucumber.api.java.Before;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {
    private static final Logger log =
        LogManager.getLogger(DriverFactory.class.getName());

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("beforeScenario");
    }

    @After(order = 0)
    public void afterScenario(Scenario scenario) {
        log.info("afterScenario");
        DriverFactory.destroyDriver();
    }
}
