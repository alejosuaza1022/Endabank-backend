package com.endava.endabank.stepdefinitions;

import com.endava.endabank.actions.GetAPI;

import io.cucumber.java.en.*;

public class GetUserStepDefinition {
    
	@Given("^Establezco la base (.*)$")
	public void establezcoLaBaseHttpsGorestCoIn(String baseUrl) {
		GetAPI.setBaseUrl(baseUrl);
	}


	@When("^Deseo consultar la informacion del (.*) por (\\d+)$")
	public void deseoConsultarLaInformacionDelPublicApiUsersPor(String path, int id) {
		GetAPI.executeGet(path, id);
	}

	@Then("^Valido que el nombre sea \"([^\"]*)\"$")
	public void validoQueElNombreSea(String userName) {
		GetAPI.valiteResponse(userName);
	}
}
