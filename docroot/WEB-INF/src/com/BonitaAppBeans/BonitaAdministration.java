package com.bonitaAppBeans;

import javax.portlet.ActionRequest;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

import bonitaApi.BonitaApi;

public class BonitaAdministration {
	public static String[] especialCharacters= {"+","-","/","%","*","!","?","#","\\","|"};
	
	private BonitaConfig config;
	
	public BonitaAdministration(HttpServletRequest request) throws SystemException{
		this.config= new BonitaConfig(PortalUtil.getCompanyId(request));
	}
	
	public BonitaApi bonitaApi(PortletSession portletSession){
		BonitaApi bonita= null;
		if(portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE) != null){
			if(portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE) == null){
				System.out.println("## Creando Bonita Api para Sesion");
				String userName= (String) (portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE));
				String password= (String) (portletSession.getAttribute("BONITA_APP_USER_PASS" ,PortletSession.APPLICATION_SCOPE));						
				bonita= new BonitaApi(this.config.getVersion(), this.config.getServerUrl(), userName, password);
				portletSession.setAttribute("BONITA_API_PORT", bonita, PortletSession.APPLICATION_SCOPE);
			}else{
				bonita= (BonitaApi) portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE);
			}
		}
		return bonita;
	}

	public Boolean isAdmin(PortletSession portletSession){
		Boolean isAdmin= false;
		if(portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE) != null){
			isAdmin= (Boolean) portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE);
		}else{
			BonitaApi bonita= this.bonitaApi(portletSession);
			isAdmin= bonita.hasProfile(bonita.actualUser().getId(), this.config.getAdminProfile());
			portletSession.setAttribute("BONITA_API_ADMIN", isAdmin,PortletSession.APPLICATION_SCOPE);
		}
		return isAdmin;
	}
	
	public Boolean updateData(ActionRequest request) throws PortalException, SystemException{
		BonitaApi bon= new BonitaApi(this.config.getVersion(), this.config.getServerUrl(), this.config.getUserAdmin(), this.config.getPassAdmin());
		
		String name= (String)request.getPortletSession().getAttribute("BONITA_APP_USER_NAME",PortletSession.APPLICATION_SCOPE);
		bonitaClass.User user= bon.user(name);
		
		//Consigo los datos actuales de Liferay
		User currentUser= PortalUtil.getUser(request);
		
		//Veo si se encuentra la contraseña en la sesion, si no guardo la encriptada
		String password= (String) request.getPortletSession().getAttribute(WebKeys.USER_PASSWORD, PortletSession.APPLICATION_SCOPE);
		if(password == null){
			//Si la password desencriptada no se encuentra, se setea la password encriptada
			password= BonitaAdministration.adaptEncryptedPassword(currentUser.getPassword());				
		}
		request.getPortletSession().setAttribute("BONITA_APP_USER_PASS", password, PortletSession.APPLICATION_SCOPE);
		
		String username= "";
		if(this.config.getUsername().equals("ScreenName")){
			username= currentUser.getScreenName();					
		}else{
			username= Long.toString(currentUser.getUserId());
		}
		request.getPortletSession().setAttribute("BONITA_APP_USER_NAME", username, PortletSession.APPLICATION_SCOPE);
		
		return bon.updateUser(user.getId(), username, password, password, currentUser.getFirstName(), currentUser.getLastName(), true);
	}
	
	public static String adaptEncryptedPassword(String encryptedPass){
		int cant= especialCharacters.length;
		for(int i=0; i < cant; i++){
			encryptedPass= encryptedPass.replace(especialCharacters[i], "");
		}
		return encryptedPass;
	}
	
	public BonitaConfig getConfig() {
		return config;
	}

	public void setConfig(BonitaConfig config) {
		this.config = config;
	}
}
