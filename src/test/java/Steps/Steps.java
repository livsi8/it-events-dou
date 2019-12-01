package Steps;

import static Core.Config.getPageByName;
import Dou.Interface.ILoginPage;
import Dou.Interface.IMainPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Steps {
    protected final Logger log = LogManager.getLogger(this.getClass());
    protected ILoginPage iLoginPage = (ILoginPage) getPageByName("LoginPage");
    protected IMainPage  iMainPage  = (IMainPage) getPageByName("MainPage");
}
