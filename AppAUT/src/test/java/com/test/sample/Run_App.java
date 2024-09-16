package com.test.sample;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;

public class Run_App {

	@Test
	public void App_Test() {
		BrowserMobProxy proxy = new BrowserMobProxyServer();
		proxy.start(0); // 0 means the system will choose an available port
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.setProxy(seleniumProxy);
		proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
		proxy.newHar("App"); // Start a new HAR with the given name
		WebDriverManager.chromedriver().browserVersion("128.0").setup();
		WebDriver driver = new ChromeDriver(options);
		driver.get("http://localhost:8080/"); // Replace with your URL
		// Get the HAR data
		Har har = proxy.getHar();
		// Find a specific network request and verify the response
		HarEntry entry = har.getLog().getEntries().stream().filter(e -> e.getRequest().getUrl().contains("localhost")) // Adjust
																														// URL
																														// as
																														// needed
				.findFirst().orElse(null);
		if (entry != null) {
			System.out.println("Response Body: " + entry.getResponse().getContent().getText());
			Assert.assertEquals("304", entry.getResponse().getStatus());
			Assert.assertEquals("Hello, World!", entry.getResponse().getContent().getText());

		} else {
			System.out.println("No matching network entry found.");
		}
		proxy.stop();
		driver.quit();

	}

}
