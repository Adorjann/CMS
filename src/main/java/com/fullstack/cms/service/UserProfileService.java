package com.fullstack.cms.service;

import java.util.List;

import com.fullstack.cms.model.UserProfile;

public interface UserProfileService {

	UserProfile findOne(String userName);
	UserProfile findById(Long id);
	
	UserProfile save(UserProfile user);
	UserProfile delete(Long id);
	

}
