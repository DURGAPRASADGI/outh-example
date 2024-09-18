package com.secure.notes.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendPasswordThroughMail(String to,String resetUrl) {
		SimpleMailMessage mailMessage=new SimpleMailMessage();
		mailMessage.setSubject("Reset password");
		mailMessage.setText("please link the click "+resetUrl);
		mailMessage.setTo(to);
		mailSender.send(mailMessage);
	}

}
