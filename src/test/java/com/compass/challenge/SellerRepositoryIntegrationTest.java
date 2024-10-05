package com.compass.challenge;

import com.compass.challenge.domain.Seller;
import com.compass.challenge.repository.SellerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * This class test some methods of {@link SellerRepository}
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class SellerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SellerRepository sellerRepository;

    /**
     * Testing the method findById
     */
    @Test
    public void findByIdThenReturnCharge() {

        Seller.Builder sellerBuilder = new Seller.Builder();
        sellerBuilder.id(10L).name("Antonio");
        Seller seller = sellerBuilder.build();

        entityManager.persist(seller);
        entityManager.flush();

        Optional<Seller> found = sellerRepository.findById(seller.getId());
        assertEquals(seller.getId(), found.get().getId());
		assertEquals(seller.getName(), found.get().getName());
    }
    
    /**
     * Testing the method findAll
     */
    @Test
    public void findAllThenReturnList() {
        List<Seller> foundall = sellerRepository.findAll();
        assertEquals(3, foundall.size());
    }
    
    /**
     * Testing the method save
     */
    @Test
    public void saveThanValidate() {

        Seller.Builder sellerBuilder = new Seller.Builder();
        sellerBuilder.id(10l).name("Junior");
        Seller seller = sellerBuilder.build();

		entityManager.persist(seller);
		entityManager.flush();

        Seller.Builder newSellerBuilder = new Seller.Builder();
        newSellerBuilder.id(1L).name("Marcos");
        Seller newSeller = newSellerBuilder.build();

        Seller savedCharge = sellerRepository.save(newSeller);
    	
    	assertEquals(savedCharge.getId(), newSeller.getId());
    	assertEquals(savedCharge.getName(), newSeller.getName());
    }
 
}
