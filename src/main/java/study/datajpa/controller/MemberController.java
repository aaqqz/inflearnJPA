package study.datajpa.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members") // members?page=0&size=3 -> 페이징 지원 , ex) page=0&size=3&sort=id,desc&sort=username,desc
    //public Page<Member> list(Pageable pageable){
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){ // 기본 값이 아닌 옵션을 넣고 싶을때
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(member -> new MemberDto(member));

        return map;
    }

    //@PostConstruct
    public void init(){
        //memberRepository.save(new Member("memberA"));

        for (int i = 1; i <= 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
