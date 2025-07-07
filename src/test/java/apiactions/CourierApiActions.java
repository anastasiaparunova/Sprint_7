package apiactions;

import entities.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApiActions extends ApiActions {

    @Step("Send POST request to /api/v1/courier to create a Courier")
    public Response createCourier(Courier courier) {

        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }


    @Step("Send POST request to /api/v1/courier/login to login via a Courier")
    public Response courierLogin(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Send POST request to /api/v1/courier/login to extract Courier id")
    public Integer getCourierId(Courier courier) {

        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
    }

    @Step("Send DELETE request to /api/v1/courier/ to delete a Courier")
    public Response deleteCourier(Object id) {

        return given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id);
    }
}
