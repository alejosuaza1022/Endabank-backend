package com.endava.endabank.stepDefinitions;

import com.endava.endabank.constants.ConstantsAPI;
import com.endava.endabank.tasks.ExecutePOST;
import com.endava.endabank.utils.AuthenticationToken;
import com.endava.endabank.utils.ConvertToJSON;
import com.endava.endabank.utils.SendMoney;
import com.endava.endabank.utils.Credentials;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.http.Header;
import net.serenitybdd.core.annotations.events.BeforeScenario;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;

import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

// In this directory we define the steps from the .feature file with the same name.
public class TransactionProcessingStepDefinition {

    SendMoney sendMoney = new SendMoney();
    AuthenticationToken authenticationToken = new AuthenticationToken();

    @BeforeScenario
    public void setTheStage() { //Trace actors we will create
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("the user is using the endpoint")
    public void theUserIsUsingTheEndpoint() {
        //theActorCalled("sourceAccount").whoCan(CallAnApi.at("http://localhost:8080/api/v1"));
        theActorCalled("sourceAccount").whoCan(CallAnApi.at("http://35.202.242.69:8081/api/v1"));
    }
    @Given("logs in using the credentials")
    public void logsInUsingTheCredentials() {

        Credentials credentials = new Credentials("jaime.gonzalez@endava.com","Admin123!");
        theActorInTheSpotlight().attemptsTo(Post.to("/login").
                with(request -> request
                        .contentType(ConstantsAPI.CONTENT_TYPE)
                        .body(credentials)
                ));

        theActorInTheSpotlight().should(seeThatResponse(
                "Authorization success",
                response -> response.statusCode(200)));

    }

    @Given("the user has written the POST request using the endpoint to send money")
    public void theUserHasWrittenThePOSTRequestUsingTheEndpointToSendMoney() {

        authenticationToken.setToken(SerenityRest.lastResponse().jsonPath().getString("accessToken"));
        //theActorInTheSpotlight().whoCan(CallAnApi.at("http://localhost:8080/api/v1"));
        //theActorInTheSpotlight().whoCan(CallAnApi.at("http://35.202.242.69:8081/api/v1"));

    }
    @When("the user types the necessary information into the body request")
    public void theUserTypesTheNecessaryInformationIntoTheBodyRequest() {

        sendMoney.setAmount("1");
        sendMoney.setBankAccountNumberIssuer("2022053111472379");
        sendMoney.setBankAccountNumberReceiver("2022053111472379");
        sendMoney.setDescription("test payment");
        sendMoney.setAddress("181.57.222.58");

        theActorInTheSpotlight().attemptsTo(Post.to("/transactions/send-money").
                with(request -> request.header("Authorization","Bearer "+ authenticationToken.getToken())
                        .contentType(ConstantsAPI.CONTENT_TYPE)
                        .body(sendMoney)
        ));

    }

    @Then("the user should see the next body response")
    public void theUserShouldSeeTheNextBodyResponse() {

        theActorInTheSpotlight().should(seeThatResponse(
                "The transaction failed.",
                response -> response.statusCode(422)));

    }

}
