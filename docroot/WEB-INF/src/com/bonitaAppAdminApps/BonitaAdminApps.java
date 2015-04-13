package com.bonitaAppAdminApps;

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

import bonitaApi.BonitaApi;
import bonitaClass.Case;
import bonitaClass.Task;

import com.BonitaAppBeans.BonitaConfig;
import com.liferay.portal.kernel.util.WebKeys;

@Controller("AdminApps")
@RequestMapping(value = "VIEW")
public class BonitaAdminApps {
	@Autowired
	public BonitaConfig config;
	
	@RenderMapping()
	public String showView(RenderRequest renderRequest,RenderResponse renderResponse) throws Exception {	
		String vista= "viewTasks-adminApps";
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
			if(action == null)action="tasks";
			
			if(action.equals("cases")){
				List<Case> cases= this.getBonita(renderRequest.getPortletSession()).cases(0,10000);
				renderRequest.setAttribute("cases", cases);
				vista= "viewCases-adminApps";
			}else if (action.equals("processes")) {
				List<bonitaClass.Process> processes= this.getBonita(renderRequest.getPortletSession()).deployedProcesses();
				renderRequest.setAttribute("processes", processes);
				vista= "viewProcesses-adminApps";
			}else{
				List<Task> tasks= this.getBonita(renderRequest.getPortletSession()).tasks(0,10000);
				renderRequest.setAttribute("tasks", tasks);
				vista= "viewTasks-adminApps";			
			}		
			
			PortletURL processesActionUrl = renderResponse.createActionURL();
			processesActionUrl.setParameter(ActionRequest.ACTION_NAME,"processes");
			PortletURL casesActionUrl= renderResponse.createActionURL();
			casesActionUrl.setParameter(ActionRequest.ACTION_NAME,"cases");
			PortletURL tasksActionUrl= renderResponse.createActionURL();
			tasksActionUrl.setParameter(ActionRequest.ACTION_NAME,"tasks");
			renderRequest.setAttribute("processesActionUrl", processesActionUrl);
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
	
	@ActionMapping(value="processes")
	public void process(ActionRequest request, ActionResponse response){		
		response.setRenderParameter("action", "processes");
	}
	
	@ActionMapping(value="cases")
	public void cases(ActionRequest request, ActionResponse response){
		response.setRenderParameter("action", "cases");
	}
	
	@ActionMapping(value="tasks")
	public void tasks(ActionRequest request, ActionResponse response){
		response.setRenderParameter("action", "tasks");
	}
	
	/**
	 * Formulario para asignar tarea
	 */
	@ResourceMapping(value="assignTask")
	public String assignTask(ResourceRequest request,ResourceResponse response, @RequestParam long taskId){
		request.setAttribute("task", this.getBonita(request.getPortletSession()).task(taskId));
		request.setAttribute("users", this.getBonita(request.getPortletSession()).users(0, 100000));
		return "assignTask-admin";
	}
	
	/**
	 * Asignar Tarea
	 */
	@ActionMapping(value="assignId")
	public void assignId(ActionRequest request, ActionResponse response){
		Boolean exito= this.getBonita(request.getPortletSession()).assignTask(Long.parseLong(request.getParameter("taskId")), Long.parseLong(request.getParameter("userId")));
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "tasks");
	}
	
	/**
	 * Liberar Tarea
	 */
	@ActionMapping(value="releaseId")
	public void releaseId(ActionRequest request, ActionResponse response, @RequestParam long taskId){
		Boolean exito= this.getBonita(request.getPortletSession()).releaseTask(taskId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "tasks");
	}
	
	/**
	 * Eliminar Case
	 */
	@ActionMapping(value="deleteCase")
	public void deleteCase(ActionRequest request, ActionResponse response, @RequestParam long caseId){
		Boolean exito= this.getBonita(request.getPortletSession()).deleteCase(caseId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "cases");
	}
	
	/**
	 * Deshabilitar Process
	 */
	@ActionMapping(value="disableProcess")
	public void disableProcess(ActionRequest request, ActionResponse response, @RequestParam long processId){
		Boolean exito= this.getBonita(request.getPortletSession()).disableProcess(processId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "processes");
	}
	
	/**
	 * Habilitar Process
	 */
	@ActionMapping(value="enableProcess")
	public void enableProcess(ActionRequest request, ActionResponse response, @RequestParam long processId){
		Boolean exito= this.getBonita(request.getPortletSession()).enableProcess(processId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "processes");
	}
	
	/**
	 * Eliminar Process
	 */
	@ActionMapping(value="deleteProcess")
	public void deleteProcess(ActionRequest request, ActionResponse response, @RequestParam long processId){
		Boolean exito= this.getBonita(request.getPortletSession()).deleteProcess(processId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "processes");
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
		Boolean isAdmin= false;
		if(portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE) != null){
			isAdmin= (Boolean) portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE);
		}else{
			BonitaApi bonita= this.getBonita(portletSession);
			isAdmin= bonita.hasProfile(bonita.actualUser().getId(), this.config.getAdminProfile());
			portletSession.setAttribute("BONITA_API_ADMIN", isAdmin,PortletSession.APPLICATION_SCOPE);
		}
		return isAdmin;
	}
}
