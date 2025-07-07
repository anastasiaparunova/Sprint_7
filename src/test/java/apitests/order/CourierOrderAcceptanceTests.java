package apitests.order;

import apiactions.CourierApiActions;
import apiactions.OrderApiActions;
import entities.Courier;
import entities.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CourierOrderAcceptanceTests extends OrderApiActions {

    private String randomLogin;
    private String randomPassword;
    int courierId;
    int orderId;
    int trackNumber;
    CourierApiActions courierAction = new CourierApiActions();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        //Тестовые данные для создания курьера
        randomLogin = UUID.randomUUID().toString();
        randomPassword = UUID.randomUUID().toString();
        String randomFirstName = UUID.randomUUID().toString();

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

        Courier courier = new Courier(randomLogin, randomPassword, randomFirstName);
        //Создаем курьера
        courierAction.createCourier(courier);
        //Добываем id курьера
        courierId = courierAction.getCourierId(new Courier(randomLogin, randomPassword));

        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        //Создаем заказ и вытаскиваем его track-номер
        trackNumber = getTrackNumber(order);
        //Ищем id заказа по track-номеру
        orderId = getOrderIdByTrackNumber(trackNumber);

    }

    @Test
    @DisplayName("Should successfully assign order to courier")
    public void shouldAcceptOrder() {
        Response response = acceptOrder(orderId, courierId);

        checkResponseCode(response, 200);
        checkResponseMessage(response, "\"ok\":true");
    }

    @Test
    @DisplayName("Should return error when courier id is missing")
    public void shouldNotAcceptOrderWithoutCourierId() {
        Response response = acceptOrder(orderId, null);

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Should return error when courier id is non-existent")
    public void shouldNotAcceptOrderWithNonExistentCourierId() {
        Response response = acceptOrder(orderId, courierId + 10000);

        checkResponseCode(response, 404);
        checkResponseMessage(response, "Курьера с таким id не существует");
    }

    @Test
    @DisplayName("Should return error when order id is missing")
    public void shouldNotAcceptOrderWithoutOrderId() { //если не передать номер заказа, запрос вернёт ошибку;
        Response response = acceptOrder(null, courierId);

        checkResponseCode(response, 400);
        checkResponseMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Should return error when order id is non-existent")
    public void shouldNotAcceptOrderWithNonExistentOrderId() {

        Response response = acceptOrder(orderId + 10000, courierId);

        checkResponseCode(response, 404);
        checkResponseMessage(response, "Заказа с таким id не существует");
    }

    @After
    public void clearTestingData() {
        Integer courierId = courierAction.getCourierId(new Courier(randomLogin, randomPassword));

        if (courierId != null) {
            courierAction.deleteCourier(courierId);
        }
    }

}
