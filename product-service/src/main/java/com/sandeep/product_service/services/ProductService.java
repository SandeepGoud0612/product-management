package com.sandeep.product_service.services;

import com.sandeep.api.product.Product;

import reactor.core.publisher.Mono;

public interface ProductService {

	Mono<Product> getProduct(int productId);

	Mono<Product> createProduct(Product product);

	Mono<Void> deleteProduct(int productId);

}
