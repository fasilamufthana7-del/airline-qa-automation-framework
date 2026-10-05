package com.fasila.automation.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * API-only tests — no browser, no Selenium. These send HTTP requests
 * straight to OpenSky Network's public REST API and check the response.
 *
 * Why this matters for your portfolio: real airline QA work is heavily
 * API-driven (PSS/GDS systems talk to each other via APIs long before any
 * UI is involved), so an API testing layer next to your UI layer in the
 * same framework is a realistic, relevant addition — not just "extra AI
 * stuff bolted on."
 *
 * OpenSky's API needs no authentication for this basic usage, and returns
 * real, live flight state data — so these assertions are against real data
 * that changes every time you run the test, not a canned demo response.
 */
public class OpenSkyApiTest {

    private static final String BASE_URL = "https://opensky-network.org/api";

    @Test
    public void shouldReturnLiveFlightStatesWithValidSchema() {
        Response response = given()
                .baseUri(BASE_URL)
            .when()
                .get("/states/all")
            .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 200,
            "Expected a 200 OK from the OpenSky API.");

        Assert.assertTrue(
            response.getContentType().contains("application/json"),
            "Expected a JSON response, but got: " + response.getContentType()
        );

        // "time" is always present — a Unix timestamp of when this snapshot was taken
        Assert.assertTrue(response.jsonPath().get("time") != null,
            "Expected a 'time' field in the response.");

        // "states" can occasionally be null if OpenSky has no data at that
        // instant — when present, each state is a fixed 17-element array
        // (icao24, callsign, origin_country, longitude, latitude, etc.)
        java.util.List<?> states = response.jsonPath().getList("states");
        if (states != null && !states.isEmpty()) {
            java.util.List<?> firstState = (java.util.List<?>) states.get(0);
            Assert.assertEquals(firstState.size(), 17,
                "Expected each flight state to have 17 fields per OpenSky's documented schema.");
        }
    }

    @Test
    public void shouldReturnFlightsNearAbuDhabi() {
        // Bounding box roughly around Abu Dhabi International Airport (AUH)
        Response response = given()
                .baseUri(BASE_URL)
                .queryParam("lamin", 24.0)
                .queryParam("lomin", 54.0)
                .queryParam("lamax", 25.2)
                .queryParam("lomax", 55.2)
            .when()
                .get("/states/all")
            .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 200,
            "Expected a 200 OK when querying flights near Abu Dhabi.");

        // We don't assert there ARE flights (air traffic varies second to
        // second) — just that the API responds correctly to a bounded,
        // domain-relevant query. This mirrors how you'd validate a
        // "flights near hub airport" feature in a real PSS/GDS integration.
        Assert.assertNotNull(response.jsonPath().get("time"),
            "Expected a valid response with a 'time' field.");
    }
}
