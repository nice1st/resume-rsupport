/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.bahwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NoticeApp {
    
    public static void main(String[] args) {
		SpringApplication.run(NoticeApp.class, args);
	}
}
