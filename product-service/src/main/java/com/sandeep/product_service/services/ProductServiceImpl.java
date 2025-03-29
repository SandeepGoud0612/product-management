package com.sandeep.product_service.services;

import static java.util.logging.Level.FINE;

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
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	private final ServiceUtil serviceUtil;
	private final ProductRepository repository;
	private final ProductMapper mapper;

	@Override
	public Mono<Product> createProduct(Product product) {
		if (product.getProductId() < 1) {
			throw new InvalidInputException("Invalid productId: " + product.getProductId());
		}
		
		LOG.info("Will create product for id={}", product.getProductId());

		ProductEntity entity = mapper.apiToEntity(product);
		Mono<Product> newEntity = repository.save(entity).log(LOG.getName(), FINE)
				.onErrorMap(DuplicateKeyException.class,
						ex -> new InvalidInputException("Duplicate key, Product Id: " + product.getProductId()))
				.map(e -> mapper.entityToApi(e));

		return newEntity;
	}

	@Override
	public Mono<Product> getProduct(int productId) {
		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		LOG.info("Will get product for id={}", productId);

		return repository.findByProductId(productId)
				.switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
				.log(LOG.getName(), FINE).map(e -> mapper.entityToApi(e)).map(e -> setServiceAddress(e));
	}

	@Override
	public Mono<Void> deleteProduct(int productId) {
		if (productId < 1) {
			throw new InvalidInputException("Invalid productId: " + productId);
		}

		LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
		
		return repository.findByProductId(productId).log(LOG.getName(), FINE).map(e -> repository.delete(e))
				.flatMap(e -> e);
	}

	private Product setServiceAddress(Product e) {
		e.setServiceAddress(serviceUtil.getServiceAddress());
		return e;
	}

}
