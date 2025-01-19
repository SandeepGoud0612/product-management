package com.sandeep.recommendation_service.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.sandeep.api.recommendation.Recommendation;
import com.sandeep.recommendation_service.persistence.RecommendationEntity;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true),
			@Mapping(target = "rating", source = "api.rate") })
	RecommendationEntity apiToEntity(Recommendation api);

	@Mappings({ @Mapping(target = "rate", source = "entity.rating"),
			@Mapping(target = "serviceAddress", ignore = true) })
	Recommendation entityToApi(RecommendationEntity entity);

	List<RecommendationEntity> apiListToEntityList(List<Recommendation> apiList);

	List<Recommendation> entityListToApiList(List<RecommendationEntity> entityList);

}
