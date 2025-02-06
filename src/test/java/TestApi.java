import com.github.dockerjava.transport.DockerHttpClient;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestApi {

    @Test
    public void checkApi(){
        String baseUri= "https://api.coindesk.com/v1/bpi/currentprice.json";
        Response response =
                given()
                        .baseUri(baseUri.trim())
                        .when()
                        .get()
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .body("bpi.USD", notNullValue())
                        .body("bpi.GBP", notNullValue())
                        .body("bpi.EUR", notNullValue())
                        .body("bpi.GBP.description", equalTo("British Pound Sterling"))
                        .extract()
                        .response();
        System.out.println(response.asPrettyString());


    }
}
