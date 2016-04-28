<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Process" %>
<%@ page import="bonitaClass.Case" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
String section= (String)renderRequest.getAttribute("section");
List<Process> processes= (List<Process>)renderRequest.getAttribute("processes");
%>

<div id="bonita-controlPanel">
<jsp:include page="../../view-sections/header-controlPanel.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<div id="selects-controlPanel" style="min-height: 30px;">
	<div class="span6 select-process">
		<select id="bpmPanel-selectProcess" class="selectpicker span12" data-live-search="true" onchange="changeProcess()">
			<option value="0">Seleccione un Proceso</option>
			<%for(Process process: processes) { %>
		  		<option value="<%= process.getId()%>"><%= process.getDisplayName()%></option>	  			
		  	<%} %>
		</select>
	</div>
	<div class="span6 select-case">
		<select id="bpmPanel-selectCases" class="span12" onchange="changeCase()">
			<option value="0">Seleccione un Caso</option>
		</select>
	</div>
</div>
<div id="body-controlPanel">
	<h4 style="text-align:center; margin-top: 30px;">Seleccione un Proceso y un Caso</h4>
	<div class="case-description" style="padding-top: 20px;">	
	</div>
</div>

<portlet:resourceURL var="actualizeCases" id="actualizeCases"></portlet:resourceURL>
<portlet:resourceURL var="actualizeArchivedCases" id="actualizeArchivedCases"></portlet:resourceURL>
<portlet:resourceURL var="actualizePanel" id="actualizePanel"></portlet:resourceURL>
<portlet:resourceURL var="actualizeArchivedPanel" id="actualizeArchivedPanel"></portlet:resourceURL>
<aui:script>
		<%if(section.equals("cases")){ %>
		function changeProcess(){
			var processId= $("#selects-controlPanel .select-process select").val();
			$("#body-controlPanel .case-description").html("");
			$("#body-controlPanel h4").show();
			$.ajax({
				url:"<%= actualizeCases%>",
				data:{
					<portlet:namespace/>processId:processId
				},
				success:function(data){			
					$("#selects-controlPanel").find(".select-case").html("");
					$("#selects-controlPanel").find(".select-case").html(data);
				},
				error: function(){
					alert("error");	
				}				
			});
			
		}
		<% }else{ %>
		function changeProcess(){
			var processId= $("#selects-controlPanel .select-process select").val();
			$("#body-controlPanel .case-description").html("");
			$("#body-controlPanel h4").show();
			$.ajax({
				url:"<%= actualizeArchivedCases%>",
				data:{
					<portlet:namespace/>processId:processId
				},
				success:function(data){			
					$("#selects-controlPanel").find(".select-case").html("");
					$("#selects-controlPanel").find(".select-case").html(data);
				},
				error: function(){
					alert("error");	
				}				
			});
		}
		<% } %>
		
		<%if(section.equals("cases")){ %>
		function changeCase(){
			var caseId= $("#selects-controlPanel .select-case select").val();
			$.ajax({
				url:"<%= actualizePanel%>",
				data:{
					<portlet:namespace/>caseId:caseId
				},
				success:function(data){			
					$("#body-controlPanel .case-description").html("");
					$("#body-controlPanel h4").hide();
					$("#body-controlPanel .case-description").html(data);
				},
				error: function(){
					alert("error");	
				}				
			});
		}
		<% }else{ %>
		function changeCase(){
			var caseId= $("#selects-controlPanel .select-case select").val();
			$.ajax({
				url:"<%= actualizeArchivedPanel%>",
				data:{
					<portlet:namespace/>caseId:caseId
				},
				success:function(data){			
					$("#body-controlPanel .case-description").html("");
					$("#body-controlPanel h4").hide();
					$("#body-controlPanel .case-description").html(data);
				},
				error: function(){
					alert("error");	
				}				
			});
		}
		<% } %>
</aui:script>

<jsp:include page="../../view-sections/footer-controlPanel.jsp"></jsp:include>
</div>