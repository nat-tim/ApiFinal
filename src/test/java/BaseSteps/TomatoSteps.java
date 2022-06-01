package BaseSteps;


import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static utils.Configuration.getConfVal;

public class TomatoSteps {

    public static String[] arrayKey = {"createdAt","name","id","job"};

    @Step("передаем запрос на создание нового пользователя")
    @Когда("передаем запрос на создание нового пользователя")
    public static void createUser() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/baseDate.json"))));
        body.put("name", "Tomato");
        body.put("job", "Eat market");

        Response creatMrTomato = given()
                .header("Content-type", "application/json")
                .header("charset", "utf-8")
                .baseUri(getConfVal("reqURL"))
                .body(body.toString())
                .when()
                .post("/users")
                .then()
                .assertThat().statusCode(201)
                .extract()
                .response();
        Files.write(Paths.get("src/test/resources/json/mrTomato.json"),
                (new JSONObject(creatMrTomato.getBody().asString())).toString().getBytes(StandardCharsets.UTF_8));
    }

    @Step("проверяем полученный результат")
    @Тогда("проверяем полученный результат")
    public static void validDate() throws IOException{
        JSONObject mrTomato = new JSONObject(new String(Files.readAllBytes(Paths
                .get("src/test/resources/json/mrTomato.json"))));
        AssertKey(mrTomato, arrayKey);
        Assertions.assertEquals(mrTomato.get("name"), "Tomato", "Name is not valid");
        Assertions.assertEquals(mrTomato.get("job"), "Eat market", "Job is not valid");
        AssertNum(mrTomato.get("id").toString());
        AssertDate(mrTomato.get("createdAt").toString());


    }
    @Step("проверяем наличие всех ключей")
    static void AssertKey(JSONObject mr, String[] arKey){
        for (String key: arKey){
            Assert.assertFalse(String.format("key \"%s\" is not found", key), (!mr.has(key)));
        }
    }
    @Step("проверяем валидность поля id")
    static void AssertNum(String valId) {
        Assert.assertFalse("parameter \"id\" is not valid", (!NumberUtils.isDigits(valId)));
    }

    @Step("проверяем валидность даты")
    static void AssertDate(String valCreate){
        Assert.assertFalse("parameter \"createdAt\" is not valid", valCreate.length() < 10);
        String s = valCreate.substring(0, 10);
        String sNow = (LocalDateTime.now().toString()).substring(0, 10);
        Assert.assertEquals("parameter \"createdAt\" is not valid", s, sNow);
    }

}
