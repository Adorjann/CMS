package com.fullstack.cms.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;



@Component
public final class InitServletContextInitializer implements ServletContextInitializer {
 
	/** kod koji se izvrsava po pokretanju aplikacije kada je ServletContext kreiran */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	System.out.println("Inicijalizacija konteksta pri ServletContextInitializer...");

		
    	System.out.println("Uspeh ServletContextInitializer!");
    }

}