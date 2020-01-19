package com.sellics.SellicsTask;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sellics.SellicsTask.model.Estimate;
import com.sellics.SellicsTask.service.EstimateService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ComponentScan("com.sellics.SellicsTask.service")
public class EstimateServiceTest {

	@Autowired
	EstimateService estimateService;

	@Test
	public void getEstimateTest() throws JsonIOException, JsonSyntaxException, IOException, InterruptedException {

		String keyWord = "house";
		Estimate estimate = estimateService.getEstimate(keyWord);
		assertThat(estimate).isNotNull();
		assertThat(estimate.getKeyWord().equals("house"));
		assertThat(estimate.getScore() >= 0 && estimate.getScore() <= 100);
	}

	@Test
	public void getEstimateInvalidKeyWordTest() throws JsonIOException, JsonSyntaxException, IOException, InterruptedException {

		String keyWord = " not valid first char in front of word";
		
		RuntimeException thrown = assertThrows(RuntimeException.class,
				() -> estimateService.getEstimate(keyWord));

		assertTrue(thrown.getMessage().contains("Invalid input, key word must start with Alpha numeric character"));
		
	}
	
	@Test
	public void overTenSecondsForResponseTest() throws JsonIOException, JsonSyntaxException, IOException, InterruptedException {

		String keyWord = "iphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphoneiphone";
		
		RuntimeException thrown = assertThrows(RuntimeException.class,
				() -> estimateService.getEstimate(keyWord));

		assertTrue(thrown.getMessage().contains("microservice only has an SLA of 10 seconds for a request round-trip​."));
		
	}
	
}
