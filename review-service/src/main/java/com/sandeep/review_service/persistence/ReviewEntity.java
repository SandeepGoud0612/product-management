package com.sandeep.review_service.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews", indexes = {
		@Index(name = "reviews_unique_idx", unique = true, columnList = "productId,reviewId") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

	@Id
	@GeneratedValue
	private int id;

	@Version
	private int version;

	private int productId;

	private int reviewId;

	private String author;

	private String subject;

	private String content;

	public ReviewEntity(int productId, int reviewId, String author, String subject, String content) {
		super();
		this.productId = productId;
		this.reviewId = reviewId;
		this.author = author;
		this.subject = subject;
		this.content = content;
	}

}
