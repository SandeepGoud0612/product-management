package com.sandeep.recommendation_service.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface RecommendationRepository extends ReactiveCrudRepository<RecommendationEntity, String> {

	Flux<RecommendationEntity> findByProductId(int productId);

}
