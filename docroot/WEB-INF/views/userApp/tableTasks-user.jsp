<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Task" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<Task> tasks= (List<Task>)resourceRequest.getAttribute("tasks");
%>
<table id="tabla-bonita" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="proceso" /></th>
			<th><fmt:message key="caso" /></th>
			<th><fmt:message key="alcanzada"/></th>
			<th><fmt:message key="deadLine" /></th>
			<th><fmt:message key="asignado" /></th>			
			<th><fmt:message key="funciones" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(Task task: tasks){ %>
			<tr>
				<td><%=task.getId() %></td>
				<td><%=task.getDisplayName() %>
				<td><%=task.getProcess().getDisplayName() %></td>
				<td><%=task.getCaseId() %></td>
				<td><%=task.getReachedStateDateString()%></td>
				<td>
				<%if(task.exceededDeadline()){ %>
					<fmt:message key="excedido" /> <%=task.timeExceededDeadline() %>
				<%}else{ %>
					<fmt:message key="resta" /> <%=task.timeToDeadline() %>
				<%} %>
				</td>				
				<%if(task.getAssignedId() == null){%>
					<td>NO</td>
					<td>
						<%if(resourceRequest.getPreferences().getValue("useAssignRelease", "true").equals("true")){ %>
							<portlet:actionURL var="assignId" name="assignId">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>
							</portlet:actionURL>
							<a href="<%= assignId%>" class="btn btn-primary"><fmt:message key="asignar" /></a>
						<%}else{ %>
							<%if(resourceRequest.getPreferences().getValue("doTaskAjax", "true").equals("true")){ %>
								<portlet:resourceURL var="doTaskAjax" id="doTaskAjax">
									<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>						
								</portlet:resourceURL>
								<button class="btn btn-primary" value="Realizar Tarea" onclick="doTask('<%=doTaskAjax %>')"><fmt:message key="realizar" /></button>
							<%}else{ %>
								<portlet:actionURL var="doTask" name="doTask">
									<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>							
								</portlet:actionURL>
								<a href="<%= doTask%>" class="btn btn-primary"><fmt:message key="realizar" /></a>							
							<%} %>
						<%} %>
					</td>
				<%}else{ %>
					<td>SI</td>
					<td>
						<%if(resourceRequest.getPreferences().getValue("useAssignRelease", "true").equals("true")){ %>
							<portlet:actionURL var="releaseId" name="releaseId">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>
							</portlet:actionURL>
							<a href="<%= releaseId%>" class="btn btn-primary"><fmt:message key="liberar" /></a>
						<%} %>
												
						<%if(resourceRequest.getPreferences().getValue("doTaskAjax", "true").equals("true")){ %>
							<portlet:resourceURL var="doTaskAjax2" id="doTaskAjax">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>						
							</portlet:resourceURL>
							<button class="btn btn-primary" value="Realizar Tarea" onclick="doTask('<%=doTaskAjax2 %>')"><fmt:message key="realizar" /></button>
						<%}else{ %>
							<portlet:actionURL var="doTask2" name="doTask">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>							
							</portlet:actionURL>
							<a href="<%= doTask2%>" class="btn btn-primary"><fmt:message key="realizar" /></a>							
						<%} %>
						
					</td>
				<%} %>				
			</tr>
		<%} %>
	</tbody>
</table>

<script>
	$(document).ready(function(){
		var locale= "<%=resourceRequest.getLocale() %>";
	   	$('#tabla-bonita').dataTable( { //CONVERTIMOS NUESTRO LISTADO DE LA FORMA DEL JQUERY.DATATABLES- PASAMOS EL ID DE LA TABLA
	        "sPaginationType": "full_numbers", //DAMOS FORMATO A LA PAGINACION(NUMEROS)
	        "language": change_locale_i18n(locale.split("_")[0]),
	        "search": {"search": "<%=(resourceRequest.getAttribute("bosSearch") != null) ? resourceRequest.getAttribute("bosSearch") : "" %>"}
	    } );
	})	
