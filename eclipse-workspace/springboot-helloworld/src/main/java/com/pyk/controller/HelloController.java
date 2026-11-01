package com.pyk.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

	@RequestMapping("/hello")
	public String hello() {
		return "hello springboot";
	}
}
