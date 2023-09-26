package eu.fbk.dslab.playful.engine.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import eu.fbk.dslab.playful.engine.exception.EntityException;
import eu.fbk.dslab.playful.engine.exception.UnauthorizedException;
import eu.fbk.dslab.playful.engine.security.SecurityHelper;
import jakarta.servlet.http.HttpServletRequest;

public class PlayfulController {
	private static transient final Logger logger = LoggerFactory.getLogger(PlayfulController.class);
	
	@Autowired
	SecurityHelper securityHelper;
	
	@ExceptionHandler(EntityException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody String badRequest(HttpServletRequest req, EntityException e) {
		String msg = String.format("Bad request: %s [%s]", req.getRequestURL().toString(), e.getMessage());
		logger.debug(msg);
		return msg;
	}	
	
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody String unauthorized(HttpServletRequest req, UnauthorizedException e) {
		String msg = String.format("Unauthorized: %s [%s]", req.getRequestURL().toString(), e.getMessage());
		logger.debug(msg);
		return msg;
	}	
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody String internalServerError(HttpServletRequest req, Exception e) {
		String msg = String.format("Internal Server Error: %s [%s]", req.getRequestURL().toString(), e.getMessage());
		logger.debug(msg);
		return msg;
	}

}
