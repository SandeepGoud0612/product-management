package com.sandeep.product_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.sandeep.api.product.Product;
import com.sandeep.product_service.presistence.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true) })
	ProductEntity apiToEntity(Product api);

	@Mappings({ @Mapping(target = "serviceAddress", ignore = true) })
	Product entityToApi(ProductEntity entity);

}
