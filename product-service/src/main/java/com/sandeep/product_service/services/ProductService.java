package com.sandeep.product_service.services;

import com.sandeep.api.product.Product;

public interface ProductService {

	Product getProduct(int productId);

	Product createProduct(Product product);

	void deleteProduct(int productId);

}
