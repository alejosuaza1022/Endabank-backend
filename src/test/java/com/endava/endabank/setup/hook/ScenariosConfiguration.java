package com.endava.endabank.setup.hook;

import com.endava.endabank.constants.ConstantsAPI;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;


import static net.serenitybdd.screenplay.actors.OnStage.*;

public class ScenariosConfiguration {

    @Before
    public void initialization(){
        setTheStage(new OnlineCast());
        theActorCalled(ConstantsAPI.USER_ENDABANK_ACTOR).whoCan(CallAnApi.at(ConstantsAPI.URL));
    }

    @After
    public void finalizeAPI(){
        drawTheCurtain();
    }
}
