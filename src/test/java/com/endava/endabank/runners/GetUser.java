package com.endava.endabank.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
		glue = "com.endava.endabank.stepdefinitions",
		features = "src/test/resources/features/get_user.feature")

public class GetUser {

}