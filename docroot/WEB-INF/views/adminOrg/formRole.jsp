<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@page import="bonitaClass.*" %>
<%@ page import="java.util.*" %>

<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
Role role= (Role)resourceRequest.getAttribute("role");
String action= "updateRole";
if(role.getId() == 0){
	action= "addRole";
}
%>

<portlet:actionURL var="submitRole" name="<%=action %>">
</portlet:actionURL>
<form method="post" action="<%=submitRole%>">
	<input type="hidden" name='<portlet:namespace/>roleId' value="<%=role.getId() %>">

	<label><fmt:message key="nombre" /></label>
	<input name="<portlet:namespace/>name" value="<%=role.getName() %>"></input>

	<label><fmt:message key="nombreDisplay" /></label>
	<input name="<portlet:namespace/>displayName" value="<%=role.getDisplayName() %>"></input>
	
	<label><fmt:message key="descripcion" /></label>
	<textarea name="<portlet:namespace/>description"><%=role.getDescription() %></textarea>
	
	<br>
	<button type="submit" class="btn btn-primary"><fmt:message key="enviar" /></button>
</form>