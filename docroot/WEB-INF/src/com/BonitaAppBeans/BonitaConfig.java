package com.BonitaAppBeans;

import org.springframework.stereotype.Service;

@Service
public class BonitaConfig {
	private String serverUrl= "http://localhost:8080/bonita/";
	private String userAdmin= "admin";
	private String passAdmin= "";
	private String defaultGroup= "";
	private String defaultRole= "";
	private String liferayGroups= "";

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
	
	public String[] getLiferayGroups() {
		return this.liferayGroups.split(",");
	}

	public void setLiferayGroups(String groups) {
		this.liferayGroups = groups;
	}	
}
