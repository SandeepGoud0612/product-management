package com.sandeep.product_service.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.sandeep.api.product.Product;
import com.sandeep.api.product.ProductClient;
import com.sandeep.product_service.services.ProductService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductClient {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;

	@Override
	public Mono<Product> createProduct(Product product) {
		LOG.info("/product creates the product for productId={}", product.getProductId());
		return productService.createProduct(product);
	}

	@Override
	public Mono<Product> getProduct(int productId) {
		LOG.info("/product returns the found product for productId={}", productId);
		return productService.getProduct(productId);
	}

	@Override
	public Mono<Void> deleteProduct(int productId) {
		LOG.info("/product deletes the product for productId={}", productId);
		return productService.deleteProduct(productId);
	}

}
