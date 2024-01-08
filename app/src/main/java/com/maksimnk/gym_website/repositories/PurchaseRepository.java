package com.maksimnk.gym_website.repositories;

import com.maksimnk.gym_website.model.Purchase;
import com.maksimnk.gym_website.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByUserId(Long userId);

    @Query("SELECT p FROM Purchase p WHERE p.user.id = :userId AND p.purchaseDate > CURRENT_DATE ORDER BY p.purchaseDate DESC")
    List<Purchase> findRecentPurchasesByUserId(@Param("userId") Long userId);

    void deleteBySubscription(Subscription subscription);
}
