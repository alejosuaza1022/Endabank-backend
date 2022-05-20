package com.endava.endabank.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class PostBodyDetail implements Question<String > {
    private String route;

    public PostBodyDetail(String route) {
        this.route = route;
    }

    @Override
    public String answeredBy(Actor actor){
    return SerenityRest.lastResponse().jsonPath().getString(this.route).trim();
    }

    public static PostBodyDetail postBodyDetail(String route){
        return new PostBodyDetail(route);
    }
}
