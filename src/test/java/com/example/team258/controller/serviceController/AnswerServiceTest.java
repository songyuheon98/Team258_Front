package com.example.team258.controller.serviceController;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.entity.*;
import com.example.team258.repository.AnswerRepository;
import com.example.team258.repository.SurveyRepository;
import com.example.team258.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnswerServiceTest {

    private AnswerService answerService;
    private AnswerRepository answerRepository;
    private SurveyRepository surveyRepository;

    @BeforeEach
    public void setUp() {
        answerRepository = new AnswerRepositoryStub();
        surveyRepository = new SurveyRepositoryStub();
        answerService = new AnswerService(answerRepository, surveyRepository);
    }

    @Test
    public void testCreateAnswer() {

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("질문질문")
                .choices("대충 답지 3개 있음")
                .deadline(LocalDateTime.of(2023,10,31,0,0))
                .maxChoice(3L)
                .user(user)
                .build();

        surveyRepository.save(survey);

        AnswerRequestDto requestDto = AnswerRequestDto.builder()
                .surveyId(1L)
                .answer(2L)
                .build();

        MessageDto response = answerService.createAnswer(requestDto, user);

        assertNotNull(response);
        assertEquals("작성이 완료되었습니다.", response.getMsg());
    }

    @Test
    public void testGetAnswers() {
        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("질문질문")
                .choices("대충 답지 3개 있음")
                .deadline(LocalDateTime.of(2023,10,31,0,0))
                .maxChoice(3L)
                .user(user)
                .build();

        surveyRepository.save(survey);
        for(long i = 1;i<11;i++) {
            Answer answer = Answer.builder()
                    .answerId(i)
                    .answerNum(i%3+1)
                    .survey(survey)
                    .user(user)
                    .build();
            answerRepository.save(answer);
        }

        List<AnswerResponseDto> response = answerService.getAnswers(user);

        assertNotNull(response);
        for(int i = 1;i<11;i++){
            System.out.println(i + "번째 데이터 비교");
            assertEquals(i%3+1, response.get(i-1).getAnswer());
        }

    }

    @Test
    public void testUpdateAnswer() {
        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("질문질문")
                .choices("대충 답지 3개 있음")
                .deadline(LocalDateTime.of(2023,10,31,0,0))
                .maxChoice(3L)
                .user(user)
                .build();

        surveyRepository.save(survey);

        for(long i = 1;i<11;i++) {
            Answer answer = Answer.builder()
                    .answerId(i)
                    .answerNum(i%3+1)
                    .survey(survey)
                    .user(user)
                    .build();
            answerRepository.save(answer);
        }

        AnswerRequestDto requestDto = AnswerRequestDto.builder()
                .surveyId(2L)
                .answer(1L)
                .build();
        Long answerId = 1L;

        MessageDto response = answerService.updateAnswer(requestDto, answerId, user);

        assertNotNull(response);
        assertEquals("수정이 완료되었습니다.", response.getMsg());
    }

    @Test
    public void testDeleteAnswer() {
        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("질문질문")
                .choices("대충 답지 3개 있음")
                .deadline(LocalDateTime.of(2023,10,31,0,0))
                .maxChoice(3L)
                .user(user)
                .build();

        surveyRepository.save(survey);

        for(long i = 1;i<11;i++) {
            Answer answer = Answer.builder()
                    .answerId(i)
                    .answerNum(i%3+1)
                    .survey(survey)
                    .user(user)
                    .build();
            answerRepository.save(answer);
        }

        Long answerId = 4L;

        MessageDto response = answerService.deleteAnswer(answerId, user);

        assertNotNull(response);
        assertEquals("삭제가 완료되었습니다.", response.getMsg());
    }


    private static class AnswerRepositoryStub implements AnswerRepository {
        private List<Answer> answers = new ArrayList<>();

        @Override
        public List<Answer> findAllByUser(User user) {
            return answers.stream().filter(i->i.getUser().getUserId()==user.getUserId()).collect(Collectors.toList());
        }

        @Override
        public Optional<Answer> findByUserAndSurvey(User user, Survey survey) {
            for(Answer ans:answers){
                if (ans.getSurvey().getSurveyId() == survey.getSurveyId() && ans.getUser().getUserId()==user.getUserId())
                    return Optional.of(ans);
            }
            return Optional.empty();
        }

        @Override
        public <S extends Answer> S save(S entity) {
            answers.add(entity);
            return entity;
        }

        @Override
        public void delete(Answer entity) {
            answers.remove(entity);
        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {

        }

        @Override
        public void deleteAll(Iterable<? extends Answer> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends Answer> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends Answer> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<Answer> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public Answer getOne(Long aLong) {
            return null;
        }

        @Override
        public Answer getById(Long aLong) {
            return null;
        }

        @Override
        public Answer getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends Answer> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Answer> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends Answer> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends Answer> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Answer> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Answer> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Answer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public <S extends Answer> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<Answer> findById(Long aLong) {
            for(Answer a : answers){
                if (a.getAnswerId() == aLong) return Optional.of(a);
            }
            return Optional.empty();
        }

        @Override
        public boolean existsById(Long aLong) {
            return false;
        }

        @Override
        public List<Answer> findAll() {
            return null;
        }

        @Override
        public List<Answer> findAllById(Iterable<Long> longs) {
            return null;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long aLong) {

        }

        @Override
        public List<Answer> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<Answer> findAll(Pageable pageable) {
            return null;
        }

        // 나머지 메서드도 필요한 경우 구현
    }

    private static class SurveyRepositoryStub implements SurveyRepository {
        private List<Survey> surveys = new ArrayList<>();

        @Override
        public void flush() {

        }

        @Override
        public <S extends Survey> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends Survey> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<Survey> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public Survey getOne(Long aLong) {
            return null;
        }

        @Override
        public Survey getById(Long aLong) {
            return null;
        }

        @Override
        public Survey getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends Survey> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Survey> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends Survey> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends Survey> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Survey> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Survey> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Survey, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public <S extends Survey> S save(S entity) {
            surveys.add(entity);
            return null;
        }

        @Override
        public <S extends Survey> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<Survey> findById(Long aLong) {
            for(Survey s:surveys){
                if(s.getSurveyId() == aLong)
                    return Optional.of(s);
            }
            return Optional.empty();
        }

        @Override
        public boolean existsById(Long aLong) {
            return false;
        }

        @Override
        public List<Survey> findAll() {
            return null;
        }

        @Override
        public List<Survey> findAllById(Iterable<Long> longs) {
            return null;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long aLong) {

        }

        @Override
        public void delete(Survey entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {

        }

        @Override
        public void deleteAll(Iterable<? extends Survey> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public List<Survey> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<Survey> findAll(Pageable pageable) {
            return null;
        }
        // SurveyRepositoryStub 클래스도 AnswerRepositoryStub과 유사한 방식으로 구현
    }
}
