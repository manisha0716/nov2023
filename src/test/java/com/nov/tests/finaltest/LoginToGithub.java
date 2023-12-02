package com.nov.tests.finaltest;

import java.util.List;

import static org.hamcrest.Matchers.lessThan;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class LoginToGithub {
	private static String mytoken="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";
	RequestSpecification myspec = null;
	
	@BeforeTest
	public void setUp() {
		RestAssured.baseURI="https://api.github.com";
		myspec = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", "token "+ mytoken)
				.build();
	}
	
	
	@Test
	public void getSingleRepo() {
		Header header=new Header("token",mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.when()
		.get("/repos/manisha0716/sep2023");
		res.prettyPrint();
		
		JsonPath jsonObject = res.body().jsonPath();
		
		System.out.println("status code: " + res.getStatusCode());
		System.out.println("full name : " + jsonObject.get("owner.login") + "/" + jsonObject.get("name"));
		System.out.println("default branch: " + jsonObject.get("default_branch"));
		
	}
	
	
	@Test
	public void getSingleNonExistRepo() {
		Header header=new Header("token",mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.when()
		.get("/repos/manisha0716/dec2023");
		res.prettyPrint();
		
		JsonPath jsonObject = res.body().jsonPath();
		
		System.out.println("status code: " + res.getStatusCode());
		System.out.println("message: " +  jsonObject.get("message"));
		
	}
	
	
	@Test
	public void getAllRepo() {
		Header header=new Header("Authorization", "Bearer " + mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.when()
		.get("/user/repos");
		
		res.then()
		.contentType(ContentType.JSON);
		res.prettyPrint();
		
		JsonPath jsonObject = res.body().jsonPath();
		
		List<String> totRep	=jsonObject.get("findAll{it.id>0}.id");
		System.out.println("Total repositories: " + totRep.size());
		System.out.println("status code: " + res.getStatusCode());
		System.out.println("repo: " + jsonObject.get("name"));
		
		System.out.println("response header: " + res.contentType());
		List<String> visStatus = jsonObject.get("visibility");
		System.out.println("visibility: " + visStatus);
		
	}
	
	
	@Test
	public void createRepo() {
		Header header=new Header("Authorization", "Bearer " + mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.contentType(ContentType.JSON)
		.body("{\"name\":\"Hello-World1\",\"description\":\"First repo\",\"homepage\":\"https://github.com\",\"private\":\"false\"}")
		.when()
		.post("/user/repos");
		
		res.then()
		.statusCode(201);

		JsonPath jsonObject = res.body().jsonPath();
		System.out.println("status code: " + res.getStatusCode());
		System.out.println("name: " +  jsonObject.get("name"));
		System.out.println("login : " + jsonObject.get("owner.login"));
		System.out.println("type : " + jsonObject.get("owner.type"));
		
	}
	
	
	@Test
	public void createExistRepo() {
		Header header=new Header("Authorization", "Bearer " + mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.contentType(ContentType.JSON)
		.body("{\"name\":\"Hello-World\",\"description\":\"First repo\",\"homepage\":\"https://github.com\",\"private\":\"false\"}")
		.when()
		.post("/user/repos");
		
		res.then()
		.statusCode(422);

		JsonPath jsonObject = res.body().jsonPath();
		System.out.println("status code: " + res.getStatusCode());
		System.out.println("message: " +  jsonObject.get("message"));
		
	}
	
	@Test
	public void updateRepo() {
		Header header=new Header("Authorization", "Bearer " + mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.contentType(ContentType.JSON)
		.body("{\"name\":\"Hello-World2\",\"description\":\"my repository created using \",\"private\":false\"}")
		.when()
		.patch("/repos/manisha0716/Hello-World1");
		res.prettyPrint();
		
		JsonPath jsonObject = res.body().jsonPath();
		
		System.out.println("status code: " + res.getStatusCode());
		System.out.println("full name : " + jsonObject.get("owner.login") + "/" + jsonObject.get("name"));
		
	}
	
	
	@Test
	public void delRepo() {
		Header header=new Header("Authorization", "Bearer " + mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.when()
		.delete("/repos/manisha0716/Hello-World2");
		res.prettyPrint();
		
		res.then()
		.statusCode(204);
		
		System.out.println("status code: " + res.getStatusCode());
		
	}
	
	@Test
	public void nonexistdelRepo() {
		Header header=new Header("Authorization", "Bearer " + mytoken);
		Response res=RestAssured
		.given()
		.header(header)
		.when()
		.delete("/repos/manisha0716/Hello-World2");
		res.prettyPrint();
		
		res.then()
		.statusCode(404);
		
		JsonPath jsonObject = res.body().jsonPath();
		System.out.println("status code: " + res.getStatusCode());
		System.out.println("message: " +  jsonObject.get("message"));
		
	}
	
	
}
