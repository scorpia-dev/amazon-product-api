package com.amazon.amazon;

import com.amazon.amazon.model.Estimate;
import com.amazon.amazon.service.EstimateService;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ComponentScan("com.amazon.amazon.service")
public class EstimateServiceTest {

	@Autowired
	EstimateService estimateService;

	@Test
	public void getEstimateTest() throws JsonIOException, JsonSyntaxException, IOException, InterruptedException {

		String keyWord = "house";
		Estimate estimate = estimateService.getEstimate(keyWord);
		assertEquals("house", estimate.getKeyWord());
		assertTrue(estimate.getScore() > 0 && estimate.getScore() < 100);
	}

}
