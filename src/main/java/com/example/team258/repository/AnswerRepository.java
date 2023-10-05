package com.example.team258.repository;

import com.example.team258.entity.Answer;
import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer,Long> {

    List<Answer> findAllByUser(User user);

    Optional<Object> findByUserAndSurvey(User user, Survey survey);
}
