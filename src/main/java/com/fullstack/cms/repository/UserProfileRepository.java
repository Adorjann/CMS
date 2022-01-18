package com.fullstack.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.cms.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

	UserProfile findByUserName(String userName);

}
