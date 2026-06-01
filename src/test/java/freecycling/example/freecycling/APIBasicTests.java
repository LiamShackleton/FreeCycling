package freecycling.example.freecycling;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer; 

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import freecycling.example.freecycling.Interface.DTOs.ItemDTO;
import freecycling.example.freecycling.Interface.DTOs.InterestDTO;
import freecycling.example.freecycling.Interface.DTOs.LoginDTO;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.sql.Timestamp;

@SpringBootTest(classes=FreecyclingApplication.class,webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class APIBasicTests {
	private static String userOneToken;
	private static String userTwoToken;
	private static ItemDTO testItem;
	private static InterestDTO testInterest;
	
	@BeforeAll
	public static void setup() {
		RestAssured.port = 8080;
		RestAssured.baseURI = "http://localhost";
		
		// Create test users first (register them)
		given()
			.contentType("application/json")
			.body(createUser("TestUserOne", "password123"))
			.when().post("/users")
			.then()
			.statusCode(201);
		
		given()
			.contentType("application/json")
			.body(createUser("TestUserTwo", "password123"))
			.when().post("/users")
			.then()
			.statusCode(201);
	}
	
	private static freecycling.example.freecycling.Core.User createUser(String username, String password) {
		freecycling.example.freecycling.Core.User user = new freecycling.example.freecycling.Core.User();
		user.setUserName(username);
		user.setPass(password);
		user.setEmail(username + "@test.com");
		return user;
	}
	
	private static LoginDTO createLoginDTO(String username, String password) {
		LoginDTO login = new LoginDTO();
		login.setUserName(username);
		login.setPass(password);
		return login;
	}
	
	@Test
	@Order(1)
	public void testLogin() {
		userOneToken = given()
				.contentType("application/json")
				.body(createLoginDTO("TestUserOne", "password123"))
				.when().post("/users/login")
				.then()
				.statusCode(200)
				.extract().asString();
		
		userTwoToken = given()
				.contentType("application/json")
				.body(createLoginDTO("TestUserTwo", "password123"))
				.when().post("/users/login")
				.then()
				.statusCode(200)
				.extract().asString();
	}
	
	@Test
	@Order(2)
	public void testPostItem() {
		testItem = new ItemDTO();
		testItem.setItemName("Bicycle");
		testItem.setItemDonor("TestUserOne");
		testItem.setItemDescription("Gently used mountain bike");
		testItem.setItemType("Sports Equipment");
		testItem.setItemAvailability("available");
		
		given()
			.header("Authorization","Bearer "+userOneToken)
			.contentType("application/json")
			.body(testItem)
			.when().post("/items")
			.then()
			.statusCode(200);
	}
	
	@Test
	@Order(3)
	public void testGetItems() {
		given()
			.when().get("/items/getAvailable")
			.then()
			.statusCode(200);
		
		given()
			.when().get("/items")
			.then()
			.statusCode(200);
	}
	
	@Test
	@Order(4)
	public void testPostInterest() {
		// First get the item ID by finding our test item
		String itemId = given()
			.when().get("/items/getAvailable")
			.then()
			.statusCode(200)
			.extract()
			.path("[0].itemId");
		
		testInterest = new InterestDTO();
		testInterest.setUser("TestUserTwo");
		testInterest.setItem(itemId);
		testInterest.setNote("I'm interested in this bicycle!");
		testInterest.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		
		given()
			.header("Authorization","Bearer "+userTwoToken)
			.contentType("application/json")
			.body(testInterest)
			.when().post("/interests")
			.then()
			.statusCode(200);
	}
	
	@Test
	@Order(5)
	public void testGetInterests() {
		given()
			.when().get("/interests")
			.then()
			.statusCode(200);
	}
}