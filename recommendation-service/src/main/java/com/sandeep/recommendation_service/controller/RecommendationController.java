package com.sandeep.recommendation_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.sandeep.api.recommendation.Recommendation;
import com.sandeep.api.recommendation.RecommendationClient;
import com.sandeep.recommendation_service.services.RecommendationService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RecommendationController implements RecommendationClient {

	private static final Logger LOG = LoggerFactory.getLogger(RecommendationController.class);

	private final RecommendationService service;

	@Override
	public Mono<Recommendation> createRecommendation(Recommendation recommendation) {
		LOG.info("/recommendation creates the recommendation for productId={}", recommendation.getProductId());
		return service.createRecommendation(recommendation);
	}

	@Override
	public Flux<Recommendation> getRecommendations(int productId) {
		LOG.info("/recommendation returns the found recommendations for productId={}", productId);
		return service.getRecommendations(productId);
	}

	@Override
	public Mono<Void> deleteRecommendations(int productId) {
		LOG.info("/recommendation deletes the recommendations for productId={}", productId);
		return service.deleteRecommendations(productId);
	}

}
