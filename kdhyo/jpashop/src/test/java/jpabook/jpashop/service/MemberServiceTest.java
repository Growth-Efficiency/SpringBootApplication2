package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;

	@Test
	void 회원가입() {
		// given
		Member member = new Member();
		member.setName("kim");

		// when
		Long savedId = memberService.join(member);

		// then
		assertThat(member).isEqualTo(memberRepository.findOne(savedId));
	}

	@Test
	void 중복_회원_예외() {
		// given
		Member member1 = new Member();
		member1.setName("kim");

		Member member2 = new Member();
		member2.setName("kim");

		// when
		memberService.join(member1);

		// then
		assertThatThrownBy(() -> memberService.join(member2))
			.isInstanceOf(IllegalStateException.class);
	}

}