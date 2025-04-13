package apitests.order;

import apiactions.OrderApiActions;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;


public class OrderListTest extends OrderApiActions {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Should return a list of orders") // имя теста
    public void shouldReturnOrderList() {
        Response response = getOrderList();
        checkResponseCode(response, 200);
        verifyOrderListIsNotEmpty(response);
    }
}
