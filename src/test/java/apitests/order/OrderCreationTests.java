package apitests.order;

import apiactions.OrderApiActions;
import entities.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OrderCreationTests extends OrderApiActions {

    private final String[] color;
    String firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;

    public OrderCreationTests(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{"BLACK", "GRAY", "TEST-COLOUR"}},
                {new String[]{"TEST-COLOUR"}},
                {new String[]{""}},
                {new String[]{}},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        firstName = "TestName";
        lastName = "TestSurname";
        address = "TestAddress, 42 apt";
        metroStation = "TestMetro";
        phone = "+7 800 555 35 35";
        rentTime = 5;
        deliveryDate = "2025-06-06";
        comment = "Test comment for the courier";
    }

    @Test
    @DisplayName("Should create order with selected colours")
    public void orderCreatedWithDifferentColors() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = createOrder(order);

        checkResponseCode(response, 201);
        checkResponseMessage(response, "track");
    }

}
