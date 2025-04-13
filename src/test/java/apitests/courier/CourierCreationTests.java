package apitests.courier;

import apiactions.CourierApiActions;
import entities.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CourierCreationTests extends CourierApiActions {

    private String randomLogin;
    private String randomPassword;
    private String randomFirstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        randomLogin = UUID.randomUUID().toString();
        randomPassword = UUID.randomUUID().toString();
        randomFirstName = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("Should successfully create a courier")
    public void shouldCreateCourier() {
        Courier courier = new Courier(randomLogin, randomPassword, randomFirstName);
        Response response = createCourier(courier);

        checkResponseCode(response, 201);
        checkResponseMessage(response, "\"ok\":true");
    }

    @Test
    @DisplayName("Should not allow creating a courier with an existing login data")
    public void shouldNotCreateCourierWithSameData() {
        Courier courier = new Courier(randomLogin, randomPassword, randomFirstName);
        createCourier(courier);
        Response response = createCourier(courier);

        checkResponseCode(response, 409);
        checkResponseMessage(response, "Этот логин уже используется.");
    }

    @Test
    @DisplayName("Should not allow creating a courier with an empty login")
    public void shouldNotCreateCourierWithEmptyLogin() {
        Courier courier = new Courier("", randomPassword, randomFirstName);
        Response response = createCourier(courier);

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Should not allow creating a courier with an empty password")
    public void shouldNotCreateCourierWithEmptyPassword() {
        Courier courier = new Courier(randomLogin, "", randomFirstName);
        Response response = createCourier(courier);

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Should not allow creating a courier with an empty first name")
    public void shouldNotCreateCourierWithEmptyFirstName() {
        Courier courier = new Courier(randomLogin, randomPassword, "");
        Response response = createCourier(courier);

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Should not allow creating a courier with an empty login, password, first name")
    public void shouldNotCreateCourierWithEmptyLoginData() {
        Courier courier = new Courier("", "", "");
        Response response = createCourier(courier);

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @After
    public void clearTestingData() {

        Integer courierId = getCourierId(new Courier(randomLogin, randomPassword));

        if (courierId != null) {
            deleteCourier(courierId);
        }
    }
}