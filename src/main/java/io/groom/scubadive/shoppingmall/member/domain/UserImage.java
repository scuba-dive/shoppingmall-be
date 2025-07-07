package io.groom.scubadive.shoppingmall.member.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_image")
public class UserImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이미지가 속한 사용자 (1:1 매핑)
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String imageUrl;

    public UserImage(User user, String imageUrl) {
        this.user = user;
        this.imageUrl = imageUrl;
    }

//    /**
//     * S3에 저장된 이미지 경로
//     */
//    @Column(columnDefinition = "TEXT", nullable = false)
//    private String imagePath;
//
//    /**
//     * S3 버킷 이름
//     */
//    @Column(nullable = false)
//    private String bucket;
//
//    /**
//     * 생성자 - 필수 필드 초기화
//     */
//    public UserImage(User user, String imagePath, String bucket) {
//        this.user = user;
//        this.imagePath = imagePath;
//        this.bucket = bucket;
//    }
//
//    /**
//     * 이미지 정보 업데이트
//     */
//    public void updateImage(String imagePath, String bucket) {
//        this.imagePath = imagePath;
//        this.bucket = bucket;
//    }
//
//    /**
//     * 전체 이미지 URL 생성 (S3 형식)
//     * 예: https://{bucket}.s3.amazonaws.com/{imagePath}
//     */
//    public String getFullImageUrl() {
//        if (bucket == null || imagePath == null) {
//            return null;
//        }
//
//        String cleanedPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
//        return "https://api.dicebear.com/9.x/notionists-neutral/svg?seed=" + cleanedPath;
//    }

}
