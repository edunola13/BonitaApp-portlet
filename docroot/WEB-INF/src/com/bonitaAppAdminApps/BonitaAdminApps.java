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

import bonitaClass.Case;
import bonitaClass.Task;

import com.bonitaAppBeans.AppUtils;
import com.bonitaAppBeans.BonitaAdministration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.util.PortalUtil;

@Controller("AdminApps")
@RequestMapping(value = "VIEW")
public class BonitaAdminApps {
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
		
		String vista= "viewTasks-adminApps";
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
				action= "tasks";
			}
			
			if(action.equals("cases")){
				List<Case> cases= this.administration.bonitaApi(renderRequest.getPortletSession()).cases(0,10000);
				renderRequest.setAttribute("cases", cases);
				vista= "viewCases-adminApps";
			}else if (action.equals("processes")) {
				List<bonitaClass.Process> processes= this.administration.bonitaApi(renderRequest.getPortletSession()).deployedProcesses();
				renderRequest.setAttribute("processes", processes);
				vista= "viewProcesses-adminApps";
			}else{
				List<Task> tasks= this.administration.bonitaApi(renderRequest.getPortletSession()).tasks(0,10000);
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
			renderRequest.setAttribute("bosSearch", bosSearch);
			renderRequest.setAttribute("section", action);
		}
		return "adminApp/" + vista;
	}
	
	@ActionMapping(value="updateData")
	public void updateData(ActionRequest request, ActionResponse response) throws PortalException, SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		this.administration.updateData(request);
		
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
	 * @throws SystemException 
	 */
	@ResourceMapping(value="assignTask")
	public String assignTask(ResourceRequest request,ResourceResponse response, @RequestParam long taskId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		request.setAttribute("task", this.administration.bonitaApi(request.getPortletSession()).task(taskId));
		request.setAttribute("users", this.administration.bonitaApi(request.getPortletSession()).users(0, 100000));
		return "adminApp/assignTask-admin";
	}
	
	/**
	 * Asignar Tarea
	 * @throws SystemException 
	 */
	@ActionMapping(value="assignId")
	public void assignId(ActionRequest request, ActionResponse response) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).assignTask(Long.parseLong(request.getParameter("taskId")), Long.parseLong(request.getParameter("userId")));
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		response.setRenderParameter("action", "tasks");
	}
	
	/**
	 * Liberar Tarea
	 * @throws SystemException 
	 */
	@ActionMapping(value="releaseId")
	public void releaseId(ActionRequest request, ActionResponse response, @RequestParam long taskId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).releaseTask(taskId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		response.setRenderParameter("action", "tasks");
	}
	
	/**
	 * Eliminar Case
	 * @throws SystemException 
	 */
	@ActionMapping(value="deleteCase")
	public void deleteCase(ActionRequest request, ActionResponse response, @RequestParam long caseId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).deleteCase(caseId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		response.setRenderParameter("action", "cases");
	}
	
	/**
	 * Deshabilitar Process
	 * @throws SystemException 
	 */
	@ActionMapping(value="disableProcess")
	public void disableProcess(ActionRequest request, ActionResponse response, @RequestParam long processId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).disableProcess(processId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		response.setRenderParameter("action", "processes");
	}
	
	/**
	 * Habilitar Process
	 * @throws SystemException 
	 */
	@ActionMapping(value="enableProcess")
	public void enableProcess(ActionRequest request, ActionResponse response, @RequestParam long processId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).enableProcess(processId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		response.setRenderParameter("action", "processes");
	}
	
	/**
	 * Eliminar Process
	 * @throws SystemException 
	 */
	@ActionMapping(value="deleteProcess")
	public void deleteProcess(ActionRequest request, ActionResponse response, @RequestParam long processId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).deleteProcess(processId);
		if(!exito){
			SessionErrors.add(request, "error");
		}else{
			SessionMessages.add(request, "success");
		}
		response.setRenderParameter("action", "processes");
	}
	
	/*private Boolean isAdmin(PortletSession portletSession){
		Boolean isAdmin= false;
		if(portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE) != null){
			isAdmin= (Boolean) portletSession.getAttribute("BONITA_API_ADMIN", PortletSession.APPLICATION_SCOPE);
		}else{
			BonitaApi bonita= this.administration.bonitaApi(portletSession);
			isAdmin= bonita.hasProfile(bonita.actualUser().getId(), this.config.getAdminProfile());
			portletSession.setAttribute("BONITA_API_ADMIN", isAdmin,PortletSession.APPLICATION_SCOPE);
		}
		return isAdmin;
	}*/
}
