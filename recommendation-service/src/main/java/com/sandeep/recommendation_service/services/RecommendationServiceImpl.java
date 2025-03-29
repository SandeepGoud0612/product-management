package com.sandeep.recommendation_service.services;

import static java.util.logging.Level.FINE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.sandeep.api.exceptions.InvalidInputException;
import com.sandeep.api.recommendation.Recommendation;
import com.sandeep.recommendation_service.mappers.RecommendationMapper;
import com.sandeep.recommendation_service.persistence.RecommendationEntity;
import com.sandeep.recommendation_service.persistence.RecommendationRepository;
import com.sandeep.util.ServiceUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

	private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

	private final RecommendationRepository repository;
	private final ServiceUtil serviceUtil;
	private final RecommendationMapper mapper;

	@Override
	public Mono<Recommendation> createRecommendation(Recommendation body) {
		if (body.getProductId() < 1) {
			throw new InvalidInputException("Invalid productId: " + body.getProductId());
		}

		LOG.info("Will create recommendations for product with id={}", body.getProductId());

		RecommendationEntity entity = mapper.apiToEntity(body);
		Mono<Recommendation> newEntity = repository.save(entity).log(LOG.getName(), FINE)
				.onErrorMap(DuplicateKeyException.class, ex -> new InvalidInputException("Duplicate key, Product Id: "
						+ body.getProductId() + ", Recommendation Id:" + body.getRecommendationId()))
				.map(e -> mapper.entityToApi(e));

		return newEntity;
	}

	@Override
	public Flux<Recommendation> getRecommendations(int productId) {
		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		LOG.info("Will get recommendations for product with id={}", productId);

		return repository.findByProductId(productId).log(LOG.getName(), FINE).map(e -> mapper.entityToApi(e))
				.map(e -> setServiceAddress(e));
	}

	@Override
	public Mono<Void> deleteRecommendations(int productId) {
		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		LOG.debug("Tries to delete recommendations for the product with productId: {}", productId);

		return repository.deleteAll(repository.findByProductId(productId));
	}

	private Recommendation setServiceAddress(Recommendation e) {
		e.setServiceAddress(serviceUtil.getServiceAddress());
		return e;
	}

}
