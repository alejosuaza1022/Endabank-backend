package com.endava.endabank.stepDefinitions;

import com.endava.endabank.constants.ConstantsAPI;
import com.endava.endabank.tasks.ExecutePOST;
import com.endava.endabank.utils.AuthenticationToken;
import com.endava.endabank.utils.ConvertToJSON;
import com.endava.endabank.utils.SendMoney;
import com.endava.endabank.utils.Credentials;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.core.annotations.events.BeforeScenario;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.equalTo;

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

    }
    @When("the user types the information into the body request")
    public void theUserTypesTheInformationIntoTheBodyRequest(Map<String,String> bodyRequest) {

        sendMoney.setAmount(bodyRequest.get("amount"));
        sendMoney.setBankAccountNumberIssuer(bodyRequest.get("bANIssuer"));
        sendMoney.setBankAccountNumberReceiver(bodyRequest.get("bANReceiver"));
        sendMoney.setDescription(bodyRequest.get("description"));
        sendMoney.setAddress(bodyRequest.get("address"));

        theActorInTheSpotlight().attemptsTo(Post.to("/transactions/send-money").
                with(request -> request.header("Authorization","Bearer "+ authenticationToken.getToken())
                        .contentType(ConstantsAPI.CONTENT_TYPE)
                        .body(sendMoney)
        ));

    }

    @Then("the user should see the next body response")
    public void theUserShouldSeeTheNextBodyResponse(Map<String,String> bodyResponse) {

        theActorInTheSpotlight().should(seeThatResponse(
                "Transaction status.",
                response -> response.statusCode(Integer.parseInt(bodyResponse.get("statusCode")))
                        .body(bodyResponse.get("name"),equalTo(bodyResponse.get("value")))));

    }

}
