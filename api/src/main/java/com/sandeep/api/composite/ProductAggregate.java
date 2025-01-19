package com.sandeep.api.composite;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregate {

	private int productId;
	private String name;
	private int weight;
	private List<RecommendationSummary> recommendations;
	private List<ReviewSummary> reviews;
	private ServiceAddresses serviceAddresses;

}
