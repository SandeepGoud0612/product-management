package com.sandeep.api.product;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductClient {

	@PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Product createProduct(@RequestBody Product product);

	@GetMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	Product getProduct(@PathVariable("productId") int productId);

	@DeleteMapping(value = "/product/{productId}")
	void deleteProduct(@PathVariable("productId") int productId);

}
