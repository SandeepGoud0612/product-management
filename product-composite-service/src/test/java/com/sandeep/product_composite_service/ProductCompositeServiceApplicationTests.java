package com.sandeep.product_composite_service;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sandeep.api.composite.ProductAggregate;
import com.sandeep.api.composite.RecommendationSummary;
import com.sandeep.api.composite.ReviewSummary;
import com.sandeep.api.exceptions.InvalidInputException;
import com.sandeep.api.exceptions.NotFoundException;
import com.sandeep.api.product.Product;
import com.sandeep.api.recommendation.Recommendation;
import com.sandeep.api.review.Review;
import com.sandeep.product_composite_service.controller.ProductCompositeIntegration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductCompositeServiceApplicationTests {

	private static final int PRODUCT_ID_OK = 1;
	private static final int PRODUCT_ID_NOT_FOUND = 2;
	private static final int PRODUCT_ID_INVALID = 3;

	@Autowired
	private WebTestClient client;

	@MockitoBean
	private ProductCompositeIntegration compositeIntegration;

	@BeforeEach
	void setup() {
		when(compositeIntegration.getProduct(PRODUCT_ID_OK))
				.thenReturn(new Product(PRODUCT_ID_OK, "name", 1, "mock-address"));
		when(compositeIntegration.getRecommendations(PRODUCT_ID_OK)).thenReturn(
				singletonList(new Recommendation(PRODUCT_ID_OK, 1, "author", 1, "content", "mock address")));
		when(compositeIntegration.getReviews(PRODUCT_ID_OK)).thenReturn(
				singletonList(new Review(PRODUCT_ID_OK, 1, "author", "subject", "content", "mock address")));

		when(compositeIntegration.getProduct(PRODUCT_ID_NOT_FOUND))
				.thenThrow(new NotFoundException("NOT FOUND: " + PRODUCT_ID_NOT_FOUND));

		when(compositeIntegration.getProduct(PRODUCT_ID_INVALID))
				.thenThrow(new InvalidInputException("INVALID: " + PRODUCT_ID_INVALID));
	}

	@Test
	void createCompositeProduct1() {

		ProductAggregate compositeProduct = new ProductAggregate(1, "name", 1, null, null, null);

		postAndVerifyProduct(compositeProduct, OK);
	}

	//@Test
	void createCompositeProduct2() {
		ProductAggregate compositeProduct = new ProductAggregate(1, "name", 1,
				singletonList(new RecommendationSummary(1, "a", 1, "c")),
				singletonList(new ReviewSummary(1, "a", "s", "c")), null);

		postAndVerifyProduct(compositeProduct, OK);
	}

	//@Test
	void getProductById() {
		client.get().uri("/product-composite/" + PRODUCT_ID_OK).accept(APPLICATION_JSON).exchange().expectStatus()
				.isOk().expectHeader().contentType(APPLICATION_JSON).expectBody().jsonPath("$.productId")
				.isEqualTo(PRODUCT_ID_OK).jsonPath("$.recommendations.length()").isEqualTo(1)
				.jsonPath("$.reviews.length()").isEqualTo(1);
	}

	//@Test
	void getProductNotFound() {
		client.get().uri("/product-composite/" + PRODUCT_ID_NOT_FOUND).accept(APPLICATION_JSON).exchange()
				.expectStatus().isNotFound().expectHeader().contentType(APPLICATION_JSON).expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND).jsonPath("$.message")
				.isEqualTo("NOT FOUND: " + PRODUCT_ID_NOT_FOUND);
	}

	//@Test
	void getProductInvalidInput() {
		client.get().uri("/product-composite/" + PRODUCT_ID_INVALID).accept(APPLICATION_JSON).exchange().expectStatus()
				.isEqualTo(UNPROCESSABLE_ENTITY).expectHeader().contentType(APPLICATION_JSON).expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_INVALID).jsonPath("$.message")
				.isEqualTo("INVALID: " + PRODUCT_ID_INVALID);
	}

	//@Test
	void deleteCompositeProduct() {
		ProductAggregate compositeProduct = new ProductAggregate(1, "name", 1,
				singletonList(new RecommendationSummary(1, "a", 1, "c")),
				singletonList(new ReviewSummary(1, "a", "s", "c")), null);

		postAndVerifyProduct(compositeProduct, OK);

		deleteAndVerifyProduct(compositeProduct.getProductId(), OK);
		deleteAndVerifyProduct(compositeProduct.getProductId(), OK);
	}

	private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		return client.get().uri("/product-composite/" + productId).accept(APPLICATION_JSON).exchange().expectStatus()
				.isEqualTo(expectedStatus).expectHeader().contentType(APPLICATION_JSON).expectBody();
	}

	private void postAndVerifyProduct(ProductAggregate compositeProduct, HttpStatus expectedStatus) {
		client.post().uri("/product-composite").body(just(compositeProduct), ProductAggregate.class).exchange()
				.expectStatus().isEqualTo(expectedStatus);
	}

	private void deleteAndVerifyProduct(int productId, HttpStatus expectedStatus) {
		client.delete().uri("/product-composite/" + productId).exchange().expectStatus().isEqualTo(expectedStatus);
	}

}
