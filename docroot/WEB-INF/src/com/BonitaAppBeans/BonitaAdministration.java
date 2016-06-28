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
import bonitaClass.Task;

public class BonitaAdministration {
	public static String[] especialCharacters= {"+","-","/","%","*","!","?","#","\\","|"};
	
	private BonitaConfig config;
	private PortletSession portletSession; 
	
	public BonitaAdministration(HttpServletRequest request, PortletSession portletSession) throws SystemException{
		this.config= new BonitaConfig(PortalUtil.getCompanyId(request));
		this.portletSession= portletSession;
	}
	
	public BonitaApi bonitaApi(){
		BonitaApi bonita= null;
		if(this.portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE) != null){
			if(this.portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE) == null){
				System.out.println("## Creando Bonita Api para Sesion");
				String userName= (String) (this.portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE));
				String password= (String) (this.portletSession.getAttribute("BONITA_APP_USER_PASS" ,PortletSession.APPLICATION_SCOPE));						
				bonita= new BonitaApi(this.config.getVersion(), this.config.getServerUrl(), userName, password);
				this.portletSession.setAttribute("BONITA_API_PORT", bonita, PortletSession.APPLICATION_SCOPE);
			}else{
				bonita= (BonitaApi) this.portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE);
			}
		}
		return bonita;
	}
	
	public BonitaApi bonitaApiLikeAdmin(){
		if(this.isAdmin()){
			return this.bonitaApi();
		}else{
			return new BonitaApi(this.config.getVersion(), this.config.getServerUrl(), this.config.getUserAdmin(), this.config.getPassAdmin());
		}
	}

	public Boolean isAdmin(){
		Boolean isAdmin= false;
		if(this.portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE) != null){
			isAdmin= (Boolean) this.portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE);
		}else{
			BonitaApi bonita= this.bonitaApi();
			isAdmin= bonita.hasProfile(bonita.actualUser().getId(), this.config.getAdminProfile());
			this.portletSession.setAttribute("BONITA_API_ADMIN", isAdmin,PortletSession.APPLICATION_SCOPE);
		}
		return isAdmin;
	}
	
	public String getUrlForm(Task task){
		return this.config.getServerUrl() + "loginservice?redirectUrl=/bonita/portal/homepage?ui=form&amp;ui=form&amp;locale=default#form="+ task.getProcess().getName() +"--"+task.getProcess().getVersion()+"--"+ task.getName() +"$entry&amp;task="+ Long.toString(task.getId()) +"&amp;mode=form";
	}
	
	public Boolean updateData(ActionRequest request) throws PortalException, SystemException{
		BonitaApi bon= new BonitaApi(this.config.getVersion(), this.config.getServerUrl(), this.config.getUserAdmin(), this.config.getPassAdmin());
		
		String name= (String)request.getPortletSession().getAttribute("BONITA_APP_USER_NAME",PortletSession.APPLICATION_SCOPE);
		bonitaClass.User user= bon.user(name);
		
		//Consigo los datos actuales de Liferay
		User currentUser= PortalUtil.getUser(request);
		
		//Veo si se encuentra la contrasena en la sesion, si no guardo la encriptada
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
