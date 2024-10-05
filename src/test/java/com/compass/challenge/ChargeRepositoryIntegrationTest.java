package com.compass.challenge;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import com.compass.challenge.domain.Charge;
import com.compass.challenge.repository.ChargeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This class test some methods of {@link ChargeRepository}
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ChargeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChargeRepository chargeRepository;

    /**
     * Testing the method findById
     */
    @Test
    public void findByIdThenReturnCharge() {

        Charge.Builder builder = new Charge.Builder();
        builder.amount(200).sellerId(3l);
        Charge charge = builder.build();

        entityManager.persist(charge);
        entityManager.flush();

        Optional<Charge> found = chargeRepository.findById(charge.getId());
        assertEquals(charge.getId(), found.get().getId());
        assertEquals(charge.getAmount(), found.get().getAmount(), 0.0004f);
        assertEquals(charge.getSellerId(), found.get().getSellerId());
    }

    /**
     * Testing the method findAll
     */
    @Test
    public void findAllThenReturnList() {
        List<Charge> foundall = chargeRepository.findAll();
        assertEquals(5, foundall.size());
    }

    /**
     * Testing the method save
     */
    @Test
    public void saveThanValidate() {

        Charge.Builder builder = new Charge.Builder();
        builder.amount(200).sellerId(3l);
        Charge charge = builder.build();

        entityManager.persist(charge);
        entityManager.flush();


        Charge.Builder newBuilder = new Charge.Builder();
        newBuilder.amount(300).sellerId(3l);
        Charge newCharge = newBuilder.build();

        Charge savedCharge = chargeRepository.save(newCharge);

        assertEquals(savedCharge.getId(), newCharge.getId());
        assertEquals(savedCharge.getAmount(), newCharge.getAmount(), 0.0004f);
        assertEquals(savedCharge.getSellerId(), newCharge.getSellerId());
    }
}
