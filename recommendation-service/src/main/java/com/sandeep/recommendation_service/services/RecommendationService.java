package com.sandeep.recommendation_service.services;

import java.util.List;

import com.sandeep.api.recommendation.Recommendation;

public interface RecommendationService {

	Recommendation createRecommendation(Recommendation recommendation);

	List<Recommendation> getRecommendations(int productId);

	void deleteRecommendations(int productId);

}
