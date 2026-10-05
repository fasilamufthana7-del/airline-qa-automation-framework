package com.fasila.automation.ai;

import com.fasila.automation.utils.ConfigReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is the "AI-assisted" part of the framework.
 *
 * It takes a plain-English requirement (the kind a Business Analyst or
 * Product Owner would actually write) and asks Google's Gemini API to turn
 * it into a ready-to-use Cucumber Gherkin feature file. This is the
 * before/after demo worth screenshotting for LinkedIn: a paragraph of
 * requirements text in, a structured .feature file out.
 *
 * Why Gemini: it has a genuinely free API tier (no card required), unlike
 * most LLM APIs which only give a small starter credit. Swapping providers
 * later only means changing this one class — the rest of the framework
 * (Selenium, Cucumber, REST Assured) doesn't know or care which AI
 * provider generated the feature file.
 *
 * How it fits with the rest of the framework: this class doesn't touch
 * Selenium at all. It only talks to the Gemini API over HTTP and writes a
 * .feature file to disk. Once that file exists, it behaves exactly like
 * FlightBooking.feature — you'd still need to write step definitions for
 * any new step wording it introduces (AI can't write your Selenium
 * interactions for you, since it doesn't know your Page Object methods).
 */
public class AITestCaseGenerator {

    private static final String MODEL = "gemini-3.5-flash";
    private static final String API_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL + ":generateContent";

    public static String generateGherkinFromRequirement(String requirement) throws IOException, InterruptedException {
        String apiKey = ConfigReader.get("gemini.api.key");
        if (apiKey == null || apiKey.isBlank() || apiKey.startsWith("YOUR_")) {
            throw new IllegalStateException(
                "No Gemini API key found. Add gemini.api.key=AIza... to src/test/resources/config.properties"
            );
        }

        String prompt = "You are a QA engineer. Convert the following requirement into a Cucumber "
            + "Gherkin feature file. Output ONLY valid Gherkin syntax, starting with 'Feature:'. "
            + "No markdown code fences, no explanation before or after. "
            + "Include at least one positive scenario and one negative/edge-case scenario.\n\n"
            + "Requirement:\n" + requirement;

        // Gemini's request shape: a list of "contents", each with "parts" of text.
        // This is different from Claude/OpenAI's "messages" array — worth noting
        // if you ever compare providers side by side.
        JSONObject part = new JSONObject().put("text", prompt);
        JSONObject content = new JSONObject().put("parts", new JSONArray().put(part));
        JSONObject requestBody = new JSONObject().put("contents", new JSONArray().put(content));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL + "?key=" + apiKey))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Gemini API call failed (HTTP " + response.statusCode() + "): " + response.body());
        }

        JSONObject responseJson = new JSONObject(response.body());
        JSONArray candidates = responseJson.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONArray parts = firstCandidate.getJSONObject("content").getJSONArray("parts");
        return parts.getJSONObject(0).getString("text").trim();
    }

    public static Path saveAsFeatureFile(String gherkinText, String fileName) throws IOException {
        Path outputDir = Paths.get("src/test/resources/features/generated");
        Files.createDirectories(outputDir);
        Path outputFile = outputDir.resolve(fileName);
        Files.writeString(outputFile, gherkinText);
        return outputFile;
    }

    /**
     * Run this class directly (right-click -> Run As -> Java Application)
     * to see the AI layer in action. Edit the `requirement` text below to
     * try your own requirements.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        String requirement =
            "As a BlazeDemo customer, I want to book a flight by selecting a departure city and a "
            + "destination city, choosing one of the returned flight options, and entering my "
            + "passenger and payment details, so that I can complete a purchase. The system should "
            + "also handle the case where the departure and destination cities are the same, which "
            + "should not be allowed.";

        System.out.println("Sending requirement to Gemini...\n");
        String gherkin = generateGherkinFromRequirement(requirement);

        System.out.println("----- AI-Generated Gherkin -----");
        System.out.println(gherkin);
        System.out.println("---------------------------------\n");

        Path savedTo = saveAsFeatureFile(gherkin, "AIGeneratedFlightBooking.feature");
        System.out.println("Saved to: " + savedTo.toAbsolutePath());
    }
}
