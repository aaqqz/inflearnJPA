package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest // 스프링 컨테이너 안에서 테스트 가능 (@Autowired .. 등등 스프링 기능 사용 가능)
@Transactional // org.springframework.XXX // Test에 있으면 실행후 롤백
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(member.getId()));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given

        //when

        //then
    }
}