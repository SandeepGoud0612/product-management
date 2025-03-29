package com.sandeep.api.recommendation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationClient {

	@PostMapping(value = "/recommendation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	Mono<Recommendation> createRecommendation(@RequestBody Recommendation recommendation);

	@GetMapping(value = "/recommendation", produces = MediaType.APPLICATION_JSON_VALUE)
	Flux<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId);

	@DeleteMapping(value = "/recommendation")
	Mono<Void> deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);

}
