package com.BonitaAppBeans;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;

//@Service - Usado cuando se inyectaba
public class BonitaConfig {
	private String version= "6.2";
	private String serverUrl= "http://localhost:8080/bonita/";
	private String userAdmin= "admin";
	private String passAdmin= "";
	private String adminProfile= "";
	private String defaultGroup= "";
	private String defaultRole= "";
	private String liferayGroups= "";
	private String username= "";

	public BonitaConfig(){
		
	}
	
	public BonitaConfig(long companyId) throws SystemException{
		this.version  = PrefsPropsUtil.getString(companyId, "bonita.version");
		this.serverUrl  = PrefsPropsUtil.getString(companyId, "bonita.serverUrl");
		this.userAdmin  = PrefsPropsUtil.getString(companyId, "bonita.userAdmin");
		this.passAdmin  = PrefsPropsUtil.getString(companyId, "bonita.passAdmin");
		this.adminProfile  = PrefsPropsUtil.getString(companyId, "bonita.adminProfile");
		this.defaultGroup  = PrefsPropsUtil.getString(companyId, "bonita.defaultGroup");
		this.defaultRole  = PrefsPropsUtil.getString(companyId, "bonita.defaultRole");
		this.liferayGroups  = PrefsPropsUtil.getString(companyId, "bonita.liferayGroups");
		this.username  = PrefsPropsUtil.getString(companyId, "bonita.username");
	}
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getUserAdmin() {
		return userAdmin;
	}

	public void setUserAdmin(String userAdmin) {
		this.userAdmin = userAdmin;
	}

	public String getPassAdmin() {
		return passAdmin;
	}

	public void setPassAdmin(String passAdmin) {
		this.passAdmin = passAdmin;
	}

	public String getAdminProfile() {
		return adminProfile;
	}

	public void setAdminProfile(String adminProfile) {
		this.adminProfile = adminProfile;
	}

	public String getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(String defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	public String getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}

	public Boolean allLiferayGroups(){
		if(this.liferayGroups.equals("*")){
			return true;
		}else{
			return false;
		}
	}
	
	public String[] getSeparateLiferayGroups() {
		return this.liferayGroups.split(",");
	}

	public String getLiferayGroups() {
		return this.liferayGroups;
	}
	
	public void setLiferayGroups(String groups) {
		this.liferayGroups = groups;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
