package com.sandeep.api.recommendation;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface RecommendationClient {

	@PostMapping(value = "/recommendation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	Recommendation createRecommendation(@RequestBody Recommendation recommendation);

	@GetMapping(value = "/recommendation", produces = MediaType.APPLICATION_JSON_VALUE)
	List<Recommendation> getRecommendations(int productId);

	@DeleteMapping(value = "/recommendation")
	void deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);

}
