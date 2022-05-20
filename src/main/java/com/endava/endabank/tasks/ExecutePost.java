package com.endava.endabank.tasks;

import com.endava.endabank.constants.ConstantsAPI;
import com.endava.endabank.interactions.ExecutePostInteraction;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class ExecutePost implements Task {

    private String body ;

    public ExecutePost( String body) {

        this.body = body;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(ExecutePostInteraction.executePostInteraction(body));
    }

    public static ExecutePost executePost(String body){
        return Tasks.instrumented(ExecutePost.class,body);
    }



}
