package io.groom.scubadive.shoppingmall.member.domain;

import io.groom.scubadive.shoppingmall.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_image")
public class UserImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String bucket;

    public UserImage(User user, String imagePath, String bucket) {
        this.user = user;
        this.imagePath = imagePath;
        this.bucket = bucket;
    }

    public void updateImage(String imagePath, String bucket) {
        this.imagePath = imagePath;
        this.bucket = bucket;
    }

    public String getFullImageUrl() {
        if (bucket == null || imagePath == null) {
            return null;
        }
        String cleanedPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;

        return "https://" + bucket + ".s3.amazonaws.com/" + cleanedPath;
    }

}
