package com.sandeep.review_service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sandeep.review_service.persistence.ReviewEntity;
import com.sandeep.review_service.persistence.ReviewRepository;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersistenceTests extends MySqlTestBase {

	@Autowired
	private ReviewRepository repository;
	private ReviewEntity savedEntity;

	@BeforeEach
	void setupDb() {
		repository.deleteAll();

		ReviewEntity entity = new ReviewEntity(1, 2, "a", "s", "c");
		savedEntity = repository.save(entity);

		assertEqualsReview(entity, savedEntity);
	}

	@Test
	void create() {
		ReviewEntity newEntity = new ReviewEntity(1, 3, "a", "s", "c");
		repository.save(newEntity);

		ReviewEntity foundEntity = repository.findById(newEntity.getId()).get();
		assertEqualsReview(newEntity, foundEntity);

		assertEquals(2, repository.count());
	}

	@Test
	void update() {
		savedEntity.setAuthor("a2");
		repository.save(savedEntity);

		ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();
		assertEquals(1, foundEntity.getVersion());
		assertEquals("a2", foundEntity.getAuthor());
	}

	@Test
	void delete() {
		repository.delete(savedEntity);
		assertFalse(repository.existsById(savedEntity.getId()));
	}

	@Test
	void findByProductId() {
		List<ReviewEntity> entities = repository.findByProductId(savedEntity.getProductId());

		assertThat(entities, hasSize(1));
		assertEqualsReview(savedEntity, entities.get(0));
	}

	@Test
	void duplicateError() {
		assertThrows(DataIntegrityViolationException.class, () -> {
			ReviewEntity entity = new ReviewEntity(1, 2, "a", "s", "c");
			repository.save(entity);
		});
	}

	@Test
	void optimisticLockError() {
		ReviewEntity entity1 = repository.findById(savedEntity.getId()).get();
		ReviewEntity entity2 = repository.findById(savedEntity.getId()).get();

		entity1.setAuthor("a1");
		repository.save(entity1);

		assertThrows(OptimisticLockingFailureException.class, () -> {
			entity2.setAuthor("a2");
			repository.save(entity2);
		});
	}

	private void assertEqualsReview(ReviewEntity expectedEntity, ReviewEntity actualEntity) {
		assertEquals(expectedEntity.getId(), actualEntity.getId());
		assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
		assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
		assertEquals(expectedEntity.getReviewId(), actualEntity.getReviewId());
		assertEquals(expectedEntity.getAuthor(), actualEntity.getAuthor());
		assertEquals(expectedEntity.getSubject(), actualEntity.getSubject());
		assertEquals(expectedEntity.getContent(), actualEntity.getContent());
	}

}
