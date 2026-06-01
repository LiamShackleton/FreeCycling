package freecycling.example.freecycling;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import freecycling.example.freecycling.Core.ItemRepository;
import freecycling.example.freecycling.Core.UserRepository;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private static String token;
    private static String testUsername = "e2e_item_" + System.currentTimeMillis();
    private static int createdItemId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterAll
    public static void cleanUp(
            @Autowired UserRepository userRepository,
            @Autowired ItemRepository itemRepository) {
        itemRepository.findAll().stream()
            .filter(i -> "Old Bike E2E".equals(i.getItemName()))
            .forEach(itemRepository::delete);
        userRepository.findAll().stream()
            .filter(u -> u.getUserName().startsWith("e2e_item_"))
            .forEach(userRepository::delete);
    }

    @Test
    @Order(1)
    public void testSetup() {
        String body = """
            {
                "userName": "%s",
                "pass": "Password123",
                "email": "e2eitem@mail.com"
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
    }

    @Test
    @Order(2)
    public void testPostItem() {
        String body = """
            {
                "itemName": "Old Bike E2E",
                "itemAvailability": "available",
                "itemType": "Sports",
                "itemDescription": "A used bike",
                "itemImageUrl": "http://example.com/bike.jpg"
            }
            """;

        String id = given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/items")
        .then()
            .statusCode(200)
            .extract().asString();

        createdItemId = Integer.parseInt(id);
        Assertions.assertTrue(createdItemId > 0);
    }

    @Test
    @Order(3)
    public void testPostItemWithoutToken() {
        String body = """
            {
                "itemName": "Old Bike E2E",
                "itemAvailability": "available",
                "itemType": "Sports",
                "itemDescription": "A used bike"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/items")
        .then()
            .statusCode(403);
    }

    @Test
    @Order(4)
    public void testGetItemById() {
        given()
        .when()
            .get("/items/" + createdItemId)
        .then()
            .statusCode(200)
            .body("itemName", equalTo("Old Bike E2E"));
    }

    @Test
    @Order(5)
    public void testGetItemByIdNotFound() {
        given()
        .when()
            .get("/items/999999")
        .then()
            .statusCode(404);
    }

    @Test
    @Order(6)
    public void testGetAvailableItems() {
        given()
        .when()
            .get("/items/getAvailable")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(7)
    public void testGetAllItems() {
        given()
        .when()
            .get("/items")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(8)
    public void testUpdateAvailability() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .patch("/items/availability?itemId=" + createdItemId + "&availability=Given Away")
        .then()
            .statusCode(200);
    }

    @Test
    @Order(9)
    public void testUpdateRecipient() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .patch("/items/recipient?itemId=" + createdItemId + "&recipientId=1")
        .then()
            .statusCode(200);
    }
}
