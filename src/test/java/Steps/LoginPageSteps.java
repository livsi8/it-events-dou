package Steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class LoginPageSteps extends Steps {
    @When("I login to the platform")
    public void iLoginToThePlatform() {
        log.info("I open url");
        iLoginPage.open();
    }

    @And("^I go to platform ([^\"]*) by url$")
    public void iGoToPlatformUrl(String url) {
        log.info("I go to platform " + url + " by url");
        iLoginPage.openPlatform(url);
    }
}
