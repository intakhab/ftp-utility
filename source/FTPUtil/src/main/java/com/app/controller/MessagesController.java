package com.app.controller;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessagesController {
	
	 
    //@RequestMapping(value="/logmessages",produces = "text/html")
   // @GetMapping(path = "/logmessages", produces = "text/html")
	@GetMapping(value = "/logmessages", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String handle(String msg) {
        return "Hello  "+new Date().toString();
    }
	@RequestMapping("/hi")
    public @ResponseBody String hiThere(){
        return "hello world!";
    }
}
