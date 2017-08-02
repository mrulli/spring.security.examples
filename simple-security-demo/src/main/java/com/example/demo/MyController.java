package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.config.IAuthenticationFacade;
import com.example.demo.mappings.ForbiddenException;

@Controller
public class MyController {

	@Autowired
    private IAuthenticationFacade authenticationFacade;
	
	/**
	 * Using ROLE-based authentication
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/", 
			method=RequestMethod.GET, 
			produces = "application/json")
	@ResponseBody
	@Secured({"ROLE_ADMIN"})
	public MyResult mySimpleGet(
			@RequestParam(value="name") String name) {
		MyResult res = new MyResult();
		res.setName(name);
		return res;
	}

	/**
	 * Accessing the context programmatically
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/ctx/", 
			method=RequestMethod.GET, 
			produces = "application/json")
	@ResponseBody
	public MyResult protectedWithCtx(
			@RequestParam(value="name") String name) {
		
		Authentication authentication = authenticationFacade.getAuthentication();
        String uname = authentication.getName();
        
        if (!uname.equals(name)) {
        		throw new ForbiddenException();
        }
        
		MyResult res = new MyResult();
		res.setName(name);
		return res;
	}
	
	/**
	 * Use Spring Expression Language
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/spel/", 
			method=RequestMethod.GET, 
			produces = "application/json")
	@ResponseBody
	@PreAuthorize("@securityService.canEdit(authentication, #name)")
	public MyResult protectedWithSpEL(
			@RequestParam(value="name") String name) {
		MyResult res = new MyResult();
		res.setName(name);
		return res;
	}
	
	/**
	 * Use a custom PermissionEvaluator
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/permeval/", 
			method=RequestMethod.GET, 
			produces = "application/json")
	@ResponseBody
	@PreAuthorize("hasPermission(#name, 'String', 'read')")
	@PostAuthorize("hasPermission(returnObject, 'read')")
	public MyResult protectedWithPermissionEvaluator(
			@RequestParam(value="name") String name) {
		MyResult res = new MyResult();
		res.setName(name);
		return res;
	}
}
