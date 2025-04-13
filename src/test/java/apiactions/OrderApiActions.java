package apiactions;

import entities.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderApiActions extends ApiActions {

    @Step("Send POST request to /api/v1/orders to create an Order")
    public Response createOrder(Order order) {

        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Send GET request to /api/v1/orders to retrieve Order List")
    public Response getOrderList() {

        return given()
                .when()
                .get("/api/v1/orders");
    }

    @Step("Get Order's Track Number")
    public int getTrackNumber(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract()
                .path("track");
    }

    @Step("Get Order by Track Number")
    public Response getOrderByTrackNumber(Object trackNumber) {

        return given()
                .when()
                .queryParam("t", trackNumber)
                .get("/api/v1/orders/track");
    }

    @Step("Get Order Id by it's Track Number")
    public int getOrderIdByTrackNumber(Object trackNumber) {

        return given()
                .when()
                .queryParam("t", trackNumber)
                .get("/api/v1/orders/track")
                .then()
                .extract()
                .path("order.id");
    }


    @Step("Check that the Order List is not empty and contains at least one element")
    public void verifyOrderListIsNotEmpty(Response response) {
        response.then()
                .body("orders", notNullValue())
                .assertThat()
                .body("orders.size()", greaterThan(0));
    }

    @Step("Send PUT request to /api/v1/orders/accept/:id to accept an Order")
    public Response acceptOrder(Object orderId, Object courierId) {

        if (orderId != null) {
            return given()
                    .queryParam("courierId", courierId)
                    .when()
                    .put("/api/v1/orders/accept/" + orderId);
        } else {
            return given()
                    .when()
                    .put("/api/v1/orders/accept/" + "courierId=" + courierId);
        }
    }

}
