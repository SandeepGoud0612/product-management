package com.sandeep.product_service.presistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class ProductEntity {

	@Id
	private String id;

	@Version
	private Integer version;

	@Indexed(unique = true)
	private int productId;

	private String name;

	private int weight;

	public ProductEntity(int productId, String name, int weight) {
		super();
		this.productId = productId;
		this.name = name;
		this.weight = weight;
	}

}