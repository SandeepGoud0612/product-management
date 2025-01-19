package com.sandeep.review_service.services;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.sandeep.api.review.Review;

public interface ReviewService {

	Review createReview(Review review);

	List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

	void deleteReviews(int productId);
}
