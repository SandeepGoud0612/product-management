package com.sandeep.product_service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sandeep.api.product.Product;
import com.sandeep.product_service.presistence.ProductRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductRepository repository;

	@BeforeEach
	void setupDb() {
		repository.deleteAll().block();
	}

	@Test
	void getProductById() {
		int productId = 1;
		
		assertNull(repository.findByProductId(productId).block());
		assertEquals(0, (long)repository.count().block());

		postAndVerifyProduct(productId, OK);

		assertNotNull(repository.findByProductId(productId).block());
		assertEquals(1, (long)repository.count().block());

		getAndVerifyProduct(productId, OK).jsonPath("$.productId").isEqualTo(productId);
	}

	@Test
	void duplicateError() {
		int productId = 1;
		
		assertNull(repository.findByProductId(productId).block());
		assertEquals(0, (long)repository.count().block());

		postAndVerifyProduct(productId, OK);
		
		assertNotNull(repository.findByProductId(productId).block());
		assertEquals(1, (long)repository.count().block());

		postAndVerifyProduct(productId, UNPROCESSABLE_ENTITY).jsonPath("$.path").isEqualTo("/product")
				.jsonPath("$.message").isEqualTo("Duplicate key, Product Id: " + productId);
		
		assertEquals(1, (long)repository.count().block());
	}

	@Test
	void deleteProduct() {
		int productId = 1;

		assertNull(repository.findByProductId(productId).block());
		assertEquals(0, (long)repository.count().block());

		postAndVerifyProduct(productId, OK);
		
		assertNotNull(repository.findByProductId(productId).block());
		assertEquals(1, (long)repository.count().block());

		deleteAndVerifyProduct(productId, OK);
		
		assertNull(repository.findByProductId(productId).block());
		assertEquals(0, (long)repository.count().block());		
	}

	@Test
	void getProductInvalidParameterString() {
		getAndVerifyProduct("/no-integer", BAD_REQUEST).jsonPath("$.path").isEqualTo("/product/no-integer")
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	void getProductNotFound() {
		int productIdNotFound = 13;
		getAndVerifyProduct(productIdNotFound, NOT_FOUND).jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
				.jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);
	}

	@Test
	void getProductInvalidParameterNegativeValue() {
		int productIdInvalid = -1;

		getAndVerifyProduct(productIdInvalid, UNPROCESSABLE_ENTITY).jsonPath("$.path")
				.isEqualTo("/product/" + productIdInvalid).jsonPath("$.message")
				.isEqualTo("Invalid productId: " + productIdInvalid);
	}

	private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		return getAndVerifyProduct("/" + productId, expectedStatus);
	}

	private WebTestClient.BodyContentSpec getAndVerifyProduct(String productIdPath, HttpStatus expectedStatus) {
		return client.get().uri("/product" + productIdPath).accept(APPLICATION_JSON).exchange().expectStatus()
				.isEqualTo(expectedStatus).expectHeader().contentType(APPLICATION_JSON).expectBody();
	}

	private WebTestClient.BodyContentSpec postAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		Product product = new Product(productId, "Name " + productId, productId, "SA");

		return client.post().uri("/product").body(just(product), Product.class).accept(APPLICATION_JSON).exchange()
				.expectStatus().isEqualTo(expectedStatus).expectHeader().contentType(APPLICATION_JSON).expectBody();
	}

	private WebTestClient.BodyContentSpec deleteAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		return client.delete().uri("/product/" + productId).accept(APPLICATION_JSON).exchange().expectStatus()
				.isEqualTo(expectedStatus).expectBody();
	}
}
