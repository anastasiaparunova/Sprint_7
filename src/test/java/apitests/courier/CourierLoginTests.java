package apitests.courier;

import apiactions.CourierApiActions;
import entities.Courier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import java.util.UUID;

public class CourierLoginTests extends CourierApiActions {

    private String randomLogin;
    private String randomPassword;
    private String randomFirstName;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        randomLogin = UUID.randomUUID().toString();
        randomPassword = UUID.randomUUID().toString();
        randomFirstName = UUID.randomUUID().toString();

        courier = new Courier(randomLogin, randomPassword, randomFirstName);
        createCourier(courier);
    }

    @Test
    @DisplayName("Should login successfully when all credentials are valid")
    public void shouldLoginSuccessfullyWithValidCredentials() {
        Response response = courierLogin(courier);

        checkResponseCode(response, 200);
        Integer courierId = getCourierId(new Courier(randomLogin, randomPassword));
        Assert.assertNotNull(courierId);
    }

    @Test
    @DisplayName("Should return error when login is missing")
    public void shouldNotLoginWithEmptyLogin() {
        Response response = courierLogin(new Courier("", randomPassword));

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Should return error when password is missing")
    public void shouldNotLoginWithEmptyPassword() {
        Response response = courierLogin(new Courier(randomLogin, ""));

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Should return error when first name is missing")
    public void shouldNotLoginWithEmptyLoginAndPassword() {
        Response response = courierLogin(new Courier("", ""));

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Should return error when login is invalid")
    public void shouldNotLoginWithIncorrectLogin() {
        Response response = courierLogin(new Courier(randomLogin+"_invalid", randomPassword));

        checkResponseCode(response, 404);
        checkResponseMessage(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Should return error when password is invalid")
    public void shouldNotLoginWithIncorrectPassword() {
        Response response = courierLogin(new Courier(randomLogin, randomPassword+"_invalid"));

        checkResponseCode(response, 404);
        checkResponseMessage(response, "Учетная запись не найдена");
    }


    @Test
    @DisplayName("Should return error when non-existent credentials")
    public void shouldNotLoginWithNonExistentCourier() {
        Courier nonExistentCourier = new Courier("nonexistentLogin", "nonexistentPassword", "Non Existent");

        Response response = courierLogin(nonExistentCourier);

        checkResponseCode(response, 404);
        checkResponseMessage(response, "Учетная запись не найдена");
    }


    @After
    public void clearTestingData() {
        Integer courierId = getCourierId(new Courier(randomLogin, randomPassword));

        if (courierId != null) {
            deleteCourier(courierId);
        }
    }
}
