<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Task" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<!-- En si no es necesario lo del locale ya que liferay lo setea -->
<fmt:setLocale value="<%=renderRequest.getLocale() %>"/>
<fmt:setBundle basename="content.Languaje"/>

<%
List<Task> tasks= (List<Task>)renderRequest.getAttribute("tasks");
%>

<jsp:include page="../view-sections/header-userArchived.jsp"></jsp:include>

<jsp:include page="../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita-userArchived" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="proceso" /></th>
			<th><fmt:message key="caso" /></th>
			<th><fmt:message key="estado" /></th>
			<th><fmt:message key="prioridad" /></th>
			<th><fmt:message key="ejecutado" /></th>			
		</tr>
	</thead>
	<tbody>
		<%for(Task task: tasks){ %>
			<tr>
				<td><%=task.getId() %></td>
				<td><%=task.getName() %>
				<td><%=task.getProcess().getName() %></td>
				<td><%=task.getCaseId() %></td>
				<td><%=task.getState() %></td>
				<td><%=task.getPriority() %></td>
				<td><%=task.getExecutedDateString() %></td>		
			</tr>
		<%} %>
	</tbody>	
</table>

<jsp:include page="../view-sections/footer-userArchived.jsp"></jsp:include>