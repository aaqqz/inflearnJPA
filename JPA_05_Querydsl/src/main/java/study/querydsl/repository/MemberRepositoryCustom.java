package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> search(MemberSearchCondition condition);

    // fetchResults() 가 없어졌기에 searchComplex 와 코드가 동일해졌다
    Page<MemberTeamDto> searchSimple(MemberSearchCondition condition, Pageable pageable);

    Page<MemberTeamDto> searchComplex(MemberSearchCondition condition, Pageable pageable);


}
