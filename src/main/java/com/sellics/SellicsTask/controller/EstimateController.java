package com.sellics.SellicsTask.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sellics.SellicsTask.model.Estimate;
import com.sellics.SellicsTask.service.EstimateService;

@Controller
public class EstimateController {

	@Autowired
	EstimateService estimateService;

	@RequestMapping(value = "estimate", method = RequestMethod.GET)
	public @ResponseBody Estimate setKeyWord(@RequestParam("keyword") String keyword)
			throws JsonIOException, JsonSyntaxException, IOException, InterruptedException {

		return estimateService.getEstimate(keyword);
	}

}
