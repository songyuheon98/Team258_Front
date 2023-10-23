package com.example.team258.common.repository;


import com.example.team258.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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


}
