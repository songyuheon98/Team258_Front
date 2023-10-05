package com.example.team258.answer;

import com.example.team258.answer.Answer;
import com.example.team258.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {

    List<Answer> findAllByUser(User user);
}
