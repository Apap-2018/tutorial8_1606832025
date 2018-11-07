package com.apap.tutorial6.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apap.tutorial6.model.UserRoleModel;
import com.apap.tutorial6.repository.UserRoleDB;

@Service
public class UserRoleServiceImpl implements UserRoleService{

	@Autowired
	private UserRoleDB userDb;
	
	@Override
	public UserRoleModel addUser(UserRoleModel user) {
		String pass = encrypt(user.getPassword());
		user.setPassword(pass);
		return userDb.save(user);
		
	}

	@Override
	public String encrypt(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
				
	}
	@Override
	public boolean checkIfValidOldPassword(UserRoleModel user, String oldPassword) {
		System.out.println("password: "+user.getPassword());
		System.out.println("password input: "+oldPassword);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(oldPassword, user.getPassword());
	}

	@Override
	public UserRoleModel findUserByUsername(String username) {
		return userDb.findByUsername(username);
	}

	@Override
	public void changeUserPassword(UserRoleModel user, String password) {
	    String encryptPass = encrypt(password);
	    user.setPassword(encryptPass);
		userDb.save(user);
	}
	
	@Override
	public boolean validatePassword(String password) {
		System.out.println(password);
		return Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z]).{8,}", password);
	}

	@Override
	public boolean validateNewPassword(String password, String confirmPassword) {
		return password.equals(confirmPassword);
	}

	
	

}
