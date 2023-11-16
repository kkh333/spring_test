package com.std.sbb;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@DisplayName("질문 넣기")
	void testJpa1() {
		Question q1 = new Question();
		q1.setSubject("질문1 타이틀 입니다.");
		q1.setContent("질문1 내용 입니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("질문2 타이틀 입니다.");
		q2.setContent("질문2 내용 입니다.");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);  // 두번째 질문 저장

		Question q3 = new Question();
		q3.setSubject("질문3 타이틀 입니다.");
		q3.setContent("질문3 내용 입니다.");
		q3.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q3);  // 세번째 질문 저장
	}

	@Test
	@DisplayName("질문 전체 조회")
	void testJpa2() {
		List<Question> all = this.questionRepository.findAll();
		assertEquals(3, all.size());

		Question q1 = all.get(0);
		assertEquals("질문1 타이틀 입니다.", q1.getSubject());

		Question q2 = all.get(1);
		assertEquals("질문2 타이틀 입니다.", q2.getSubject());
	}

	@Test
	@DisplayName("질문 id 조회")
	void testJpa3() {
		Optional<Question> oq = this.questionRepository.findById(1);
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("질문1 타이틀 입니다.", q.getSubject());
		}
	}

	@Test
	@DisplayName("질문 subject 조회")
	void testJpa4() {
		Question q = this.questionRepository.findBySubject("질문1 타이틀 입니다.");
		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("질문 subjectAndContent 조회")
	void testJpa5() {
		Question q = this.questionRepository.findBySubjectAndContent(
				"질문1 타이틀 입니다.", "질문1 내용 입니다.");
		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("질문 검색 내용 조회")
	void testJpa6() {
		List<Question> qList = this.questionRepository.findBySubjectLike("질문%");
		Question q = qList.get(0);
		assertEquals("질문1 타이틀 입니다.", q.getSubject());
	}

	@Test
	@DisplayName("질문 수정하기")
	void testJpa7() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("질문1 타이틀 입니다. 수정됌");
		this.questionRepository.save(q);
	}

	@Test
	@DisplayName("질문 삭제하기")
	void testJpa8() {
		assertEquals(3, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(2, this.questionRepository.count());
	}

	@Test
	@DisplayName("답변 넣기")
	void testJpa9() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a1 = new Answer();
		a1.setContent("질문1 답변입니다1.");
		a1.setQuestion(q);
		a1.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a1);

		Answer a2 = new Answer();
		a2.setContent("질문1 답변입니다2.");
		a2.setQuestion(q);
		a2.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a2);
	}

	@Test
	@DisplayName("답변 조회하기")
	void testJpa10() {
		Optional<Answer> oa = this.answerRepository.findById(2);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(1, a.getQuestion().getId());
	}

	@Transactional
	@Test
	@DisplayName("답변에 연결된 질문 찾기")
	void testJpa11() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(2, answerList.size());
		assertEquals("질문1 답변입니다1.", answerList.get(0).getContent());
	}

}
