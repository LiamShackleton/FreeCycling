package freecycling.example.freecycling;

import com.fasterxml.jackson.databind.ObjectMapper;
import freecycling.example.freecycling.Interface.DTOs.InterestDTO;
import freecycling.example.freecycling.Interface.DTOs.ItemDTO;
import freecycling.example.freecycling.security.JwtService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end tests for the FreeCycling item donation flow.
 * 
 * Tests:
 * 1. Donor posts an item to be donated
 * 2. Item shows up in the list of items being offered
 * 3. Both potential recipients express interest in the item
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EndToEndItemFlowTest {

    @Autowired
    private JwtService jwtService;

    @LocalServerPort
    private int port;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    /**
     * Test: Donor posts an item to be donated.
     */
    @Test
    void testDonorPostsItem() {
        // Create a new item DTO
        ItemDTO newItem = new ItemDTO();
        newItem.setItemName("Bicycle - Road Bike");
        newItem.setItemDonor("1");
        newItem.setItemRecipient("");
        newItem.setItemImageUrl("http://example.com/bike.jpg");
        newItem.setItemAvailability("available");
        newItem.setItemType("Vehicle");
        newItem.setItemDescription("Gently used road bike, size medium");

        // Send POST request to create item
        String response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("1"))
                .body(newItem)
                .when()
                .post("/items")
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(response).isNotEmpty();
    }

    /**
     * Test: Item shows up in the list of items being offered.
     */
    @Test
    void testItemShowsUpInAvailableList() {
        // First, create an item
        ItemDTO newItem = new ItemDTO();
        newItem.setItemName("Laptop - Dell XPS");
        newItem.setItemDonor("1");
        newItem.setItemRecipient("");
        newItem.setItemImageUrl("http://example.com/laptop.jpg");
        newItem.setItemAvailability("available");
        newItem.setItemType("Electronics");
        newItem.setItemDescription("Dell XPS laptop, i7 processor");

        // Create the item
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("1"))
                .body(newItem)
                .when()
                .post("/items")
                .then()
                .statusCode(200);

        // Get all available items and verify our item is there
        String availableItems = given()
                .when()
                .get("/items/getAvailable")
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(availableItems).contains("Laptop - Dell XPS");
    }

    /**
     * Test: Both potential recipients express interest in the item.
     */
    @Test
    void testBothRecipientsExpressInterest() {
        // Step 1: Create an item
        ItemDTO newItem = new ItemDTO();
        newItem.setItemName("Furniture - Bookshelf");
        newItem.setItemDonor("1");
        newItem.setItemRecipient("");
        newItem.setItemImageUrl("http://example.com/bookshelf.jpg");
        newItem.setItemAvailability("available");
        newItem.setItemType("Furniture");
        newItem.setItemDescription("Wooden bookshelf with 5 shelves");

        String createResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("1"))
                .body(newItem)
                .when()
                .post("/items")
                .then()
                .statusCode(200)
                .extract().asString();

        // Extract the item ID from the response
        String itemId = createResponse.trim();
        assertThat(itemId).isNotEmpty();

        // Step 2: First recipient expresses interest
        InterestDTO firstInterest = new InterestDTO();
        firstInterest.setUser("2");
        firstInterest.setItem(itemId);
        firstInterest.setTimeStamp(Timestamp.from(Instant.now()));
        firstInterest.setNote("I would like to take this bookshelf");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("2"))
                .body(firstInterest)
                .when()
                .post("/interests")
                .then()
                .statusCode(200);

        // Step 3: Second recipient expresses interest
        InterestDTO secondInterest = new InterestDTO();
        secondInterest.setUser("3");
        secondInterest.setItem(itemId);
        secondInterest.setTimeStamp(Timestamp.from(Instant.now()));
        secondInterest.setNote("I'm also interested in this bookshelf");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("3"))
                .body(secondInterest)
                .when()
                .post("/interests")
                .then()
                .statusCode(200);

        // Step 4: Verify both interests were recorded by checking the item's interest list
        String itemInterests = given()
                .when()
                .get("/interests/item/" + itemId)
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(itemInterests).contains("2");
        assertThat(itemInterests).contains("3");
    }

    /**
     * Test: Complete flow - Donor posts item, it appears in list, and multiple users show interest.
     */
    @Test
    void testCompleteItemDonationFlow() {
        // Phase 1: Donor posts an item
        ItemDTO newItem = new ItemDTO();
        newItem.setItemName("Toaster Oven");
        newItem.setItemDonor("1");
        newItem.setItemRecipient("");
        newItem.setItemImageUrl("http://example.com/toaster.jpg");
        newItem.setItemAvailability("available");
        newItem.setItemType("Appliance");
        newItem.setItemDescription("Convection toaster oven, like new");

        String createResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("1"))
                .body(newItem)
                .when()
                .post("/items")
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(createResponse).isNotEmpty();
        String itemId = createResponse.trim();

        // Phase 2: Item shows up in available list
        String availableItemsResponse = given()
                .when()
                .get("/items/getAvailable")
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(availableItemsResponse).contains("Toaster Oven");
        assertThat(availableItemsResponse).contains("Appliance");

        // Phase 3: First recipient expresses interest
        InterestDTO firstInterest = new InterestDTO();
        firstInterest.setUser("2");
        firstInterest.setItem(itemId);
        firstInterest.setTimeStamp(Timestamp.from(Instant.now()));
        firstInterest.setNote("I need this for my apartment");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("2"))
                .body(firstInterest)
                .when()
                .post("/interests")
                .then()
                .statusCode(200);

        // Phase 4: Second recipient expresses interest
        InterestDTO secondInterest = new InterestDTO();
        secondInterest.setUser("3");
        secondInterest.setItem(itemId);
        secondInterest.setTimeStamp(Timestamp.from(Instant.now()));
        secondInterest.setNote("I'm also interested");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtService.makeJwt("3"))
                .body(secondInterest)
                .when()
                .post("/interests")
                .then()
                .statusCode(200);

        // Phase 5: Verify both interests are recorded
        String itemInterests = given()
                .when()
                .get("/interests/item/" + itemId)
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(itemInterests).contains("2");
        assertThat(itemInterests).contains("3");

        // Phase 6: Verify the item still appears in available list
        String updatedAvailableItems = given()
                .when()
                .get("/items/getAvailable")
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(updatedAvailableItems).contains("Toaster Oven");
    }
}