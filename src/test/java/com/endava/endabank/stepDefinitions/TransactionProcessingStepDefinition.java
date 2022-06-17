package com.endava.endabank.stepDefinitions;

import com.endava.endabank.constants.ConstantsAPI;
import com.endava.endabank.tasks.ExecutePOST;
import com.endava.endabank.utils.ConvertToJSON;
import com.endava.endabank.utils.SendMoney;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.hamcrest.Matchers;
import org.junit.Before;

import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

// In this directory we define the steps from the .feature file with the same name.
public class TransactionProcessingStepDefinition {

    @Before
    public void setTheStage() { //Trace actors we will create
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("the user is using the endpoint")
    public void theUserIsUsingTheEndpoint() {
        OnStage.setTheStage(new OnlineCast());
        theActorCalled("sourceAccount").whoCan(CallAnApi.at("http://35.202.242.69:8081/api/v1"));
    }
    @Given("logs in using the credentials")
    public void logsInUsingTheCredentials() {

        theActorInTheSpotlight().attemptsTo(
                Post.to("/login")
                        .with(request ->request.auth().basic("jaime.gonzalez@endava.com", "Admin123!")
                        ));
    }

    @Given("the user has written the POST request using the endpoint to send money")
    public void theUserHasWrittenThePOSTRequestUsingTheEndpointToSendMoney() {
        theActorInTheSpotlight().whoCan(CallAnApi.at("http://35.202.242.69:8081/api/v1"));
    }
    @When("the user types the necessary information into the body request")
    public void theUserTypesTheNecessaryInformationIntoTheBodyRequest() {
        SendMoney sendMoney = new SendMoney();
        sendMoney.setAmount("1");
        sendMoney.setBankAccountNumberIssuer("2022053111472379");
        sendMoney.setBankAccountNumberReceiver("2022053111472379");
        sendMoney.setDescription("test payment");
        sendMoney.setAddress("181.57.222.58");

        theActorInTheSpotlight().attemptsTo(Post.to("/transactions/send-money").
                with(request -> request
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
