package api.login.display_login_page;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.core.Serenity;
import org.junit.Assert;

import java.net.URI;
import java.net.URLEncoder;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;

public class DisplayLoginPageApiTestSteps {

    private String givenLoginUrl;
    private ValidatableResponse response;

    @Given("a known {string}")
    public void aKnownLogin_url(String loginUrl) {
        this.givenLoginUrl = loginUrl;
    }

    @When("sending a GET request")
    public void sendingAGETRequest() {
        URI targetUri = URI.create(this.givenLoginUrl);
        this.response =  when().get(targetUri).then();
    }

    @Then("the HTTP status should be {string}")
    public void theHTTPStatusShouldBeStatus(String status) {
        // assert the actual status code is the same as the expected code
        response.statusCode(Integer.valueOf(status));
    }

    @Then("the contents should contain a login form")
    public void theContentsShouldContainALoginForm() {
        String contentsHtml = response.extract().asString();
        Serenity.recordReportData().withTitle("Received Contents").andContents(contentsHtml);
        Assert.assertTrue(contentsHtml.contains("container login-container"));
    }
}
