package com.bonitaApp;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.BonitaAppBeans.BonitaAdministration;
import com.BonitaAppBeans.BonitaConfig;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import bonitaClass.Case;
import bonitaClass.Task;

@Controller("UserController")
@RequestMapping(value = "VIEW")
public class BonitaUserController {	
	@Autowired
	public BonitaConfig config;
	@Autowired
	public BonitaAdministration administration;
	
	@RenderMapping()
	public String showView(RenderRequest renderRequest,RenderResponse renderResponse) throws Exception {	
		String vista= "viewTasks-user";
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
				List<Case> cases= this.administration.bonitaApi(renderRequest.getPortletSession()).cases(user.getId(),0,100);
				renderRequest.setAttribute("cases", cases);
				vista= "viewCases-user";
			}else if (action.equals("processes")) {
				List<bonitaClass.Process> processes= this.administration.bonitaApi(renderRequest.getPortletSession()).deployedProccessForUser(user.getId());
				renderRequest.setAttribute("processes", processes);
				vista= "viewProcesses-user";
			}else{
				List<Task> tasks= this.administration.bonitaApi(renderRequest.getPortletSession()).tasks(user.getId(),0,100);
				renderRequest.setAttribute("tasks", tasks);
				vista= "viewTasks-user";			
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
		return "userApp/" + vista;
	}
	
	@ActionMapping(value="updateData")
	public void updateData(ActionRequest request, ActionResponse response) throws PortalException, SystemException{	
		/*BonitaApi bon= new BonitaApi(this.config.getServerUrl(), this.config.getUserAdmin(), this.config.getPassAdmin());
		String name= (String)request.getPortletSession().getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE);
		String pass= (String)request.getPortletSession().getAttribute("BONITA_APP_USER_PASS" ,PortletSession.APPLICATION_SCOPE);
		bonitaClass.User user= bon.user(name);
		bon.updateUser(user.getId(), name, pass, pass, user.getFirstName(), user.getLastName(), true);*/
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
	 * Ver que crea con Juan ya que asi esta en BonitaApi
	 * cambiar bonita api, que cree el que esta logueado y crear otro metodo que se le pase el id q crea y entonces modificamos el case
	 * una vez creado
	 * @param request
	 * @param response
	 * @param processId
	 */
	@ActionMapping(value="startCase")
	public void startCase(ActionRequest request, ActionResponse response, @RequestParam long processId){		
		Case caso= this.administration.bonitaApi(request.getPortletSession()).startCase(processId);
		if(caso == null){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "cases");
	}
	
	/**
	 * Asignar Tarea
	 */
	@ActionMapping(value="assignId")
	public void assignId(ActionRequest request, ActionResponse response, @RequestParam long taskId){
		bonitaClass.User user= this.administration.bonitaApi(request.getPortletSession()).actualUser();
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).assignTask(taskId, user.getId());
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
		Boolean exito= this.administration.bonitaApi(request.getPortletSession()).releaseTask(taskId);
		if(!exito){
			response.setRenderParameter("rtaAction", "error");
		}else{
			response.setRenderParameter("rtaAction", "success");
		}
		response.setRenderParameter("action", "tasks");
	}
		
	/*
	 * Dos maneras de abrir el formulario de la tarea
	 * Mediante un evento que lee otro portlet o levantando un recurso que se carga en una ventana emergente
	 * El primer metodo es el action que levanta el evento
	 * El segundo metodo es un recurso
	 */
	@ActionMapping(value="doTask")
	public void doTask(ActionRequest request, ActionResponse response, @RequestParam long taskId){	
		bonitaClass.User user= this.administration.bonitaApi(request.getPortletSession()).actualUser();
		
		QName qName= new QName("http://BontaAppLiferay.com", "doTask", "x");
		response.setEvent(qName, Long.toString(user.getId()) + "-" + Long.toString(taskId));
		
		response.setRenderParameter("action", "tasks");
	}
	
	@ResourceMapping(value="doTaskAjax")
	public String doTaskAjax(ResourceRequest request,ResourceResponse response, @RequestParam long taskId){
		
		Task task= this.administration.bonitaApi(request.getPortletSession()).task(taskId);
		if(task != null && task.getState().equals("ready")){
			request.setAttribute("estado", true);
			request.setAttribute("task", task);
			
			//String url= his.config.getServerUrl() + "?ui=form#form="+ task.getProcess().getName() +"--6.0--"+ task.getName() +"$entry&amp;task="+ Long.toString(task.getId()) +"&amp;mode=form&locale=default";
			String url= this.config.getServerUrl() + "/loginservice?redirectUrl=/bonita/portal/homepage?ui=form&amp;ui=form&amp;locale=default#form="+ task.getProcess().getName() +"--"+task.getProcess().getVersion()+"--"+ task.getName() +"$entry&amp;task="+ Long.toString(task.getId()) +"&amp;mode=form";
			request.setAttribute("url", url);
			
			String password= (String) (request.getPortletSession().getAttribute("BONITA_APP_USER_PASS" ,PortletSession.APPLICATION_SCOPE));
			String username= (String) (request.getPortletSession().getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE));
			request.setAttribute("password", password);
			request.setAttribute("userName", username);
		}else{
			request.setAttribute("estado", false);
		}
		
		return "userApp/formTask-Ajax";
	}
	
	/*private BonitaApi getBonita(PortletSession portletSession){		
		BonitaApi bonita= null;
		if(portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE) != null){
			if(portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE) == null){
				String userName= (String) (portletSession.getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE));
				String password= (String) (portletSession.getAttribute("BONITA_APP_USER_PASS" ,PortletSession.APPLICATION_SCOPE));						
				bonita= new BonitaApi(this.config.getServerUrl(), userName, password);
				portletSession.setAttribute("BONITA_API_PORT", bonita, PortletSession.APPLICATION_SCOPE);
			}else{
				bonita= (BonitaApi) portletSession.getAttribute("BONITA_API_PORT", PortletSession.APPLICATION_SCOPE);
			}
		}
		return bonita;
	}*/
}
