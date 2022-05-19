package com.endava.endabank.stepsdefinitions;

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

    private static String path="";
    @Before

    public void setTheStage(){
        OnStage.setTheStage(new OnlineCast());
    }
    @Given("the user is on the Login page")
    public void theUserIsOnTheLoginPage(Map<String,String> conection) {
        theActorCalled(conection.get("Actor")).whoCan(CallAnApi.at(conection.get("URL")));
        path=conection.get("Path");
    }
    @When("the user logs in using its credentials")
    public void theUserLogsInUsingItsCredentials(Map<String,String> credentials) {
        System.out.println(ConvertToJSON.bodyJSON(credentials));
        theActorInTheSpotlight().attemptsTo(ExecutePost.executePost(path, ConvertToJSON.bodyJSON(credentials)));
    }
    @Then("the user should be allowed to access to the application")
    public void theUserShouldBeAllowedToAccessToTheApplication(Map<String,String> status) {
        theActorInTheSpotlight().should(seeThatResponse("the user is approved",response -> response.statusCode(200).body(status.get("field"), Matchers.equalTo(Boolean.TRUE))));
    }


}
