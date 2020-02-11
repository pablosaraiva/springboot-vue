package com.pablosaraiva.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController
{
	@GetMapping("works")
	public String test()
	{
		return "yep!";
	}

}
