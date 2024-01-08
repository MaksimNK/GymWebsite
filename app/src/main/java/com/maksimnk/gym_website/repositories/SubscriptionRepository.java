package com.maksimnk.gym_website.repositories;

import com.maksimnk.gym_website.model.Subscription;
import com.maksimnk.gym_website.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findTop1ByUserIdAndEndDateAfterOrderByEndDateDesc(Long userId, Date currentDate);

    default Optional<Subscription> findTop1ByUserIdAndEndDateAfterOrderByEndDateDesc(Long userId, LocalDate currentDate) {
        Date convertedDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return findTop1ByUserIdAndEndDateAfterOrderByEndDateDesc(userId, convertedDate);
    }

    void deleteByUser(User user);
}
