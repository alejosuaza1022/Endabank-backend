package com.endava.endabank.stepsdefinitions;

import com.endava.endabank.constants.KeysJSON;
import com.endava.endabank.constants.MessageResponse;
import com.endava.endabank.exceptions.ErrorBody;
import com.endava.endabank.questions.PostBodyDetail;
import com.endava.endabank.tasks.ExecutePost;
import com.endava.endabank.utils.ConvertToJSON;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserStepDefinition {


    @When("the user logs in using its credentials")
    public void theUserLogsInUsingItsCredentials(Map<String,String> credentials) {
        System.out.println(ConvertToJSON.bodyJSON(credentials));
        theActorInTheSpotlight().attemptsTo(ExecutePost.executePost(ConvertToJSON.bodyJSON(credentials)));
    }
    @Then("the user should be allowed to access to the application")
    public void theUserShouldBeAllowedToAccessToTheApplication(Map<String,String> status) {
        theActorInTheSpotlight().should(seeThat(PostBodyDetail
                .postBodyDetail(KeysJSON.ROLE),
                equalTo(MessageResponse.ROLE_USER))
                .orComplainWith(ErrorBody.class,ErrorBody.MESSAGE_ERROR_USER_ROLE));
    }


}
