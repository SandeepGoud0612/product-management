package com.sandeep.api.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummary {

	private int reviewId;
	private String author;
	private String subject;
	private String content;

}
