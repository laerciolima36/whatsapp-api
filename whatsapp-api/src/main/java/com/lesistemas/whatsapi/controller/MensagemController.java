package com.lesistemas.whatsapi.controller;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lesistemas.whatsapi.model.Mensagem;

@RestController
public class MensagemController {

	@Autowired
	private WebDriver webDriver;

	@PostMapping("/sendbyname")
	public void receberMensagem(@RequestBody Mensagem mensagem) {
		for (String contato : mensagem.getContatos()) {
			try {
				var elementoContato = findContato(contato);
				elementoContato.click();

				var caixaMensagem = findCaixaTexto();
				caixaMensagem.sendKeys(mensagem.getConteudo());
				caixaMensagem.sendKeys(Keys.ENTER);
			} catch (Exception e) {
				System.out
						.println("Não foi possível enviar a mensagem para o contato " + contato + " - " + e.toString());
			}
		}
	}

	private WebElement findContato(String nomeContato) {
		var xPathContato = "//*[@id=\"pane-side\"]/*//span[@title='" + nomeContato + "']";
		return webDriver.findElement(By.xpath(xPathContato));
	}

	private WebElement findCaixaTexto() {
		var xPathCaixaTexto = "//*[@id=\"main\"]/footer//*[@role='textbox']";
		return webDriver.findElement(By.xpath(xPathCaixaTexto));
	}

	@PostMapping("/sendbycontato")
	public ResponseEntity<String> mensagemDireta(@RequestBody Mensagem mensagem) {
		try {
			webDriver.get("https://web.whatsapp.com/send/?phone=" + mensagem.getContato() + "&text="
					+ mensagem.getConteudo());
			
			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(120));
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"main\"]/footer//*[@role='textbox']")));
					
			var caixaMensagem = findCaixaTexto();
			caixaMensagem.sendKeys(Keys.ENTER);
			
			return new ResponseEntity<>("Mensagem Enviada com Sucesso", HttpStatus.OK);

		} catch (Exception e) {
			System.out.println("Não foi possível enviar a mensagem para o contato " + mensagem.getContato() + " - " + e.toString());
			return new ResponseEntity<>("Falha no envio da mensagem para " + mensagem.getContato() + " - " + e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}