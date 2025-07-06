package io.groom.scubadive.shoppingmall.global.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class S3Uploader {
    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3Uploader(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.s3.bucket}") String bucket
    ) {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        this.bucket = bucket;
    }

    public void upload(MultipartFile file, String imagePath) throws IOException {
        String s3Key = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;

        // ObjectMetadata에 필수 정보 추가
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucket, s3Key, file.getInputStream(), metadata));
        // ACL 옵션 제거! (withCannedAcl 등)
    }

    public String getBucketName() {
        return this.bucket;
    }
}
