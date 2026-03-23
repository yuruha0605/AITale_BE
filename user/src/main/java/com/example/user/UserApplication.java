package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { // rabbitMq 사용 안함. 테스트에서만 사용하고 삭제하기!
		org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class
})
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
