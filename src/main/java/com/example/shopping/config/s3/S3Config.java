package com.example.shopping.config.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 *   writer : YuYoHan
 *   work :
 *          S3에 이미지나 파일을 올리기 위해서 S3설정 클래스입니다.
 *   date : 2023/12/04
 * */
@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        // Amazon S3에 액세스하기 위한 기본 AWS 자격 증명을 생성합니다.
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                // 생성된 자격 증명을 사용하여 Amazon S3 클라이언트에 자격 증명을 제공
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                // Amazon S3 클라이언트가 사용할 리전을 설정
                .withRegion(region)
                .build();
    }
}
