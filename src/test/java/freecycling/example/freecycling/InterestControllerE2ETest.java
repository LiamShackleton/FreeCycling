package freecycling.example.freecycling;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import freecycling.example.freecycling.Core.InterestRepository;
import freecycling.example.freecycling.Core.ItemRepository;
import freecycling.example.freecycling.Core.UserRepository;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InterestControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InterestRepository interestRepository;

    private static String donorToken;
    private static String recipientToken;
    private static String donorUsername = "e2e_donor_" + System.currentTimeMillis();
    private static String recipientUsername = "e2e_recip_" + System.currentTimeMillis();
    private static int createdItemId;
    private static int createdInterestId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterAll
    public static void cleanUp(
            @Autowired UserRepository userRepository,
            @Autowired ItemRepository itemRepository,
            @Autowired InterestRepository interestRepository) {
        interestRepository.findAll().stream()
            .filter(i -> "I want this bike".equals(i.getNote()))
            .forEach(interestRepository::delete);
        itemRepository.findAll().stream()
            .filter(i -> "Bike For Interest Test".equals(i.getItemName()))
            .forEach(itemRepository::delete);
        userRepository.findAll().stream()
            .filter(u -> u.getUserName().startsWith("e2e_donor_") || u.getUserName().startsWith("e2e_recip_"))
            .forEach(userRepository::delete);
    }

    @Test
    @Order(1)
    public void testSetup() {
        // Register donor
        donorToken = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "userName": "%s",
                    "pass": "Password123",
                    "email": "donor@mail.com"
                }
                """.formatted(donorUsername))
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().asString();

        // Register recipient
        recipientToken = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "userName": "%s",
                    "pass": "Password123",
                    "email": "recip@mail.com"
                }
                """.formatted(recipientUsername))
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().asString();

        // Donor posts an item
        String itemId = given()
            .header("Authorization", "Bearer " + donorToken)
            .contentType(ContentType.JSON)
            .body("""
                {
                    "itemName": "Bike For Interest Test",
                    "itemAvailability": "available",
                    "itemType": "Sports",
                    "itemDescription": "A bike",
                    "itemImageUrl": "http://example.com/bike.jpg"
                }
                """)
        .when()
            .post("/items")
        .then()
            .statusCode(200)
            .extract().asString();

        createdItemId = Integer.parseInt(itemId);
    }

    @Test
    @Order(2)
    public void testPostInterest() {
        String id = given()
            .header("Authorization", "Bearer " + recipientToken)
            .contentType(ContentType.JSON)
            .body("""
                {
                    "item": "%d",
                    "note": "I want this bike"
                }
                """.formatted(createdItemId))
        .when()
            .post("/interests")
        .then()
            .statusCode(200)
            .extract().asString();

        Assertions.assertNotNull(id);
    }

    @Test
    @Order(3)
    public void testPostInterestWithoutToken() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "item": "%d",
                    "note": "I want this bike"
                }
                """.formatted(createdItemId))
        .when()
            .post("/interests")
        .then()
            .statusCode(403);
    }

    @Test
    @Order(4)
    public void testGetAllInterests() {
        given()
            .header("Authorization", "Bearer " + recipientToken)
        .when()
            .get("/interests")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(5)
    public void testGetInterestsByItem() {
        given()
            .header("Authorization", "Bearer " + donorToken)
        .when()
            .get("/interests/item/" + createdItemId)
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(6)
    public void testGetInterestsByUser() {
        given()
            .header("Authorization", "Bearer " + recipientToken)
        .when()
            .get("/interests/user/2")
        .then()
            .statusCode(200);
    }

    @Test
    @Order(7)
    public void testGetInterestByIdNotFound() {
        given()
            .header("Authorization", "Bearer " + recipientToken)
        .when()
            .get("/interests/999999")
        .then()
            .statusCode(404);
    }
}
