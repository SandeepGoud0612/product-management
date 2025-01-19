package com.sandeep.api.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationSummary {

	private int recommendationId;
	private String author;
	private int rate;
	private String content;

}
