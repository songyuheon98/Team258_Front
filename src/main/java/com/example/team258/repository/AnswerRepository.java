package com.example.team258.repository;

import com.example.team258.entity.Answer;
import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer,Long> {

    List<Answer> findAllByUser(User user);

    Optional<Answer> findByUserAndSurvey(User user, Survey survey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Answer a WHERE a.answerId = :answerId")
    Optional<Answer> findByIdForUpdate(@Param("answerId") Long answerId);

}
