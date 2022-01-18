package com.fullstack.cms.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.cms.model.UserProfile;
import com.fullstack.cms.repository.UserProfileRepository;
import com.fullstack.cms.service.UserProfileService;

@Service
public class JPAUserProfileService implements UserProfileService {
	
	@Autowired
	private UserProfileRepository repository;

	@Override
	public UserProfile findOne(String userName) {
		
		return repository.findByUserName(userName);
	}

	@Override
	public UserProfile findById(Long id) {
		
		
		Optional<UserProfile> user = repository.findById(id);
		
		if(user.isEmpty()) {
			return null;
		}
		return user.get();
	}

	@Override
	public UserProfile save(UserProfile user) {
		return repository.save(user);
	}

	@Override
	public UserProfile delete(Long id) {
		UserProfile user = findById(id);
		
		if(user != null) {
			repository.delete(user);
			return user;
		}
		return null;
		
		
	}

	

	

}
