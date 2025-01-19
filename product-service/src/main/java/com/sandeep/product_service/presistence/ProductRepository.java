package com.sandeep.product_service.presistence;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
		extends CrudRepository<ProductEntity, String>, PagingAndSortingRepository<ProductEntity, String> {

	Optional<ProductEntity> findByProductId(int productId);

}
