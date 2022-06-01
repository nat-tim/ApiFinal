package Hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
public class Hooks {
    @Before
    public static void before() {
        RestAssured.filters(new AllureRestAssured()) ;
    }

    @After
    public static void after() {
        RestAssured.reset();
    }

}
