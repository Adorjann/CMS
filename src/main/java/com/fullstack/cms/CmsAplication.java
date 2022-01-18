package com.fullstack.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class CmsAplication extends SpringBootServletInitializer {
	
	
  
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CmsAplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(CmsAplication.class, args);
	}

	

}
