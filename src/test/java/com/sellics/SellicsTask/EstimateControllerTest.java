package com.sellics.SellicsTask;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sellics.SellicsTask.model.Estimate;
import com.sellics.SellicsTask.service.EstimateService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc

@ComponentScan("com.sellics.SellicsTask.service")
public class EstimateControllerTest {


	@Autowired
	private MockMvc mvc;

	@Autowired
	EstimateService estimateService;

	@Test
	public void setKeyWordTest() throws Exception {
		String keyWord = "keyboard";
		Estimate estimate = estimateService.getEstimate(keyWord);
		float score = estimate.getScore();

		mvc.perform(get("/estimate").param("keyword", keyWord).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("keyWord").value("keyboard"))
				.andExpect(MockMvcResultMatchers.jsonPath("score").value(score));

	}

	@Test
	public void setInvalidKeyWordTest() throws Exception {
		String keyWord = " invalid keyword";

		NestedServletException thrown = assertThrows(NestedServletException.class, () -> mvc.perform(get("/estimate").param("keyword", keyWord)));

		assertTrue(thrown.getMessage().contains("Invalid input, key word must start with Alpha numeric character"));
				

	}

}
