package com.sandeep.api.review;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewClient {

	@PostMapping(value = "/review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Review createReview(@RequestBody Review review);

	@GetMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
	List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

	@DeleteMapping(value = "/review")
	void deleteReviews(@RequestParam(value = "productId", required = true) int productId);

}
