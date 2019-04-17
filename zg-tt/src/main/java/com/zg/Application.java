package com.zg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ComponentScan("com.zg.*")
public class Application {
	
    public static void main( String[] args ){
       System.out.println(SpringApplication.run(Application.class, args));
    }
    
}
