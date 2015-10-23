package com.bonitaAppTask;

import java.io.IOException;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.EventMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.bonitaAppBeans.BonitaAdministration;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;

import bonitaClass.Task;

@Controller("TaskController")
@RequestMapping(value = "VIEW")
public class BonitaTaskController {
	//@Autowired
	//public BonitaConfig config;
	//@Autowired
	public BonitaAdministration administration;
	
	@RenderMapping()
	public String view(RenderRequest request, RenderResponse response) throws PortletException, IOException, SystemException {
		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)));
		
		long userId= 0;
		if(request.getParameter("userId") != null)userId= Long.parseLong(request.getParameter("userId"));
		long taskId= 0;
		if(request.getParameter("taskId") != null)taskId= Long.parseLong(request.getParameter("taskId"));
		
		if(userId != 0){
			Task task= this.administration.bonitaApi(request.getPortletSession()).task(taskId);
			if(task != null && task.getState().equals("ready")){
				request.setAttribute("estado", true);
				request.setAttribute("task", task);
				
				//String url= "http://localhost:8080/bonita?ui=form#form="+ task.getProcess().getName() +"--6.0--"+ task.getName() +"$entry&amp;task="+ Long.toString(task.getId()) +"&amp;mode=form&locale=default";
				String url= this.administration.getConfig().getServerUrl() + "loginservice?redirectUrl=/bonita/portal/homepage?ui=form&amp;ui=form&amp;locale=default#form="+ task.getProcess().getName() +"--"+task.getProcess().getVersion()+"--"+ task.getName() +"$entry&amp;task="+ Long.toString(task.getId()) +"&amp;mode=form";
				request.setAttribute("url", url);
				
				String password= (String) (request.getPortletSession().getAttribute("BONITA_APP_USER_PASS" ,PortletSession.APPLICATION_SCOPE));
				String username= (String) (request.getPortletSession().getAttribute("BONITA_APP_USER_NAME" ,PortletSession.APPLICATION_SCOPE));
				request.setAttribute("password", password);
				request.setAttribute("userName", username);
			}else{
				request.setAttribute("estado", false);
			}
			request.setAttribute("realizando", true);
		}else{
			request.setAttribute("realizando", false);
		}
		return "formTask";
	}
	
	@EventMapping(value="doTask")
	public void doTask(EventRequest eventRequest, EventResponse eventResponse){
	    javax.portlet.Event event = eventRequest.getEvent();
	    String value = (String) event.getValue();
	    //corto por el "-" el value, la primer parte userId segunda parte taskId
	    String[] values= value.split("-");	    
	    
	    eventResponse.setRenderParameter("userId", values[0]);
	    eventResponse.setRenderParameter("taskId", values[1]);
	}
}
