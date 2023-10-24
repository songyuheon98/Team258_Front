package com.example.team258.common.repository;


import com.example.team258.common.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.QueryHint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable pageable);

    @Query("select u from users u " +
            "join fetch u.bookRents br join fetch br.book b " +
            "WHERE u.userId = :userId")
    Optional<User> findByIdFetchBookRent(@Param("userId") Long userId);

    @Query("select u from users u " +
            "join fetch u.bookReservations br join fetch br.book b " +
            "WHERE u.userId = :userId")
    Optional<User> findByIdFetchBookReservation(@Param("userId") Long userId);


    /**
     * 1대 다 관계라서 데이터 뻥튀기 가능
     */
    @Query("select u from users u " +
            "join fetch u.bookApplyDonations bad " +
            "join fetch u.bookRents br " +
            "join fetch u.bookReservations brd " +
            "where u.userId = :userId")
    Optional<User> findFetchJoinById(@Param("userId") Long userId);

    @Query("select u from users u " +
            "left join fetch u.bookApplyDonations bad " +
            "left join fetch bad.book b " +
            "where u.userId = :userId")
    Optional<User> findFetchJoinBookById(@Param("userId") Long userId);

    @Query("SELECT u FROM users u WHERE u.userId = :userId")
    @QueryHints({ @QueryHint(name = "org.hibernate.fetchSize", value = "100") })
    Optional<User> findJPQLById(@Param("userId") Long userId);

}
