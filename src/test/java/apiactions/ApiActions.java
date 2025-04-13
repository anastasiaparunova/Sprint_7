package apiactions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;

public class ApiActions {

    @Step("Compare a response code to an expected one")
    public void checkResponseCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }


    @Step("Compare a response message to an expected one")
    public void checkResponseMessage(Response response, String expectedText) {
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Response message does not match expected value", responseBody.contains(expectedText));
    }
}
