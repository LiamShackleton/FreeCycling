package freecycling.example.freecycling;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import freecycling.example.freecycling.Core.UserRepository;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private static String token;
    private static String testUsername = "e2e_user_" + System.currentTimeMillis();

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterAll
    public static void cleanUp(@Autowired UserRepository userRepository) {
        userRepository.findAll().stream()
            .filter(u -> u.getUserName().startsWith("e2e_user_"))
            .forEach(userRepository::delete);
    }

    @Test
    @Order(1)
    public void testRegisterUser() {
        String body = """
            {
                "userName": "%s",
                "pass": "Password123",
                "email": "e2etest@mail.com"
            }
            """.formatted(testUsername);

        token = given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().asString();

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isBlank());
    }

    @Test
    @Order(2)
    public void testLogin() {
        String body = """
            {
                "userName": "%s",
                "pass": "Password123"
            }
            """.formatted(testUsername);

        token = given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/users/login")
        .then()
            .statusCode(200)
            .extract().asString();

        Assertions.assertNotNull(token);
    }

    @Test
    @Order(3)
    public void testLoginWrongPassword() {
        String body = """
            {
                "userName": "%s",
                "pass": "wrongpassword"
            }
            """.formatted(testUsername);

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/users/login")
        .then()
            .statusCode(401);
    }

    @Test
    @Order(4)
    public void testLoginUnknownUser() {
        String body = """
            {
                "userName": "nobody_at_all",
                "pass": "Password123"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/users/login")
        .then()
            .statusCode(401);
    }

    @Test
    @Order(5)
    public void testGetMe() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/users/me")
        .then()
            .statusCode(200)
            .body("userName", equalTo(testUsername));
    }

    @Test
    @Order(6)
    public void testGetMeWithoutToken() {
        given()
        .when()
            .get("/users/me")
        .then()
            .statusCode(403);
    }

    @Test
    @Order(7)
    public void testGetAllUsers() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }
}