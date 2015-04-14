package com.bonitaAppUserArchived;

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
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import bonitaApi.BonitaApi;
import bonitaClass.Case;
import bonitaClass.Task;

import com.BonitaAppBeans.BonitaConfig;
import com.liferay.portal.kernel.util.WebKeys;

@Controller("UserArchived")
@RequestMapping(value = "VIEW")
public class BonitaAdminUserArchived {
	@Autowired
	public BonitaConfig config;
	
	@RenderMapping()
	public String showView(RenderRequest renderRequest,RenderResponse renderResponse) throws Exception {	
		String vista= "viewTasks-userArchived";
		if(this.getBonita(renderRequest.getPortletSession()) == null){
			vista= "error-no-login";
		}else if(!this.getBonita(renderRequest.getPortletSession()).getCorrectLogin()){
			PortletURL updateDataActionUrl= renderResponse.createActionURL();
			updateDataActionUrl.setParameter(ActionRequest.ACTION_NAME,"updateData");
			renderRequest.setAttribute("updateDataActionUrl", updateDataActionUrl);
			vista= "error-login";
		}else{
			String action= renderRequest.getParameter("action");
			if(action == null)action="tasks";
			
			bonitaClass.User user= this.getBonita(renderRequest.getPortletSession()).actualUser();
			
			if(action.equals("cases")){
				List<Case> cases= this.getBonita(renderRequest.getPortletSession()).archivedCases(user.getId(), 0, 100000);
				renderRequest.setAttribute("cases", cases);
				vista= "viewCases-userArchived";
			}else{
				List<Task> tasks= this.getBonita(renderRequest.getPortletSession()).archivedHumanTask(user.getId(), 0, 1000000);
				renderRequest.setAttribute("tasks", tasks);
				vista= "viewTasks-userArchived";			
			}		
			
			PortletURL casesActionUrl= renderResponse.createActionURL();
			casesActionUrl.setParameter(ActionRequest.ACTION_NAME,"cases");
			PortletURL tasksActionUrl= renderResponse.createActionURL();
			tasksActionUrl.setParameter(ActionRequest.ACTION_NAME,"tasks");
			renderRequest.setAttribute("casesActionUrl", casesActionUrl);
			renderRequest.setAttribute("tasksActionUrl", tasksActionUrl);
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
	
	@ActionMapping(value="tasks")
	public void process(ActionRequest request, ActionResponse response){		
		response.setRenderParameter("action", "tasks");
	}
	
	@ActionMapping(value="cases")
	public void cases(ActionRequest request, ActionResponse response){
		response.setRenderParameter("action", "cases");
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
}
