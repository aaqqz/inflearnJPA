package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // public 메소드에 모두 적용됨
                                // jpa 사용시 필수 @Transactional - default (readOnly = false)
                                // 읽기 전용(조회) 쿼리에서 성능 최적화
                                // 현재 클래스에 읽기 전용이 많기에 (readOnly = true) 속성을 줬음
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional // class level에 어노테이션 보다 우선권을 가짐 (readOnly = true) 이면 안되는 - 쓰기 전용 쿼리에 사용
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    //@Transactional(readOnly = true) // 읽기 전용(조회) 쿼리에서 성능 최적화
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    //@Transactional(readOnly = true) // 읽기 전용(조회) 쿼리에서 성능 최적화
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
}
