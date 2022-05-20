package com.endava.endabank.stepsdefinitions;

import com.endava.endabank.constants.ConstantsAPI;
import com.endava.endabank.tasks.ExecutePost;
import com.endava.endabank.utils.ConvertToJSON;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.hamcrest.Matchers;

import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class LoginUserStepDefinition {

    @When("the user logs in using its credentials")
    public void theUserLogsInUsingItsCredentials(Map<String,String> credentials) {
        System.out.println(ConvertToJSON.bodyJSON(credentials));
        theActorInTheSpotlight().attemptsTo(ExecutePost.executePost(ConvertToJSON.bodyJSON(credentials)));
    }
    @Then("the user should be allowed to access to the application")
    public void theUserShouldBeAllowedToAccessToTheApplication(Map<String,String> status) {
        theActorInTheSpotlight().should(seeThatResponse("the user is approved",response -> response.statusCode(200).body(status.get("field"), Matchers.equalTo(Boolean.TRUE))));
    }


}
