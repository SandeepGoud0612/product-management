package com.sandeep.recommendation_service.services;

import com.sandeep.api.recommendation.Recommendation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationService {

	Mono<Recommendation> createRecommendation(Recommendation recommendation);

	Flux<Recommendation> getRecommendations(int productId);

	Mono<Void> deleteRecommendations(int productId);

}
