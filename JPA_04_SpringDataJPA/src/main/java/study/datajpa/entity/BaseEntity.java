package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // Auditingr 기능을 포함
@MappedSuperclass // jpa Entity 들이 @MappedSupperClass 가 선언된 클래스를 상속할 때 클래스의 필드들도 컬럼으로 인식
@Getter
public class BaseEntity extends BaseTimeEntity{

//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createDate;
//
//    @LastModifiedDate
//    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(updatable = false)
    private String createBy;

    @LastModifiedBy
    private String lastModifiedBy;
}