<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.*" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
Case caso= (Case)resourceRequest.getAttribute("case");
List<Task> tasks= (List<Task>)resourceRequest.getAttribute("tasks");
List<Task> archivedTasks= (List<Task>)resourceRequest.getAttribute("archivedTasks");
%>

<h4><fmt:message key="caso" />: <%=caso.getId() %> - <fmt:message key="alcanzada"/>: <%=caso.getBeginDateString() %> <%if(caso.getEndDate() != null){ %>- <fmt:message key="finalizada"/>: <%=caso.getEndDateString() %> <%} %></h4>

<%if(tasks != null){ %>
<h5>Tareas Activas</h5>
<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="proceso" /></th>
			<th><fmt:message key="caso" /></th>
			<th><fmt:message key="alcanzada"/></th>
			<th><fmt:message key="deadLine" /></th>
			<th><fmt:message key="asignado" /></th>
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
				<%}else{ %>
					<td>SI - <%=task.getAssignedId().getUserName() %></td>						
				<%} %>				
			</tr>
		<%} %>
	</tbody>
</table>
<%} %>

<h5>Tareas Finalizadas</h5>
<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="proceso" /></th>
			<th><fmt:message key="caso" /></th>
			<th><fmt:message key="estado" /></th>
			<th><fmt:message key="prioridad" /></th>
			<th><fmt:message key="ejecutado" /></th>
			<th><fmt:message key="asignado" /></th>			
		</tr>
	</thead>
	<tbody>
		<%for(Task task: archivedTasks){ %>
			<tr>
				<td><%=task.getId() %></td>
				<td><%=task.getDisplayName() %>
				<td><%=task.getProcess().getDisplayName() %></td>
				<td><%=task.getCaseId() %></td>
				<td><%=task.getState() %></td>
				<td><%=task.getPriority() %></td>
				<td><%=task.getExecutedDateString() %></td>
				<td>
				<%if(task.getAssignedId() != null){ %>
					<%=task.getAssignedId().getUserName() %>
				<%} %>	
				</td>	
			</tr>
		<%} %>
	</tbody>	
</table>