package com.horace.toolbackend;

import com.horace.toolbackend.util.LocalEnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToolBackendApplication {

    static void main(String[] args) {
        LocalEnvLoader.load();
        SpringApplication.run(ToolBackendApplication.class, args);
    }

}
