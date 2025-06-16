package org.example.backend.Service.Impl;

import org.example.backend.Service.TestService;
import org.springframework.stereotype.Service;

@Service

public class TestServiceImpl implements TestService {

    @Override
    public String sayHello(String name) {
        return "Hello "+name+"!";
    }
}
