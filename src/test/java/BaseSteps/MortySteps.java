package BaseSteps;



import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static utils.Configuration.getConfVal;
import static utils.Configuration.getOtherVal;


public class MortySteps {

    public static String lastEpisode;
    public static String lastCharacter;

    @Step("делаем запрос о Морти и последнем эпизоде с его участием передавая его id")
    @Когда("^делаем запрос о Морти и последнем эпизоде с его участием передавая его id$")
    public static void InfoAboutMorty() throws IOException {
        //get info about Morty and link to last episode
        JSONObject Morty = new JSONObject();
        Response aboutMorty = given()
                .header("Content-type", "application/json")
                .baseUri(getConfVal("mortyURL"))
                .when()
                .get("/character/" + getOtherVal("idChar"))
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();
        Morty.put("species", new JSONObject(aboutMorty.getBody().asString()).get("species").toString());
        Morty.put("location", new JSONObject(aboutMorty.getBody().asString())
                .getJSONObject("location").get("name").toString());
        Files.write(Paths.get("src/test/resources/json/Morty.json"),
                Morty.toString().getBytes(StandardCharsets.UTF_8));
        JSONArray tempArray = new JSONObject(aboutMorty.getBody().asString()).getJSONArray("episode");
        int lenghtAr = tempArray.length();
        lastEpisode = tempArray.get(lenghtAr-1).toString().replace(getConfVal("mortyURL"), "");

    }

    @Step("делаем запрос о ссылке на последнего персонажа последнего эпизода")
    @И("^делаем запрос о ссылке на последнего персонажа последнего эпизода$")
    public static void InfoLastEpisode() {
        //get link to last character
        Response aboutLastCharacter = given()
                .header("Content-type", "application/json")
                .baseUri(getConfVal("mortyURL"))
                .when()
                .get(lastEpisode)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();
        JSONArray tempArray = new JSONObject(aboutLastCharacter.getBody().asString()).getJSONArray("characters");
        int lenghtAr = tempArray.length();
        lastCharacter = tempArray.get(lenghtAr-1).toString().replace(getConfVal("mortyURL"), "");

    }

    @Step("получаем инфо этого персонажа")
    @И("^получаем инфо этого персонажа$")
    public static void InfoLastCharacter() throws IOException {
        //get info about last character
        JSONObject OtherChar = new JSONObject();
        Response aboutLastCharacter = given()
                .header("Content-type", "application/json")
                .baseUri(getConfVal("mortyURL"))
                .when()
                .get(lastCharacter)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();
        OtherChar.put("species", new JSONObject(aboutLastCharacter.getBody().asString()).get("species").toString());
        OtherChar.put("location", new JSONObject(aboutLastCharacter.getBody().asString())
                .getJSONObject("location").get("name").toString());
        Files.write(Paths.get("src/test/resources/json/OtherChar.json"),
                OtherChar.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Step("сравниваем параметры Морти и полученного персонажа")
    @Тогда("^сравниваем параметры Морти и полученного персонажа$")
    public static void compareOfParameters() throws IOException {
        //compare of parameters
        JSONObject Morty = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/Morty.json"))));
        JSONObject OtherChar = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/OtherChar.json"))));
        Assertions.assertEquals(Morty.get("species"), OtherChar.get("species"), "Different species");
        Assertions.assertEquals(Morty.get("location"), OtherChar.get("location"), "Different location");

    }
}
