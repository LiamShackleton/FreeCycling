package freecycling.example.freecycling;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import freecycling.example.freecycling.Core.TransferSiteRepository;
import freecycling.example.freecycling.Core.UserRepository;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransferSiteControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferSiteRepository transferSiteRepository;

    private static String token;
    private static String testUsername = "e2e_site_" + System.currentTimeMillis();
    private static int createdSiteId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterAll
    public static void cleanUp(
            @Autowired UserRepository userRepository,
            @Autowired TransferSiteRepository transferSiteRepository) {
        transferSiteRepository.findAll().stream()
            .filter(s -> "E2E Test Library".equals(s.getTransferSiteName()))
            .forEach(transferSiteRepository::delete);
        userRepository.findAll().stream()
            .filter(u -> u.getUserName().startsWith("e2e_site_"))
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
                    "email": "e2esite@mail.com"
                }
                """.formatted(testUsername))
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().asString();
    }

    @Test
    @Order(2)
    public void testPostTransferSite() {
        String id = given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("""
                {
                    "transferSiteName": "E2E Test Library",
                    "transferAddress": "123 Test St"
                }
                """)
        .when()
            .post("/transfersites")
        .then()
            .statusCode(200)
            .extract().asString();

        createdSiteId = Integer.parseInt(id);
        Assertions.assertTrue(createdSiteId > 0);
    }

    @Test
    @Order(3)
    public void testPostTransferSiteWithoutToken() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "transferSiteName": "E2E Test Library",
                    "transferAddress": "123 Test St"
                }
                """)
        .when()
            .post("/transfersites")
        .then()
            .statusCode(403);
    }

    @Test
    @Order(4)
    public void testGetAllSites() {
        given()
        .when()
            .get("/transfersites")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(5)
    public void testGetSiteById() {
        given()
        .when()
            .get("/transfersites/" + createdSiteId)
        .then()
            .statusCode(200)
            .body("transferSiteName", equalTo("E2E Test Library"));
    }

    @Test
    @Order(6)
    public void testGetSiteByIdNotFound() {
        given()
        .when()
            .get("/transfersites/999999")
        .then()
            .statusCode(404);
    }

    @Test
    @Order(7)
    public void testGetSiteByName() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/transfersites/name/E2E Test Library")
        .then()
            .statusCode(200)
            .body("[0].transferSiteName", equalTo("E2E Test Library"));
    }

    @Test
    @Order(8)
    public void testGetSiteByNameNotFound() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/transfersites/name/NoSuchPlace")
        .then()
            .statusCode(404);
    }
}