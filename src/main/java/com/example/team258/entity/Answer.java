package com.example.team258.entity;

import com.example.team258.entity.Survey;
import com.example.team258.entity.Timestamped;
import com.example.team258.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Answer extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "answer_num", nullable = false)
    private Long answerNum;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="survey_id")
    private Survey survey;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Answer(Long answerNum, User user,Survey survey) {
        this.answerNum = answerNum;
        this.survey = survey;
        this.user = user;
        survey.addAnswer(this);
        user.addAnswer(this);
    }

    public void addUser(User user){
        this.user = user;
    }

    public void update(Long answerNum) {
        this.answerNum = answerNum;
    }

}
