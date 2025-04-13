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

public class CourierDeletionTests extends CourierApiActions {

    private String randomLogin;
    private String randomPassword;
    private String randomFirstName;
    Response response;
    Courier courier;
    int id;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        randomLogin = UUID.randomUUID().toString();
        randomPassword = UUID.randomUUID().toString();
        randomFirstName = UUID.randomUUID().toString();
        courier = new Courier(randomLogin, randomPassword, randomFirstName);
        createCourier(courier);
        id = getCourierId(new Courier(randomLogin, randomPassword));
    }

    @Test
    @DisplayName("Should successfully delete a courier")
    public void shouldDeleteCourier() {
        response = deleteCourier(id);

        checkResponseCode(response, 200);
        checkResponseMessage(response, "\"ok\":true");
    }

    //Тест не проходит, так как фактическтй результат - 404 "Not Found" вместо ожидаемого 400 кода.
    @Test
    @DisplayName("Should return error when courier id is missing")
    public void shouldNotDeleteWithoutId() {
        response = deleteCourier("");

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для удаления курьера");
    }


    @Test
    @DisplayName("Should return error when courier id is non-existent")
    public void shouldNotDeleteCourierWithNonExistentId() {
        response = deleteCourier(id + 10000);
        checkResponseCode(response, 404);
        checkResponseMessage(response, "Курьера с таким id нет");
    }

    @After
    public void clearTestingData() {

        Integer courierId = getCourierId(new Courier(randomLogin, randomPassword));

        if (courierId != null) {
            deleteCourier(courierId);
        }
    }


}
