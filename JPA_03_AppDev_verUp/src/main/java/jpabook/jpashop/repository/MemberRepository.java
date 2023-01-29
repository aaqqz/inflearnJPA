package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 구현체가 없어도 만들어줌 (Spring Data Jpa)
    // 시그니처 단어보고 만들어줌 잘 써야함(아무거나 쓰면 안됨)
    // select m from Member m where m.name = ?
   List<Member> findByName(String name);
}
