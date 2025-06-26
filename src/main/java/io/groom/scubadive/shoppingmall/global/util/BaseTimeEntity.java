package io.groom.scubadive.shoppingmall.global.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티에서 공통적으로 사용되는
 * 생성일시(createdAt), 수정일시(updatedAt)를 관리하는 추상 클래스입니다.
 *
 * @MappedSuperclass : JPA 엔티티 클래스들이 이 클래스를 상속하면
 *   해당 필드들이 테이블 컬럼으로 인식됩니다.
 * @EntityListeners(AuditingEntityListener.class) :
 *   스프링 데이터 JPA의 Auditing 기능을 적용하기 위한 설정입니다.
 *   createdAt, updatedAt 값을 자동으로 채워줍니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /**
     * 생성일시 - 엔티티가 처음 저장될 때 자동으로 기록됩니다.
     * (수정 불가, insert 전용)
     */
    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    /**
     * 수정일시 - 엔티티가 변경될 때마다 자동으로 갱신됩니다.
     */
    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
