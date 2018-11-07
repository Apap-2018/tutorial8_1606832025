package com.apap.tutorial6.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tutorial6.model.UserRoleModel;
import com.apap.tutorial6.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value="/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user, RedirectAttributes r) {
		if (userService.validatePassword(user.getPassword())) {
			userService.addUser(user);
			return "home";
		}
		r.addFlashAttribute("alert","alert-red");
		r.addFlashAttribute("alertText","Error! Password harus memiliki angka dan huruf serta minimal 8 karakter");
		return "redirect:/";
		
	}
	
	
	@RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
	private String changePassword() {
		return "update-password";
	}
	
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@PreAuthorize("hasRole('READ_PRIVILEGE')")
	private String changeUserPassword(Locale locale, 
	  @RequestParam("password") String password, 
	  @RequestParam("oldpassword") String oldPassword, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes ra) {
	    UserRoleModel user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
	    if (userService.checkIfValidOldPassword(user, oldPassword)) {
		    if (userService.validateNewPassword(password, confirmPassword)) {
		    	if (userService.validatePassword(password)) {
		    		userService.changeUserPassword(user, password);
			    	return "redirect:/";
		    	}
		    	ra.addFlashAttribute("alert","alert-red");
		        ra.addFlashAttribute("alertText","Error! Password harus memiliki angka dan huruf serta minimal 8 karakter!");
		    	return "redirect:/user/updatePassword";
		    }
		    ra.addFlashAttribute("alert","alert-red");
	        ra.addFlashAttribute("alertText","Error! Password baru dan konfirmasi password tidak sama!");
	    	return "redirect:/user/updatePassword";
	    }
    	System.out.println("password ga sama");
        ra.addFlashAttribute("alert","alert-red");
        ra.addFlashAttribute("alertText","Error! Password lama salah!");
    	return "redirect:/user/updatePassword";
	}
}
