package api.login.display_login_page;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features="src/test/resources/features/login/display_login_page", glue = "api.login.display_login_page")
public class DisplayLoginPageApiTest {}
