package com.endava.endabank.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        glue = "com.endava.endabank.stepDefinitions",
        features = "src/test/resources/features/transactionProcessing.feature",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        tags = "@firstScenario"
)

public class TransactionProcessing {

}
