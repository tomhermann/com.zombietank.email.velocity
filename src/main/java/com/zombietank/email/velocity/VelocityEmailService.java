package com.zombietank.email.velocity;

import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;

import java.io.Serializable;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

import com.zombietank.email.Email;
import com.zombietank.email.exception.EmailException;
import com.zombietank.email.support.AbstractTemplatedEmailService;

public class VelocityEmailService extends AbstractTemplatedEmailService {
	private static final Logger log = LoggerFactory.getLogger(VelocityEmailService.class);
	private final VelocityEngine velocityEngine;

	public VelocityEmailService(JavaMailSender mailSender, VelocityEngine velocityEngine) {
		super(mailSender);
		this.velocityEngine = velocityEngine;
	}
	
	@Override
	public void send(Email email, String templateLocation, Map<String, ? extends Serializable> model) throws EmailException {
		log.debug("Using templateLocation: {}", templateLocation);
		log.debug("Using model: {}", model);

		try {
			String message = mergeTemplateIntoString(velocityEngine, templateLocation, model);
			log.debug("Merged message: {}", message);
			send(email.withMessage(message));
		} catch(Exception e) {
			throw new EmailException("Failed: email not sent to: " + email.getTo(), e);
		}
	}
}
