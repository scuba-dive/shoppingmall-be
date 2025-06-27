package io.groom.scubadive.shoppingmall.member.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String imagePath;

    @Column(length = 100)
    private String bucket;

    public UserImage(String imagePath, String bucket) {
        this.imagePath = imagePath;
        this.bucket = bucket;
    }

    public void updateImage(String imagePath, String bucket) {
        this.imagePath = imagePath;
        this.bucket = bucket;
    }
}