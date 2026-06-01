package freecycling.example.freecycling;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import freecycling.example.freecycling.Core.ItemRepository;
import freecycling.example.freecycling.Core.MeetingTimeTableRepository;
import freecycling.example.freecycling.Core.UserRepository;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MeetingTimeTableControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MeetingTimeTableRepository meetingTimeTableRepository;

    private static String token;
    private static String testUsername = "e2e_meeting_" + System.currentTimeMillis();
    private static int createdMeetingId;
    private static int createdItemId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterAll
    public static void cleanUp(
            @Autowired UserRepository userRepository,
            @Autowired ItemRepository itemRepository,
            @Autowired MeetingTimeTableRepository meetingTimeTableRepository) {
        meetingTimeTableRepository.findAll().stream()
            .filter(m -> "E2E Test Library".equals(m.getMeetingSite()))
            .forEach(meetingTimeTableRepository::delete);
        itemRepository.findAll().stream()
            .filter(i -> "Meeting Test Bike".equals(i.getItemName()))
            .forEach(itemRepository::delete);
        userRepository.findAll().stream()
            .filter(u -> u.getUserName().startsWith("e2e_meeting_"))
            .forEach(userRepository::delete);
    }

    @Test
    @Order(1)
    public void testSetup() {
        token = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "userName": "%s",
                    "pass": "Password123",
                    "email": "e2emeeting@mail.com"
                }
                """.formatted(testUsername))
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().asString();

        String itemId = given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("""
                {
                    "itemName": "Meeting Test Bike",
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
    public void testPostMeeting() {
        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("""
                {
                    "meetingSite": "E2E Test Library",
                    "meetingTime": "2026-06-01 10:00:00"
                }
                """)
        .when()
            .post("/meetings")
        .then()
            .statusCode(200);
    }

    @Test
    @Order(3)
    public void testPostMeetingWithoutToken() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "meetingSite": "E2E Test Library",
                    "meetingTime": "2026-06-01 10:00:00"
                }
                """)
        .when()
            .post("/meetings")
        .then()
            .statusCode(403);
    }

    @Test
    @Order(4)
    public void testGetAvailableMeetings() {
        given()
        .when()
            .get("/meetings/available?site=E2E Test Library&date=2026-06-01")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(5)
    public void testGetAllMeetings() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/meetings")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(6)
    public void testGetMeetingsBySite() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/meetings/site/E2E Test Library")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(7)
    public void testGetMeetingsByDate() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/meetings/date/2026-06-01")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(8)
    public void testGetMeetingByIdNotFound() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/meetings/999999")
        .then()
            .statusCode(404);
    }

    @Test
    @Order(9)
    public void testBookMeeting() {
        // First get the meeting id we created
        createdMeetingId = given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/meetings/site/E2E Test Library")
        .then()
            .statusCode(200)
            .extract().jsonPath().getInt("[0].idMeetingTimeTable");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .post("/meetings/book/" + createdMeetingId + "?itemId=" + createdItemId)
        .then()
            .statusCode(200);
    }

    @Test
    @Order(10)
    public void testBookAlreadyBookedMeeting() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .post("/meetings/book/" + createdMeetingId + "?itemId=" + createdItemId)
        .then()
            .statusCode(409);
    }

    @Test
    @Order(11)
    public void testUpdateMeetingState() {
        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.TEXT)
            .body("Completed")
        .when()
            .patch("/meetings/state/" + createdMeetingId)
        .then()
            .statusCode(200);
    }
}