package com.example.team258.entity;

import com.example.team258.answer.Answer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Survey extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long surveyId;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "choices",nullable = false)
    private String choices;

    @Column(name = "maxchoice")
    private Long maxChoice;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "survey")
    private List<Answer> answers = new ArrayList<>();

    public void addUser(User user){
        this.user = user;
    }

}
