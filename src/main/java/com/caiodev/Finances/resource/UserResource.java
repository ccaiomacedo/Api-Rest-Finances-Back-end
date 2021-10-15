package com.caiodev.Finances.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

    @RequestMapping(value ="/",method = RequestMethod.GET)
    public String helloWorld(){
        return "Hello World!";
    }


}
