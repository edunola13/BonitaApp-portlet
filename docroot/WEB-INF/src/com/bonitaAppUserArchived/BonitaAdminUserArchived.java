package com.bonitaAppUserArchived;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import bonitaClass.Case;
import bonitaClass.Task;

import com.BonitaAppBeans.BonitaAdministration;
import com.BonitaAppBeans.BonitaConfig;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;

@Controller("UserArchived")
@RequestMapping(value = "VIEW")
public class BonitaAdminUserArchived {
	//@Autowired
	public BonitaConfig config;
	//@Autowired
	public BonitaAdministration administration;
	
	@RenderMapping()
	public String showView(RenderRequest renderRequest,RenderResponse renderResponse) throws Exception {	
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(renderRequest)));
		
		String vista= "viewTasks-userArchived";
		if(this.administration.bonitaApi(renderRequest.getPortletSession()) == null){
			return "error-no-login";
		}else if(!this.administration.bonitaApi(renderRequest.getPortletSession()).getCorrectLogin()){
			PortletURL updateDataActionUrl= renderResponse.createActionURL();
			updateDataActionUrl.setParameter(ActionRequest.ACTION_NAME,"updateData");
			renderRequest.setAttribute("updateDataActionUrl", updateDataActionUrl);
			return "error-login";
		}else{
			String action= renderRequest.getParameter("action");
			if(action == null)action="tasks";
			
			bonitaClass.User user= this.administration.bonitaApi(renderRequest.getPortletSession()).actualUser();
			
			if(action.equals("cases")){
				List<Case> cases= this.administration.bonitaApi(renderRequest.getPortletSession()).archivedCases(user.getId(), 0, 100000);
				renderRequest.setAttribute("cases", cases);
				vista= "viewCases-userArchived";
			}else{
				List<Task> tasks= this.administration.bonitaApi(renderRequest.getPortletSession()).archivedHumanTask(user.getId(), 0, 1000000);
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
		return "userArchived/" + vista;
	}
	
	@ActionMapping(value="updateData")
	public void updateData(ActionRequest request, ActionResponse response) throws PortalException, SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		this.administration.updateData(request);
		
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

}
