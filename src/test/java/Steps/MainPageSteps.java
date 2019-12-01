package Steps;

import Core.Helper;
import cucumber.api.java.en.And;
import java.util.List;
import org.openqa.selenium.WebElement;

public class MainPageSteps extends Steps {
    @And("^I open all founded IT news at new tab$")
    public void iOpenAllFoundedITNewsAtNewTab() {
        log.info("I open all founded IT news at new tab");
        List<WebElement> titleNews = iMainPage.getTitleNews();
        for (int i = 0; i < titleNews.size(); i++) {
            Helper.goToNewTabAndOpenUrl(titleNews.get(i).getAttribute("href"));

            String headNews = iMainPage.getHeadNews().getText();
            System.out.println("headNews = " + headNews);

            Helper.closeCurrentTabAndBackToBeforeTab();
        }
    }
}
