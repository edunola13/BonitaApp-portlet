package com.bonitaAppAdminOrg;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import bonitaClass.Group;
import bonitaClass.Role;
import bonitaClass.User;

import com.bonitaAppBeans.AppUtils;
import com.bonitaAppBeans.BonitaAdministration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.util.PortalUtil;

@Controller("AdminOrg")
@RequestMapping(value = "VIEW")
public class BonitaAdminOrg {
	//@Autowired
	//public BonitaConfig config;
	//@Autowired
	public BonitaAdministration administration;
	@Autowired
	public AppUtils utils;
	
	@RenderMapping()
	public String showView(RenderRequest renderRequest,RenderResponse renderResponse) throws Exception {
		//Url Parameters
		String bosAction = utils.getUrlParameter(renderRequest, "bosAction");
		String bosSearch = utils.getUrlParameter(renderRequest, "bosSearch");
		//Inicializacion de Bonita
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(renderRequest)));
		
		String vista= "viewUsers-adminOrg";
		if(this.administration.bonitaApi(renderRequest.getPortletSession()) == null){
			return "error-no-login";
		}else if(!this.administration.bonitaApi(renderRequest.getPortletSession()).getCorrectLogin()){
			PortletURL updateDataActionUrl= renderResponse.createActionURL();
			updateDataActionUrl.setParameter(ActionRequest.ACTION_NAME,"updateData");
			renderRequest.setAttribute("updateDataActionUrl", updateDataActionUrl);
			return "error-login";
		}else if(! this.administration.isAdmin(renderRequest.getPortletSession())){
			return "error-login-admin";
		}else{
			String action= renderRequest.getParameter("action");
			if(bosAction != null){
				action= bosAction;
			}else if(action == null){
				action= "users";
			}
			
			if(action.equals("groups")){
				List<Group> groups= this.administration.bonitaApi(renderRequest.getPortletSession()).groups(0, 100);
				renderRequest.setAttribute("groups", groups);
				vista= "viewGroups-adminOrg";
			}else if (action.equals("roles")) {
				List<Role> roles= this.administration.bonitaApi(renderRequest.getPortletSession()).roles(0, 100);
				renderRequest.setAttribute("roles", roles);
				vista= "viewRoles-adminOrg";
			}else{
				List<User> users= this.administration.bonitaApi(renderRequest.getPortletSession()).users(0, 100000);
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
			renderRequest.setAttribute("bosSearch", bosSearch);
			renderRequest.setAttribute("section", action);
		}
		return "adminOrg/" + vista;
	}
	
	@ActionMapping(value="updateData")
	public void updateData(ActionRequest request, ActionResponse response) throws PortalException, SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		this.administration.updateData(request);
		
		request.getPortletSession().setAttribute("BONITA_API_PORT", null, PortletSession.APPLICATION_SCOPE);
		
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "users");
	}
	
	@ActionMapping(value="users")
	public void users(ActionRequest request, ActionResponse response){
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "users");
	}
	
	@ActionMapping(value="groups")
	public void groups(ActionRequest request, ActionResponse response){
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "groups");
	}
	
	@ActionMapping(value="roles")
	public void roles(ActionRequest request, ActionResponse response){
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "roles");
	}
	
	/**
	 * Desactivar Usuario
	 * @throws SystemException 
	 */
	@ActionMapping(value="deactivateUser")
	public void deactivateUser(ActionRequest request, ActionResponse response, @RequestParam long userId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).deactivateUser(userId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "users");
	}
	
	/**
	 * Activar Usuario
	 * @throws SystemException 
	 */
	@ActionMapping(value="activateUser")
	public void activateUser(ActionRequest request, ActionResponse response, @RequestParam long userId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).activeUser(userId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "users");
	}
		
	/**
	 * Formulario para ver y asignar nueva membresia
	 * @throws SystemException 
	 */
	@ResourceMapping(value="memberships")
	public String memberships(ResourceRequest request,ResourceResponse response, @RequestParam String userName) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		User user= this.administration.bonitaApi(request.getPortletSession()).user(userName);
		request.setAttribute("user", user);
		request.setAttribute("roles", this.administration.bonitaApi(request.getPortletSession()).roles(0, 100000));
		request.setAttribute("groups", this.administration.bonitaApi(request.getPortletSession()).groups(0, 100000));
		request.setAttribute("memberships", this.administration.bonitaApi(request.getPortletSession()).memberships(user.getId(), 0, 100000));
		return "adminOrg/assignMembership";
	}
	
	/**
	 * Asignar Membresia
	 * @throws SystemException 
	 */
	@ActionMapping(value="assignMembership")
	public void assignMembership(ActionRequest request, ActionResponse response) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).addMembership(Long.parseLong(request.getParameter("userId")), 
				Long.parseLong(request.getParameter("roleId")), Long.parseLong(request.getParameter("groupId")));
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "users");
	}
	
	/**
	 * Formulario Group
	 * @throws SystemException 
	 */
	@ResourceMapping(value="formGroup")
	public String formGroup(ResourceRequest request,ResourceResponse response, @RequestParam long groupId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Group group= new Group();
		if(groupId != 0){
			group= this.administration.bonitaApi(request.getPortletSession()).group(groupId);
		}		
		request.setAttribute("group", group);
		
		return "adminOrg/formGroup";
	}
	
	/**
	 * Add Group
	 * @throws SystemException 
	 */
	@ActionMapping(value="addGroup")
	public void addGroup(ActionRequest request, ActionResponse response) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Group group= this.administration.bonitaApi(request.getPortletSession()).createGroup(request.getParameter("name"), 
				request.getParameter("displayName"), request.getParameter("description"));
		if(group == null){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "groups");
	}
	
	/**
	 * Update Group
	 * @throws SystemException 
	 */
	@ActionMapping(value="updateGroup")
	public void updateGroup(ActionRequest request, ActionResponse response) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).updateGroup(Long.parseLong(request.getParameter("groupId")),
				request.getParameter("name"), request.getParameter("displayName"), request.getParameter("description"));
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "groups");
	}
	
	/**
	 * Update Group
	 * @throws SystemException 
	 */
	@ActionMapping(value="deleteGroup")
	public void deleteGroup(ActionRequest request, ActionResponse response, @RequestParam long groupId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).deleteGroup(groupId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "groups");
	}
	
	/**
	 * Formulario Role
	 * @throws SystemException 
	 */
	@ResourceMapping(value="formRole")
	public String formRole(ResourceRequest request,ResourceResponse response, @RequestParam long roleId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Role role= new Role();
		if(roleId != 0){
			role= this.administration.bonitaApi(request.getPortletSession()).role(roleId);
		}		
		request.setAttribute("role", role);
		
		return "adminOrg/formRole";
	}
	
	/**
	 * Add Role
	 * @throws SystemException 
	 */
	@ActionMapping(value="addRole")
	public void addRole(ActionRequest request, ActionResponse response) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Role role= this.administration.bonitaApi(request.getPortletSession()).createRole(request.getParameter("name"), 
				request.getParameter("displayName"), request.getParameter("description"));
		if(role == null){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "roles");
	}
	
	/**
	 * Update Role
	 * @throws SystemException 
	 */
	@ActionMapping(value="updateRole")
	public void updateRole(ActionRequest request, ActionResponse response) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		System.out.println(request.getParameter("roleId"));
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).updateRole(Long.parseLong(request.getParameter("roleId")),
				request.getParameter("name"), request.getParameter("displayName"), request.getParameter("description"));
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "roles");
	}
	
	/**
	 * Update Role
	 * @throws SystemException 
	 */
	@ActionMapping(value="deleteRole")
	public void deleteRole(ActionRequest request, ActionResponse response, @RequestParam long roleId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).deleteRole(roleId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "roles");
	}
}
