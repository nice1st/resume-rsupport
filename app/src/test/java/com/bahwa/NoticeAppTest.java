/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.bahwa;

import com.bahwa.configuration.QueryDslConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(QueryDslConfiguration.class)
class NoticeAppTest {

    @Test
	void contextLoads() {
	}
}
