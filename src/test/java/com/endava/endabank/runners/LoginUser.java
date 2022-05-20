package com.endava.endabank.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        glue = {"com.endava.endabank.stepsdefinitions","com.endava.endabank.setup"},
        features = "src/test/resources/features/loginUser.feature",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        tags = "@Login"
)

public class LoginUser {

}
