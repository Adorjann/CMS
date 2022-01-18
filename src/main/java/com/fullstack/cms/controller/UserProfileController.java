package com.fullstack.cms.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fullstack.cms.model.UserProfile;
import com.fullstack.cms.service.UserProfileService;

@Controller
@RequestMapping(value="/Users")
public class UserProfileController {

	public static final String KORISNIK_KEY = "prijavljeniKorisnik";
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private ServletContext servletContext;
	private String baseURL; 

	@PostConstruct
	public void init() {	
		baseURL = servletContext.getContextPath() + "/";			
	}

	@GetMapping(value="/PrijavljeniKorisnik")
	@ResponseBody
	public Map<String, Object> prijavljeniKorisnik(HttpSession session) {
		UserProfile prijavljeniKorisnik = (UserProfile) session.getAttribute(UserProfileController.KORISNIK_KEY);
		
		
		Map<String, Object> odgovor = new LinkedHashMap<>();
		
		if(prijavljeniKorisnik != null) {
			odgovor.put("status", "ok");
			odgovor.put("prijavljeniKorisnik", prijavljeniKorisnik);
			
		}else {
			odgovor.put("status", "404");
			odgovor.put("prijavljeniKorisnik", null);
		}
			
		return odgovor;
	}
	
	
	
	@PostMapping(value="/Login")
	@ResponseBody
	public Map<String, Object> postLogin(@RequestParam String userName, @RequestParam String password, 
			HttpSession session, HttpServletResponse response) throws IOException {
		try {
			// validacija
			UserProfile korisnik = userProfileService.findOne(userName);
			
			if (korisnik == null || (!korisnik.getPassword().equals(password)) ) {
				throw new Exception("Neispravno korisničko ime ili lozinka!");
			}			

			// prijava
			session.setAttribute(UserProfileController.KORISNIK_KEY, korisnik);
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "ok");	
			return odgovor;
		} catch (Exception ex) {
			// ispis greške
			String poruka = ex.getMessage();
			if (poruka == "") {
				poruka = "Neuspešna prijava!";
			}
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "greska");
			odgovor.put("poruka", poruka);
			return odgovor;
		}
	}

	@GetMapping(value="/Logout")
	@ResponseBody
	public Map<String, Object> logout(HttpSession session, HttpServletResponse response) throws IOException {
		// odjava	
		session.invalidate();
		
		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");	
		return odgovor;
	}
	
}
