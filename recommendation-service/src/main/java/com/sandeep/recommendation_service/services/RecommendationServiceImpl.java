package com.sandeep.recommendation_service.services;

import java.util.List;

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

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

	private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

	private final RecommendationRepository repository;
	private final ServiceUtil serviceUtil;
	private final RecommendationMapper mapper;

	@Override
	public Recommendation createRecommendation(Recommendation body) {
		try {
			RecommendationEntity entity = mapper.apiToEntity(body);
			RecommendationEntity newEntity = repository.save(entity);

			LOG.info("createRecommendation: created a recommendation entity: {}/{}", body.getProductId(),
					body.getRecommendationId());
			return mapper.entityToApi(newEntity);
		} catch (DuplicateKeyException dke) {
			throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Recommendation Id:"
					+ body.getRecommendationId());
		}
	}

	@Override
	public List<Recommendation> getRecommendations(int productId) {
		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		List<RecommendationEntity> entityList = repository.findByProductId(productId);
		List<Recommendation> apiList = mapper.entityListToApiList(entityList);
		apiList.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

		LOG.info("getRecommendations: response size: {}", apiList.size());

		return apiList;
	}

	@Override
	public void deleteRecommendations(int productId) {
		LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}",
				productId);
		repository.deleteAll(repository.findByProductId(productId));
	}

}
