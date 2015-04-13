package com.bonitaApp.hook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bonitaApi.BonitaApi;
import bonitaClass.Group;
import bonitaClass.Role;

import com.BonitaAppBeans.BonitaConfig;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;


public class LoginActionPost  extends Action {
	private BonitaConfig config;	
	
    public void run(HttpServletRequest req, HttpServletResponse res){
        System.out.println("## Custom Login Bonita App");      	
        this.setBonitaConfig();
        
        try {			
        	User currentUser= PortalUtil.getUser(req);
        	
			Boolean isUserBonita= this.config.allLiferayGroups();
			if(! isUserBonita){
				for(com.liferay.portal.model.UserGroup grupo : currentUser.getUserGroups()){
					if(Arrays.asList(this.config.getSeparateLiferayGroups()).contains(grupo.getName())){
						isUserBonita= true;
						break;
					}
				}
			}
			
			if(isUserBonita){
				HttpSession session= req.getSession();
	        	session.getAttribute(WebKeys.USER_PASSWORD);				
				session.setAttribute("BONITA_APP_USER_NAME", currentUser.getScreenName());
				
				BonitaApi bonita= new BonitaApi("http://server200364b:8080/bonita/", this.config.getUserAdmin(), this.config.getPassAdmin());
				
				bonitaClass.User user= bonita.user(currentUser.getScreenName());
				if(user == null){
					user= bonita.createUser(currentUser.getScreenName(), (String)session.getAttribute(WebKeys.USER_PASSWORD), (String)session.getAttribute(WebKeys.USER_PASSWORD), currentUser.getFirstName(), currentUser.getLastName());
					Group group= bonita.group(this.config.getDefaultGroup());
					Role role= bonita.role(this.config.getDefaultRole());
					bonita.addMembership(user.getId(), role.getId(), group.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void setBonitaConfig(){
    	//load a properties file
    	try {
    		Properties prop = new Properties();
    	   	InputStream input = null;
    	    	
    	   	String filename = "bonitaConfig.properties";
    	   	input = getClass().getClassLoader().getResourceAsStream(filename);
    		if (input == null) {
    			System.out.println("Sorry, unable to find " + filename);
    			throw new IOException("El archivo no existe");
    		}
    		
    		//load a properties file from class path, inside static method    			
			prop.load(input);
			
			this.config= new BonitaConfig();
			this.config.setServerUrl(prop.getProperty("serverUrl"));
			this.config.setUserAdmin(prop.getProperty("userAdmin"));
			this.config.setPassAdmin(prop.getProperty("passAdmin"));
			this.config.setAdminProfile(prop.getProperty("adminProfile"));
			this.config.setDefaultGroup(prop.getProperty("defaultGroup"));
			this.config.setDefaultRole(prop.getProperty("defaultRole"));
			this.config.setLiferayGroups(prop.getProperty("liferayGroups"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}    
    }
}
