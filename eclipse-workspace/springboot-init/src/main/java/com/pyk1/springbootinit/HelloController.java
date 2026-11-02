package com.pyk1.springbootinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@Value("${name}")
	private String name;
	
	@Value("${person.name}")
	private String name2;
	
	@Value("${person.age}")
	private int age2;

	@Value("${address[0]}")
	private String address;
	
	@Value("${msg1}")
	private String msg1;
	
	@Value("${msg2}")
	private String msg2;
	
	@Autowired
	private Environment env;
	
	@RequestMapping("/hello2")
	public String hello2() {
		
		System.out.println(env.getProperty("msg2"));
		System.out.println(env.getProperty("address[1]"));
		

		System.out.println("--------------------");
		
		System.out.println(msg2);
		
		System.out.println(msg1);
		
		System.out.println(address);
		
		System.out.println(name);

		System.out.println(name2);

		System.out.println(age2);
		
		return "hello Spring Boot 2222!";
	}
	
	@RequestMapping("/hello")
	public String hello() {
		return "hello Spring Boot 222!";
	}
}
