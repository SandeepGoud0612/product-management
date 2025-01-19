package com.sandeep.recommendation_service;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public abstract class MongoDbTestBase {

	private static MongoDBContainer database = new MongoDBContainer("mongo:latest");

	static {
		database.start();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.host", database::getHost);
		registry.add("spring.data.mongodb.port", () -> database.getMappedPort(27017));
		registry.add("spring.data.mongodb.database", () -> "test");
		registry.add("spring.data.mongodb.auto-index-creation", () -> true);
	}

}
