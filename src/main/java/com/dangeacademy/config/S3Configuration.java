package com.dangeacademy.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

    @Value("${cloud.aws.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.secret-key}")
    private String awsSecretKey;

    @Value("${cloud.aws.region}")
    private  String region;


    //A Client is an object provided by AWS SDK that allows your application to talk to an AWS service.
    //A bridge between your application and AWS.
    //Without client → your app cannot communicate with AWS.
    //We are creating an object that knows: = AWS endpoint URL,Region ,Authentication credentials, How to sign requests,How to send HTTP requests,How to handle responses

    @Bean
    public AmazonS3 client(){

        //here we crate a credentials using  accessKey and secretKey but AWSCredentials is  interface,
        // so we use its implemented class BasicAWSCredentials
        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey,awsSecretKey);

        // By using that credentials we build a AmazonS3 , Amazon s3 is also  interface so we make object using
        // a AmazonS3ClientBuilder and build by providing a credentials and region
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        return amazonS3;
    }

}

