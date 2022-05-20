package com.endava.endabank.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.RestInteraction;

import static net.serenitybdd.screenplay.rest.abilities.CallAnApi.as;

public class Post extends RestInteraction {


    private String resource;

    public Post(String resource) {
        this.resource = resource;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        rest().log().all().post(as(actor).resolve(this.resource)).then().log().all();

    }

    public static Post post(String resource){
        return new Post(resource);
    }
}
