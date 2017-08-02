package com.example.demo.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(
			Authentication auth, Object targetDomainObject, Object permission) {
		if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
			return false;
		}
		System.out.println("Processing " + targetDomainObject + " target with permission: " + permission);
		
		return true;
	}

	@Override
	public boolean hasPermission(
			Authentication auth, Serializable targetId, String targetType, Object permission) {
		if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
			return false;
		}
		
		System.out.println("Processing target id " + targetId + " and type " + targetType + " target with permission: " + permission);
		
		if (auth.getName().equals(targetId))
			return true;
		return false;
	}
}