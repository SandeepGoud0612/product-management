package com.sandeep.recommendation_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.sandeep.api.recommendation.Recommendation;
import com.sandeep.api.recommendation.RecommendationClient;
import com.sandeep.recommendation_service.services.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RecommendationController implements RecommendationClient {

	private static final Logger LOG = LoggerFactory.getLogger(RecommendationController.class);

	private final RecommendationService service;

	@Override
	public Recommendation createRecommendation(Recommendation recommendation) {
		LOG.info("/recommendation creates the recommendation for productId={}", recommendation.getProductId());
		return service.createRecommendation(recommendation);
	}

	@Override
	public List<Recommendation> getRecommendations(int productId) {
		LOG.info("/recommendation returns the found recommendations for productId={}", productId);
		return service.getRecommendations(productId);
	}

	@Override
	public void deleteRecommendations(int productId) {
		LOG.info("/recommendation deletes the recommendations for productId={}", productId);
		service.deleteRecommendations(productId);
	}

}
