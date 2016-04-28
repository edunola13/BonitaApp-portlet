package com.bonitaAppControlPanel;

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

import com.bonitaAppBeans.AppUtils;
import com.bonitaAppBeans.BonitaAdministration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;

import bonitaApi.BonitaApi;
import bonitaClass.Case;
import bonitaClass.Task;

@Controller("UserController")
@RequestMapping(value = "VIEW")
public class BonitaControlPanel {	
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
//		String bosSearch = utils.getUrlParameter(renderRequest, "bosSearch");
		//Inicializacion de Bonita
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(renderRequest)), renderRequest.getPortletSession());
		
		String vista= "viewCases-controlPanel";
		if(this.administration.bonitaApi() == null){
			return "error-no-login";
		}else if(!this.administration.bonitaApi().getCorrectLogin()){
			PortletURL updateDataActionUrl= renderResponse.createActionURL();
			updateDataActionUrl.setParameter(ActionRequest.ACTION_NAME,"updateData");
			renderRequest.setAttribute("updateDataActionUrl", updateDataActionUrl);
			return "error-login";
		}else{
			String action= renderRequest.getParameter("action");
			if(bosAction != null){
				action= bosAction;
			}else if(action == null){
				action= "cases";
			}
						
			if(action.equals("cases")){
				vista= "viewCases-controlPanel";
			}else {
				vista= "viewCases-controlPanel";
			}	
			
			List<bonitaClass.Process> processes= this.administration.bonitaApi().deployedProcesses();
			renderRequest.setAttribute("processes", processes);
			
			PortletURL casesActionUrl= renderResponse.createActionURL();
			casesActionUrl.setParameter(ActionRequest.ACTION_NAME,"cases");
			PortletURL archivedCasesActionUrl= renderResponse.createActionURL();
			archivedCasesActionUrl.setParameter(ActionRequest.ACTION_NAME,"archivedCases");
			renderRequest.setAttribute("casesActionUrl", casesActionUrl);
			renderRequest.setAttribute("archivedCasesActionUrlActionUrl", archivedCasesActionUrl);
			
			renderRequest.setAttribute("section", action);			
		}
		return "controlPanel/" + vista;
	}
	
	@ActionMapping(value="updateData")
	public void updateData(ActionRequest request, ActionResponse response) throws PortalException, SystemException{	
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
		
		this.administration.updateData(request);
		
		request.getPortletSession().setAttribute("BONITA_API_PORT", null, PortletSession.APPLICATION_SCOPE);
		
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "tasks");
	}
	
	
	@ActionMapping(value="cases")
	public void cases(ActionRequest request, ActionResponse response){
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "cases");
	}
		
	@ActionMapping(value="archivedCases")
	public void archivedCases(ActionRequest request, ActionResponse response){
		request.setAttribute("scrollToPortlet", "true");
		response.setRenderParameter("action", "archivedCases");
	}
	
	
	@ResourceMapping(value="actualizeCases")
	public String actualizeCases(ResourceRequest request,ResourceResponse response, @RequestParam long processId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
				
		List<Case> cases= this.administration.bonitaApi().casesProcess(processId, true, "");
		request.setAttribute("cases", cases);
		
		return "controlPanel/selectCases-controlPanel";
	}
	
	@ResourceMapping(value="actualizeArchivedCases")
	public String actualizeArchivedCases(ResourceRequest request,ResourceResponse response, @RequestParam long processId) throws SystemException{
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
				
		List<Case> cases= this.administration.bonitaApi().archivedCasesProcess(processId, true, "");
		request.setAttribute("cases", cases);
		
		return "controlPanel/selectCases-controlPanel";
	}	
		
	
	@ResourceMapping(value="actualizePanel")
	public String actualizePanel(ResourceRequest request,ResourceResponse response, @RequestParam long caseId) throws SystemException{				
		if(caseId != 0){
			this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
			
			Case caso= this.administration.bonitaApi().caseById(caseId);
			BonitaApi bonitaAdmin= this.administration.bonitaApiLikeAdmin();
			List<Task> tasks= bonitaAdmin.tasksCase(caseId, "");
			List<Task> archivedTasks= bonitaAdmin.archivedTaskCase(caseId, true, "");
			request.setAttribute("case", caso);
			request.setAttribute("tasks", tasks);
			request.setAttribute("archivedTasks", archivedTasks);
		}
		
		return "controlPanel/viewTasks-controlPanel";
	}
	
	@ResourceMapping(value="actualizeArchivedPanel")
	public String actualizeArchivedPanel(ResourceRequest request,ResourceResponse response, @RequestParam long caseId) throws SystemException{				
		if(caseId != 0){
			this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
			
			Case caso= this.administration.bonitaApi().archivedCaseById(caseId);
			BonitaApi bonitaAdmin= this.administration.bonitaApiLikeAdmin();
			List<Task> archivedTasks= bonitaAdmin.archivedTaskCase(caso.getRootCaseId(), true, "");
			request.setAttribute("case", caso);
			request.setAttribute("archivedTasks", archivedTasks);
		}
			
		return "controlPanel/viewTasks-controlPanel";
	}
}
