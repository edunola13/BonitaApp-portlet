<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="bonitaClass.*" %>
<%@ page import="java.util.*" %>

<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%Task task= (Task)resourceRequest.getAttribute("task"); %>
<h4><%=task.getDisplayName() %> - Case ID: <%= task.getCaseId()%> - Task ID: <%=task.getId() %></h4>
<h5>Fecha Limite Estimada: <%=task.getDueDateString() %></h5>
<p><%=task.getDisplayDescription() %></p>
	
<portlet:actionURL var="assignId" name="assignId">
</portlet:actionURL>
<form method="post" action="<%=assignId%>"> 
	<input type="hidden" name='<portlet:namespace/>taskId' value="<%=task.getId() %>"> 
	<%List<User> users= (List<User>)resourceRequest.getAttribute("users"); %>
	<select name="<portlet:namespace/>userId" style="width: 350px;">
		<%for(User user : users){ %>
		<option value="<%=user.getId() %>"><%=user.getLastName() + " " + user.getFirstName() + " (" + user.getUserName() + ")" %></option>
		<%} %>
	</select>
	<button type="submit" class="btn btn-primary"><fmt:message key="asignar" /></button>
</form>