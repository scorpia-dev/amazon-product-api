package com.amazon.amazon;

import com.amazon.amazon.model.Estimate;
import com.amazon.amazon.service.EstimateService;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ComponentScan("com.amazon.amazon.service")
public class EstimateControllerTest {

    @Autowired
    EstimateService estimateService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void setKeyWordTest() throws Exception {
        String keyWord = "keyboard";
        Estimate estimate = estimateService.getEstimate(keyWord);
        float score = estimate.getScore();
        mvc.perform(get("/estimate/{product}", keyWord).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("product").value("keyboard"))
                .andExpect(MockMvcResultMatchers.jsonPath("score").value(score));
    }

    @Test
    public void setInvalidKeyWordTest() throws Exception {
        String keyWord = " invalid keyword";

        mvc.perform(get("/estimate/{product}", keyWord).accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andDo(print()).andExpect(content().string(containsString(
                "some parameters are invalid: Invalid input, key word must start with Alpha numeric character")));
    }

}
