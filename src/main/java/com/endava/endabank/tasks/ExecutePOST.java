package com.endava.endabank.tasks;

import com.endava.endabank.constants.ConstantsAPI;
import com.endava.endabank.utils.SendMoney;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

// Tasks are sets of actions, such as clicks, enters, etc.,
// aiming for a specific functionality
public class ExecutePOST implements Task {

    private String path;
    private String body;

    public ExecutePOST(String path, String body) {
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

    public static ExecutePOST executePOST(String path, String body){
        return  Tasks.instrumented(ExecutePOST.class,path,body);
    }
}
