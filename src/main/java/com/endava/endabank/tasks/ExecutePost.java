package com.endava.endabank.tasks;

import com.endava.endabank.constants.ConstantsAPI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class ExecutePost implements Task {

    public ExecutePost(String path, String body) {
        this.path = path;
        this.body = body;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(Post.to(path).with(
                request -> request
                        .contentType(ConstantsAPI.CONTENT_TYPE)
                        .body(body)
        ));
    }

    public static ExecutePost executePost(String path, String body){
        return Tasks.instrumented(ExecutePost.class,path,body);
    }

    private String path ;
    private String body ;
}
