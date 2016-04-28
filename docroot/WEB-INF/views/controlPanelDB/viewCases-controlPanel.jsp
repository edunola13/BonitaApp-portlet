<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Process" %>
<%@ page import="bonitaClass.Case" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.ResultSet" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
String section= (String)renderRequest.getAttribute("section");
List<String[]> processes= (List<String[]>)renderRequest.getAttribute("processes");
String tipoT= (String)renderRequest.getAttribute("tipoT");
List<String[]> cases= (List<String[]>)renderRequest.getAttribute("cases");
%>

<div id="bonita-controlPanelDB">
<jsp:include page="../../view-sections/header-controlPanelDB.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<div id="selects-controlPanelDB" style="min-height: 30px;">
	<div class="span6 select-process">
		<select id="bpmPanel-selectProcessDB" class="selectpicker span12" data-live-search="true" onchange="changeProcess()">
			<option value="0">Seleccione un Proceso</option>
			<%for(String[] process : processes) { 
				if(tipoT.equals(process[0])){%>
					<option value="<%= process[0]%>" selected="selected"><%= process[1]%></option>
				<%}else{ %>					
		  			<option value="<%= process[0]%>"><%= process[1]%></option>
		  		<%} %>		  			
		  	<%} %>
		</select>
	</div>
	<div class="span6 select-case">
		<select id="bpmPanel-selectCasesDB" class="selectpicker span12" data-live-search="true" onchange="changeCase()">
			<option value="0">Seleccione un Caso</option>
			<%for(String[] caso : cases) {  %>					
				<%String partida= caso[2] != null ?  " " + caso[2]: ""; %>
				<option value="<%= caso[0] %>"><%= "TR:" + caso[1] + partida + " " + caso[3]  %></option>		  			
			<%} %>
		</select>
	</div>
</div>
<div id="body-controlPanelDB">
	<h4 style="text-align:center; margin-top: 30px;">Seleccione un Proceso y un Caso</h4>
	<div class="case-description" style="padding-top: 20px;">	
	</div>
</div>

<portlet:actionURL var="actualizeCases" name="actualizeCases"></portlet:actionURL>
<portlet:actionURL var="actualizeArchivedCases" name="actualizeArchivedCases"></portlet:actionURL>
<portlet:resourceURL var="actualizePanel" id="actualizePanel"></portlet:resourceURL>
<portlet:resourceURL var="actualizeArchivedPanel" id="actualizeArchivedPanel"></portlet:resourceURL>
<aui:script>
		<%if(section.equals("cases")){ %>
		function changeProcess(){
			var tipoT= $("#selects-controlPanelDB .select-process select").val();
			window.location= "<%= actualizeCases%>&<portlet:namespace/>tipoT=" +tipoT;			
		}
		<% }else{ %>
		function changeProcess(){
			var processId= $("#selects-controlPanelDB .select-process select").val();
			window.location= "<%= actualizeArchivedCases%>&<portlet:namespace/>tipoT=" +tipoT;
		}
		<% } %>
		
		<%if(section.equals("cases")){ %>
		function changeCase(){
			var caseId= $("#selects-controlPanelDB .select-case select").val();
			$.ajax({
				url:"<%= actualizePanel%>",
				data:{
					<portlet:namespace/>caseId:caseId
				},
				success:function(data){			
					$("#body-controlPanelDB .case-description").html("");
					$("#body-controlPanelDB h4").hide();
					$("#body-controlPanelDB .case-description").html(data);
				},
				error: function(){
					alert("error");	
				}				
			});
		}
		<% }else{ %>
		function changeCase(){
			var caseId= $("#selects-controlPanelDB .select-case select").val();
			$.ajax({
				url:"<%= actualizeArchivedPanel%>",
				data:{
					<portlet:namespace/>caseId:caseId
				},
				success:function(data){			
					$("#body-controlPanelDB .case-description").html("");
					$("#body-controlPanelDB h4").hide();
					$("#body-controlPanelDB .case-description").html(data);
				},
				error: function(){
					alert("error");	
				}				
			});
		}
		<% } %>
</aui:script>

<jsp:include page="../../view-sections/footer-controlPanelDB.jsp"></jsp:include>
</div>