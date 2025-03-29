package com.sandeep.recommendation_service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sandeep.api.recommendation.Recommendation;
import com.sandeep.recommendation_service.persistence.RecommendationRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecommendationServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private RecommendationRepository repository;

	@BeforeEach
	void setupDb() {
		repository.deleteAll().block();
	}

	@Test
	void getRecommendationsByProductId() {
		int productId = 1;

		assertEquals(Collections.EMPTY_LIST, repository.findByProductId(productId).collectList().block());
		assertEquals(0, (long) repository.count().block());

		postAndVerifyRecommendation(productId, 1, OK);
		postAndVerifyRecommendation(productId, 2, OK);
		postAndVerifyRecommendation(productId, 3, OK);

		assertNotNull(repository.findByProductId(productId).collectList().block());
		assertEquals(3, (long) repository.count().block());

		getAndVerifyRecommendationsByProductId(productId, OK).jsonPath("$.length()").isEqualTo(3)
				.jsonPath("$[2].productId").isEqualTo(productId).jsonPath("$[2].recommendationId").isEqualTo(3);
	}

	@Test
	void duplicateError() {
		int productId = 1;
		int recommendationId = 1;

		assertEquals(Collections.EMPTY_LIST, repository.findByProductId(productId).collectList().block());
		assertEquals(0, (long) repository.count().block());

		postAndVerifyRecommendation(productId, recommendationId, OK).jsonPath("$.productId").isEqualTo(productId)
				.jsonPath("$.recommendationId").isEqualTo(recommendationId);

		assertNotNull(repository.findByProductId(productId).collectList().block());
		assertEquals(1, (long) repository.count().block());

		postAndVerifyRecommendation(productId, recommendationId, UNPROCESSABLE_ENTITY).jsonPath("$.path")
				.isEqualTo("/recommendation").jsonPath("$.message")
				.isEqualTo("Duplicate key, Product Id: 1, Recommendation Id:1");

		assertEquals(1, (long) repository.count().block());
	}

	@Test
	void deleteRecommendations() {
		int productId = 1;
		int recommendationId = 1;

		assertEquals(Collections.EMPTY_LIST, repository.findByProductId(productId).collectList().block());
		assertEquals(0, (long) repository.count().block());

		postAndVerifyRecommendation(productId, recommendationId, OK);

		assertNotNull(repository.findByProductId(productId).collectList().block());
		assertEquals(1, (long) repository.count().block());

		deleteAndVerifyRecommendationsByProductId(productId, OK);

		assertEquals(Collections.EMPTY_LIST, repository.findByProductId(productId).collectList().block());
		assertEquals(0, (long) repository.count().block());

		deleteAndVerifyRecommendationsByProductId(productId, OK);

		assertEquals(Collections.EMPTY_LIST, repository.findByProductId(productId).collectList().block());
		assertEquals(0, (long) repository.count().block());
	}

	@Test
	void getRecommendationsInvalidParameter() {
		getAndVerifyRecommendationsByProductId("?productId=no-integer", BAD_REQUEST).jsonPath("$.path")
				.isEqualTo("/recommendation").jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	void getRecommendationsNotFound() {
		getAndVerifyRecommendationsByProductId("?productId=113", OK).jsonPath("$.length()").isEqualTo(0);
	}

	@Test
	void getRecommendationsInvalidParameterNegativeValue() {
		int productIdInvalid = -1;

		getAndVerifyRecommendationsByProductId("?productId=" + productIdInvalid, UNPROCESSABLE_ENTITY)
				.jsonPath("$.path").isEqualTo("/recommendation").jsonPath("$.message")
				.isEqualTo("Invalid productId: " + productIdInvalid);
	}

	private WebTestClient.BodyContentSpec getAndVerifyRecommendationsByProductId(int productId,
			HttpStatus expectedStatus) {
		return getAndVerifyRecommendationsByProductId("?productId=" + productId, expectedStatus);
	}

	private WebTestClient.BodyContentSpec getAndVerifyRecommendationsByProductId(String productIdQuery,
			HttpStatus expectedStatus) {
		return client.get().uri("/recommendation" + productIdQuery).accept(APPLICATION_JSON).exchange().expectStatus()
				.isEqualTo(expectedStatus).expectHeader().contentType(APPLICATION_JSON).expectBody();
	}

	private WebTestClient.BodyContentSpec postAndVerifyRecommendation(int productId, int recommendationId,
			HttpStatus expectedStatus) {
		Recommendation recommendation = new Recommendation(productId, recommendationId, "Author " + recommendationId,
				recommendationId, "Content " + recommendationId, "SA");
		return client.post().uri("/recommendation").body(just(recommendation), Recommendation.class)
				.accept(APPLICATION_JSON).exchange().expectStatus().isEqualTo(expectedStatus).expectHeader()
				.contentType(APPLICATION_JSON).expectBody();
	}

	private WebTestClient.BodyContentSpec deleteAndVerifyRecommendationsByProductId(int productId,
			HttpStatus expectedStatus) {
		return client.delete().uri("/recommendation?productId=" + productId).accept(APPLICATION_JSON).exchange()
				.expectStatus().isEqualTo(expectedStatus).expectBody();
	}

}
