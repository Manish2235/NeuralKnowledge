package com.incapp.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incapp.entity.MyUser;
import com.incapp.repo.UserRepo;

@Service
public class UserService {

    

	@Autowired
	private UserRepo userRepo;

  
	
	public MyUser getUserByUsername(String username) {
		return userRepo.findById(username).orElse(null);
	}
	
	@Bean
	public BCryptPasswordEncoder getEncoder(){
		return new BCryptPasswordEncoder();
	}

	public boolean save(MyUser myUser ) {
		BCryptPasswordEncoder b=getEncoder();
		if(myUser.getPassword()!=null) {
			myUser.setPassword(b.encode(myUser.getPassword()));
		}
		MyUser u=userRepo.findById(myUser.getUsername()).orElse(null);
		if(u==null) {
		
			userRepo.save(myUser);
			return true;
		}else {
			if(u.getPassword()==null) {
				userRepo.save(myUser);
				return true;
			}
			return false;
		}
	}

	public boolean updatePassword(String newPassword, String email) {
		
		MyUser user=userRepo.findById(email).orElse(null);
		BCryptPasswordEncoder b=getEncoder();
		if(user!=null) {
			user.setPassword(b.encode(newPassword));
			userRepo.save(user);
			return true;
		}
		
		
		return false;
	}

	public String updateProfileImage(MyUser user, MultipartFile img) throws IOException{
		
		MyUser u=userRepo.findById(user.getUsername()).orElse(null);
		
		if(u!=null) {
			byte[] image=img.getBytes();
			u.setPhoto(image);
			userRepo .save(u);
			return "success";
		}
		
		
		return "failed";
	}

	public boolean changePassword(String userName, String oldPassword, String newPassword) {
		BCryptPasswordEncoder b=getEncoder();
		MyUser user=userRepo.findById(userName).orElse(null);
		
		if(user!=null && b.matches(oldPassword, user.getPassword())) {
			
			user.setPassword(b.encode(newPassword));
			userRepo.save(user);
			return true;
		}
		
		return false;
	}

	

	
	
}
