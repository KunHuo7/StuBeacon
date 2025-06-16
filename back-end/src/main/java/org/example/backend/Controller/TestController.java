package org.example.backend.Controller;

import jakarta.annotation.Resource;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Service.TestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/test")

public class TestController {
    @Resource
    TestService testService;

    @PostMapping("login")
    public String login(@RequestParam("zhanghao") String username,@RequestParam("mima")String password){
        System.out.println(username+password);
        return null;
    }
    @PostMapping("register")
    public String register(@RequestBody RequestBody registerBody){
        System.out.println(registerBody);
        return null;
    }
    @GetMapping("{id}")
    public String getSomeOne(@PathVariable Integer id){
        System.out.println(id);
        return null;
    }
    @GetMapping("sayHello")
    public RestBean<String> sayHello(@RequestParam String name){
    return RestBean.success(testService.sayHello(name));
    }
}
