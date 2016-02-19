<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="bonitaClass.*" %>
<%@ page import="java.util.*" %>

<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%User user= (User)resourceRequest.getAttribute("user"); %>
<h4><%=user.getFirstName() + " " + user.getLastName() %> - User ID: <%=user.getId() %></h4>
	
<h5><fmt:message key="membresias-actuales" /></h5>
<%List<Membership> memberships= (List<Membership>)resourceRequest.getAttribute("memberships"); %>
<%for(Membership membership : memberships){ %>
	<p><%=membership.getRole().getName()%> <fmt:message key="membresias-de" /> <%=membership.getGroup().getName()%></p>
<%} %>	

<h5><fmt:message key="membresias-nueva" /></h5>
<portlet:actionURL var="assignMembership" name="assignMembership">
</portlet:actionURL>
<form method="post" action="<%=assignMembership%>"> 
	<input type="hidden" name='<portlet:namespace/>userId' value="<%=user.getId() %>">
	
	<label><fmt:message key="role" /></label>
	<%List<Role> roles= (List<Role>)resourceRequest.getAttribute("roles"); %>
	<select name="<portlet:namespace/>roleId" style="width: 350px;">
		<%for(Role role : roles){ %>
		<option value="<%=role.getId() %>"><%=role.getDisplayName() %></option>
		<%} %>
	</select>
	
	<label><fmt:message key="grupo" /></label>
	<%List<Group> groups= (List<Group>)resourceRequest.getAttribute("groups"); %>
	<select name="<portlet:namespace/>groupId" style="width: 350px;">
		<%for(Group group : groups){ %>
		<option value="<%=group.getId() %>"><%=group.getDisplayName() %></option>
		<%} %>
	</select>
	
	<button type="submit" class="btn btn-primary"><fmt:message key="asignar" /></button>
</form>