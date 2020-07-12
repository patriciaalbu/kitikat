package api.login.login_with_email_and_password;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features="src/test/resources/features/login/login_with_email_and_password", glue = "api.login.login_with_email_and_password")
public class LoginWithEmailAndPasswordApiTest {}
