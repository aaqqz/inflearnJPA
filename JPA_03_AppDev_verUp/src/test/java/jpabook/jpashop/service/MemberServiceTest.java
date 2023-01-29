package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // junit 실행시 스프링과 같이 실행
@SpringBootTest // 스프링 컨테이너 안에서 테스트 가능 (@Autowired .. 등등 스프링 기능 사용 가능)
@Transactional // org.springframework.XXX // Test에 있으면 실행후 롤백
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepository;
    @Autowired EntityManager em;

    @Test
    //@Rollback(false) // 커밋 (db 반영 O)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
        //then
        em.flush(); // insert문 보내고 -> @Transactional 롤백 (db 반영 X)
        assertEquals(member, memberRepository.findOne(savedId)); // 같은 트랜젝션 안에서 같은 영속성컨텍스트에서 관리하기 때문에 true
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        System.out.println("111");
        Member member1 = new Member();
        member1.setName("kim1");
        System.out.println("222");
        Member member2 = new Member();
        member2.setName("kim1");
        System.out.println("333");
        //when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생햐야 한다!!!

        // @Test 옵션으로 Exception 설정 가능
//        try {
//            memberService.join(member2); // 예외가 발생햐야 한다!!!
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야 한다."); // Assert.fail -> 코드가 도착하면 안됨

    }

}