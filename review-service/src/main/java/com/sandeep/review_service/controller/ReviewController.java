package com.sandeep.review_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sandeep.api.review.Review;
import com.sandeep.api.review.ReviewClient;
import com.sandeep.review_service.services.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewClient {

	private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

	private final ReviewService reviewService;

	@Override
	public Review createReview(Review review) {
		LOG.info("/review creates the review for productId={}", review.getProductId());
		return reviewService.createReview(review);
	}

	@Override
	public List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId) {
		LOG.info("/review returns the found reviews for productId={}", productId);
		return reviewService.getReviews(productId);
	}

	@Override
	public void deleteReviews(int productId) {
		LOG.info("/review deletes the reviews for productId={}", productId);
		reviewService.deleteReviews(productId);
	}

}
