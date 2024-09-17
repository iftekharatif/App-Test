package com.test.health;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;

public class HealthCheck {
	
	 	private static final String URL_TO_CHECK = "https://demo.filestash.app/files/";
	    private static final String LOG_FILE = "C:\\Users\\mahbo\\git\\repository\\AppAUT\\src\\test\\java\\com\\test\\sample\\application_health.log";
	    private static final int EXPECTED_STATUS_CODE = 200;

	
	  private static void checkApplicationHealth(String urlToCheck, int expectedStatusCode) {
	        try {
	            URL url = new URL(urlToCheck);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");

	            int responseCode = connection.getResponseCode();
	            String status = (responseCode == expectedStatusCode) ? "up" : "down";

	            String logMessage = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
	                                " - Application is " + status + ". Status Code: " + responseCode;
	            Files.write(Paths.get(LOG_FILE), logMessage.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

	        } catch (IOException e) {
	            String errorMessage = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
	                                  " - Application check failed. Error: " + e.getMessage();
	            try {
	                Files.write(Paths.get(LOG_FILE), errorMessage.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	            } catch (IOException ioException) {
	                ioException.printStackTrace();
	            }
	        }
	    }
	  
@Test	  
public void health_check_test() {
	checkApplicationHealth(URL_TO_CHECK, EXPECTED_STATUS_CODE);

}

}
