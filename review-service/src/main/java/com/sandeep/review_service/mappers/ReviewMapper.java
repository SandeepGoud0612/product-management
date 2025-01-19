package com.sandeep.review_service.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.sandeep.api.review.Review;
import com.sandeep.review_service.persistence.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true) })
	ReviewEntity apiToEntity(Review api);

	@Mappings({ @Mapping(target = "serviceAddress", ignore = true) })
	Review entityToApi(ReviewEntity entity);

	List<Review> entityListToApiList(List<ReviewEntity> entityList);

	List<ReviewEntity> apiListToEntityList(List<Review> apiList);

}
