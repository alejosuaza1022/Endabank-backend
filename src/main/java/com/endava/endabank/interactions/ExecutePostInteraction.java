package com.endava.endabank.interactions;

import com.endava.endabank.constants.ConstantsAPI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;

public class ExecutePostInteraction implements Interaction {

    private String body;

    public ExecutePostInteraction(String body) {
        this.body = body;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Post.post(ConstantsAPI.PATH_USER_LOGIN).with(
                request -> request.contentType(ConstantsAPI.CONTENT_TYPE).body(body).relaxedHTTPSValidation()

        ));
    }

    public static ExecutePostInteraction executePostInteraction(String body){
        return new ExecutePostInteraction(body);
    }
}
