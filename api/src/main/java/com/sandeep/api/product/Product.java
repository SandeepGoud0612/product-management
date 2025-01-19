package com.sandeep.api.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private int productId;
	private String name;
	private int weight;
	private String serviceAddress;

}
