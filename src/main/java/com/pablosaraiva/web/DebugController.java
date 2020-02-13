package com.pablosaraiva.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class DebugController
{
	@GetMapping("works")
	public String test()
	{
		return "yep!";
	}

	@GetMapping("/mdm")
	public RedirectView needToFixTheRouteViaVuePressLater()
	{
		return new RedirectView("/mdm/index.html");
	}

}
