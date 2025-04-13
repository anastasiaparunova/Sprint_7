package apitests.order;

import apiactions.OrderApiActions;
import entities.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderByTrackNumberTests extends OrderApiActions {

    int trackNumber;
    Order order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        //Тестовые данные для создания заказа
        String firstName = "TestName";
        String lastName = "TestSurname";
        String address = "TestAddress, 42 apt";
        String metroStation = "TestMetro";
        String phone = "+7 800 555 35 35";
        int rentTime = 5;
        String deliveryDate = "2025-06-06";
        String comment = "Test comment for the courier";
        String[] color = {"GREY", "BLACK"};

        order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        trackNumber = getTrackNumber(order);
    }

    @Test
    @DisplayName("Should return order by track number")
    public void shouldReturnOrder() {
        Response response = getOrderByTrackNumber(trackNumber);

        checkResponseCode(response, 200);
        Assert.assertNotNull(response.path("order"));
    }


    @Test
    @DisplayName("Should return error when track number is missing")
    public void shouldReturnErrorWhenMissingTrackNumber() {
        Response response = getOrderByTrackNumber(null);

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для поиска");

    }

    @Test
    @DisplayName("Should return error when track number is non-existent")
    public void shouldReturnErrorForNonExistentTrackNumber() {
        Response response = getOrderByTrackNumber(trackNumber + 10000);

        checkResponseCode(response, 404);
        checkResponseMessage(response, "Заказ не найден");
    }

}
