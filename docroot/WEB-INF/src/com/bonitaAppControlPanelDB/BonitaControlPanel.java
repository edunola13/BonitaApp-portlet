package com.bonitaAppControlPanelDB;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
			
			List<String[]> processes= this.procesos();
			renderRequest.setAttribute("processes", processes);
			
			PortletURL casesActionUrl= renderResponse.createActionURL();
			casesActionUrl.setParameter(ActionRequest.ACTION_NAME,"cases");
			PortletURL archivedCasesActionUrl= renderResponse.createActionURL();
			archivedCasesActionUrl.setParameter(ActionRequest.ACTION_NAME,"archivedCases");
			renderRequest.setAttribute("casesActionUrl", casesActionUrl);
			renderRequest.setAttribute("archivedCasesActionUrlActionUrl", archivedCasesActionUrl);
			if(renderRequest.getAttribute("tipoT") == null){
				renderRequest.setAttribute("tipoT", "");
			}
			if(renderRequest.getAttribute("cases") == null){
				renderRequest.setAttribute("cases", new ArrayList<String[]>());
			}
			
			renderRequest.setAttribute("section", action);			
		}
		return "controlPanelDB/" + vista;
	}
	
	private List<String[]> procesos(){
		try{  
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			//step2 create  the connection object  
			java.sql.Connection con= DriverManager.getConnection("jdbc:oracle:thin:@10.36.0.17:1521:XE","MUNICIPIO","123456");
//			java.sql.Connection con= DriverManager.getConnection("jdbc:oracle:thin:@10.1.1.3:1521:DESA","MUNICIPIO","MUNICIPIO"); 
			  
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			  
			//step4 execute query  
			ResultSet rs=stmt.executeQuery("select COD_TIPO_TRAMITE, DESCRIPCION from TR_TIPOS_TRAMITE");
			List<String[]> tipos= new ArrayList<String[]>();			
			while(rs.next()){
				String[] elems= {rs.getString(1), rs.getString(2)};
				tipos.add(elems);
			}
			
			//step5 close the connection object  
			con.close();
			
			return tipos;
		}catch(Exception e){
			System.out.println(e);
			return new ArrayList<String[]>();			
		}  
	}
	
	private List<String[]> instancias(String tipoT){
		try{  
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			//step2 create  the connection object  
			java.sql.Connection con= DriverManager.getConnection("jdbc:oracle:thin:@10.36.0.17:1521:XE","MUNICIPIO","123456");
//			java.sql.Connection con= DriverManager.getConnection("jdbc:oracle:thin:@10.1.1.3:1521:DESA","MUNICIPIO","MUNICIPIO"); 
			  
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			  
			//step4 execute query  
			ResultSet rs=stmt.executeQuery("select t.ID_PROCESO, t.ID_TRAMITE, p.NRO_PARTIDA, t.NOMBRE from TR_TRAMITES t left join RE_PARTIDAS p on t.ID_PARTIDA = p.ID_PARTIDA where COD_TIPO_TRAMITE = '" + tipoT + "'");
			List<String[]> tipos= new ArrayList<String[]>();			
			while(rs.next()){
				String[] elems= {rs.getString(1), rs.getString(2), rs.getString(2), rs.getString(4)};
				tipos.add(elems);
			} 
			
			//step5 close the connection object  
			con.close();
			
			return tipos;
		}catch(Exception e){
			System.out.println(e);
			return new ArrayList<String[]>();			
		}  
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
	
	
	@ActionMapping(value="actualizeCases")
	public void actualizeCases(ActionRequest request, ActionResponse response, @RequestParam String tipoT) throws SystemException{
		
		List<String[]> cases= this.instancias(tipoT);
		
		request.setAttribute("cases", cases);
		request.setAttribute("tipoT", tipoT);
	}
	
//	@ActionMapping(value="actualizeArchivedCases")
//	public void actualizeArchivedCases(ActionRequest request, ActionResponse response, @RequestParam long processId) throws SystemException{
//		this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
//				
//		List<Case> cases= this.administration.bonitaApi().archivedCasesProcess(processId, true, "");
//		request.setAttribute("cases", cases);
//		request.setAttribute("processId", processId);
//	}	
	
	
	
	@ResourceMapping(value="actualizePanel")
	public String actualizePanel(ResourceRequest request,ResourceResponse response, @RequestParam long caseId) throws SystemException{				
		if(caseId != 0){
			this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
			
			Case caso= this.administration.bonitaApi().caseById(20008L);
			BonitaApi bonitaAdmin= this.administration.bonitaApiLikeAdmin();
			
			if(caso != null){			
				List<Task> tasks= bonitaAdmin.tasksCase(caseId, "");
				List<Task> archivedTasks= bonitaAdmin.archivedTaskCase(caseId, true, "");				
				request.setAttribute("tasks", tasks);
				request.setAttribute("archivedTasks", archivedTasks);
				
			}else{
				caso= this.administration.bonitaApi().archivedCaseById(20008L);
				if(caso != null){
					List<Task> archivedTasks= bonitaAdmin.archivedTaskCase(caso.getRootCaseId(), true, "");
					request.setAttribute("archivedTasks", archivedTasks);
				}else{
					request.setAttribute("estado", "terminado");
				}
			}
			request.setAttribute("case", caso);			
		}
		
		return "controlPanelDB/viewTasks-controlPanel";
	}
	
//	@ResourceMapping(value="actualizeArchivedPanel")
//	public String actualizeArchivedPanel(ResourceRequest request,ResourceResponse response, @RequestParam long caseId) throws SystemException{				
//		if(caseId != 0){
//			this.administration= new BonitaAdministration((PortalUtil.getHttpServletRequest(request)), request.getPortletSession());
//			
//			Case caso= this.administration.bonitaApi().archivedCaseById(caseId);
//			BonitaApi bonitaAdmin= this.administration.bonitaApiLikeAdmin();
//			List<Task> archivedTasks= bonitaAdmin.archivedTaskCase(caso.getRootCaseId(), true, "");
//			request.setAttribute("case", caso);
//			request.setAttribute("archivedTasks", archivedTasks);
//		}
//			
//		return "controlPanelDB/viewTasks-controlPanel";
//	}
}
