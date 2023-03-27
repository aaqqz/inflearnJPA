package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> search(MemberSearchCondition condition);

    // 단순 (paging, totalCount 통합 조회) - 향후 미지원
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);

    // 복잡 (countQuery 분리) - 이것으로 페이징 하시오
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);


}
