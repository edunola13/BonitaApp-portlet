package com.bonitaAppAdminOrg;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import bonitaApi.BonitaApi;
import bonitaClass.Group;
import bonitaClass.Role;
import bonitaClass.User;

import com.BonitaAppBeans.BonitaConfig;
import com.liferay.portal.kernel.util.WebKeys;

@Controller("AdminOrg")
@RequestMapping(value = "VIEW")
public class BonitaAdminOrg {
	@Autowired
	public BonitaConfig config;
	
	@RenderMapping()
	public String showView(RenderRequest renderRequest,RenderResponse renderResponse) throws Exception {	
		String vista= "viewUsers-adminOrg";
		if(this.getBonita(renderRequest.getPortletSession()) == null){
			vista= "error-no-login";
		}else if(!this.getBonita(renderRequest.getPortletSession()).getCorrectLogin()){
			PortletURL updateDataActionUrl= renderResponse.createActionURL();
			updateDataActionUrl.setParameter(ActionRequest.ACTION_NAME,"updateData");
			renderRequest.setAttribute("updateDataActionUrl", updateDataActionUrl);
			vista= "error-login";
		}else if(! this.isAdmin(renderRequest.getPortletSession())){
			vista= "error-login-admin";
		}else{
			String action= renderRequest.getParameter("action");
			if(action == null)action="users";
			
			if(action.equals("groups")){
				List<Group> groups= this.getBonita(renderRequest.getPortletSession()).groups(0, 100);
				renderRequest.setAttribute("groups", groups);
				vista= "viewGroups-adminOrg";
			}else if (action.equals("roles")) {
				List<Role> roles= this.getBonita(renderRequest.getPortletSession()).roles(0, 100);
				renderRequest.setAttribute("roles", roles);
				vista= "viewRoles-adminOrg";
			}else{
				List<User> users= this.getBonita(renderRequest.getPortletSession()).users(0, 100000);
				renderRequest.setAttribute("users", users);
				vista= "viewUsers-adminOrg";			
			}		
			
			PortletURL groupsActionUrl = renderResponse.createActionURL();
			groupsActionUrl.setParameter(ActionRequest.ACTION_NAME,"groups");
			PortletURL rolesActionUrl= renderResponse.createActionURL();
			rolesActionUrl.setParameter(ActionRequest.ACTION_NAME,"roles");
			PortletURL usersActionUrl= renderResponse.createActionURL();
			usersActionUrl.setParameter(ActionRequest.ACTION_NAME,"users");
			renderRequest.setAttribute("groupsActionUrl", groupsActionUrl);
			renderRequest.setAttribute("rolesActionUrl", rolesActionUrl);
			renderRequest.setAttribute("usersActionUrl", usersActionUrl);
			//Si anterior al render se ejecuto un action devuelve el resultado
			String rtaAction= renderRequest.getParameter("rtaAction");
			if(rtaAction != null){
				renderRequest.setAttribute("rtaAction", rtaAction);
			}
			//Seteo la seccion actual en base al action
			renderRequest.setAttribute("section", action);
		}
		return vista;
	}
	
	@ActionMapping(value="updateData")
	public void updateData(ActionRequest request, ActionResponse response){
		BonitaApi bon= new BonitaApi(this.config.getServerUrl(), this.config.getUserAdmin(), this.config.getPassAdmin());
		String name= (String)request.getPortletSession().getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE);
		String pass= (String)request.getPortletSession().getAttribute(WebKeys.USER_PASSWORD ,PortletSession.APPLICATION_SCOPE);
		bonitaClass.User user= bon.user(name);
		bon.updateUser(user.getId(), name, pass, pass, user.getFirstName(), user.getLastName(), true);
		
		request.getPortletSession().setAttribute("BONITA_API_PORT", null, PortletSession.APPLICATION_SCOPE);
		
		response.setRenderParameter("action", "tasks");
	}
	
	@ActionMapping(value="users")
	public void users(ActionRequest request, ActionResponse response){		
		response.setRenderParameter("action", "users");
	}
	
	@ActionMapping(value="groups")
	public void groups(ActionRequest request, ActionResponse response){
		response.setRenderParameter("action", "groups");
	}
	
	@ActionMapping(value="roles")
	public void roles(ActionRequest request, ActionResponse response){
		response.setRenderParameter("action", "roles");
	}
	
	/**
	 * Desactivar Usuario
	 */
	@ActionMapping(value="deactivateUser")
	public void deactivateUser(ActionRequest request, ActionResponse response, @RequestParam long userId){
		Boolean exito= this.getBonita(request.getPortletSession()).deactivateUser(userId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "tasks");
	}
	
	/**
	 * Activar Usuario
	 */
	@ActionMapping(value="activateUser")
	public void activateUser(ActionRequest request, ActionResponse response, @RequestParam long userId){
		Boolean exito= this.getBonita(request.getPortletSession()).activeUser(userId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "tasks");
	}
	
	private BonitaApi getBonita(PortletSession portletSession){
		BonitaApi bonita= null;
		if(portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE) != null){
			if(portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE) == null){
				String userName= (String) (portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE));
				String password= (String) (portletSession.getAttribute(WebKeys.USER_PASSWORD ,PortletSession.APPLICATION_SCOPE));						
				bonita= new BonitaApi(this.config.getServerUrl(), userName, password);
				bonita.actualUser();
				portletSession.setAttribute("BONITA_API_PORT", bonita, PortletSession.APPLICATION_SCOPE);
			}else{
				bonita= (BonitaApi) portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE);
			}
		}
		return bonita;
	}
	
	private Boolean isAdmin(PortletSession portletSession){
		return true;
	}
}
