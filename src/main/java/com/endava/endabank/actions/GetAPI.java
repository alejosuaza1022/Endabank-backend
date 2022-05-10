package com.endava.endabank.actions;

import static org.junit.Assert.assertTrue;

import com.endava.endabank.constants.Constants;
import net.serenitybdd.rest.SerenityRest;

public class GetAPI {
    public static String baseUrl = "";

	public static void setBaseUrl(String baseUrl) {
		GetAPI.baseUrl = baseUrl;
	}

	public static void executeGet(String path, int id) {
		SerenityRest.given().auth().oauth2(Constants.TOKEN).contentType(Constants.CONTENT_TYPE).when()
				.get(baseUrl.concat(path).concat(String.valueOf(id)));
	}

	public static void valiteResponse(String userName) {
		assertTrue(String.format(Constants.MSG_FAIL_RESPONSE_NAME, userName),
				SerenityRest.lastResponse().asString().contains(userName));
	}
}
