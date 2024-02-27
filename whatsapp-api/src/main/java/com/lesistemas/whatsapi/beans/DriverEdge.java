package com.lesistemas.whatsapi.beans;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriverEdge {

	@Bean
    public WebDriver webDriver() {
		System.out.println("Instanciando o Seleniun webdriver");

		var webDriver = new ChromeDriver();
		webDriver.get("https://web.whatsapp.com/");
		return webDriver;
	}

}