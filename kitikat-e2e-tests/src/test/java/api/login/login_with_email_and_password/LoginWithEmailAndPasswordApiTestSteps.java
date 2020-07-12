package api.login.login_with_email_and_password;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.core.Serenity;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static io.restassured.RestAssured.*;

public class LoginWithEmailAndPasswordApiTestSteps {

    private String givenEmail;
    private String givenPassword;

    private ValidatableResponse response;
    private String redirectHeader;

    @Given("{string} as E-mail and {string} as password")
    public void emailAsEMailAndPasswordAsPassword(String email, String password) {
        // store the given input for the next step
        this.givenEmail = email;
        this.givenPassword = password;
    }

    @When("sending a POST request to {string}")
    public void sendingAPOSTRequestToLogin(String loginUrl) {
        // prepare request and call the eandpoint
        String targetBody = "email="+ URLEncoder.encode(this.givenEmail)+"&password="+URLEncoder.encode(this.givenPassword)+"";
        URI targetUri = URI.create(loginUrl);
        this.response = with()
                .header(new Header("Content-Type", "application/x-www-form-urlencoded"))
                .content(targetBody)
                .when()
                .post(targetUri)
                .then();
    }

    @Then("the HTTP status should be {string}")
    public void theHTTPStatusShouldBeStatus(String status) {
        // assert the actual status code is the same as the expected code
        response.statusCode(Integer.valueOf(status));
    }
}
