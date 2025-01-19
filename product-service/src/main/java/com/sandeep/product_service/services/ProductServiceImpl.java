package com.sandeep.product_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.sandeep.api.exceptions.InvalidInputException;
import com.sandeep.api.exceptions.NotFoundException;
import com.sandeep.api.product.Product;
import com.sandeep.product_service.mappers.ProductMapper;
import com.sandeep.product_service.presistence.ProductEntity;
import com.sandeep.product_service.presistence.ProductRepository;
import com.sandeep.util.ServiceUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	private final ServiceUtil serviceUtil;
	private final ProductRepository repository;
	private final ProductMapper mapper;

	@Override
	public Product createProduct(Product product) {
		try {
			ProductEntity entity = mapper.apiToEntity(product);
			ProductEntity newEntity = repository.save(entity);

			LOG.info("createProduct: entity created for productId: {}", product.getProductId());
			return mapper.entityToApi(newEntity);
		} catch (DuplicateKeyException dke) {
			throw new InvalidInputException("Duplicate key, Product Id: " + product.getProductId());
		}
	}

	@Override
	public Product getProduct(int productId) {
		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		ProductEntity entity = repository.findByProductId(productId)
				.orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

		Product api = mapper.entityToApi(entity);
		api.setServiceAddress(serviceUtil.getServiceAddress());

		LOG.info("getProduct: found productId: {}", api.getProductId());

		return api;
	}

	@Override
	public void deleteProduct(int productId) {
		LOG.debug("deleteProduct: deletes an product with productId: {}", productId);
		repository.findByProductId(productId).ifPresent(e -> repository.delete(e));
	}

}
