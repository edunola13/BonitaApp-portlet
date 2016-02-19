<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@page import="bonitaClass.*" %>
<%@ page import="java.util.*" %>

<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
Group group= (Group)resourceRequest.getAttribute("group");
String action= "updateGroup";
if(group.getId() == 0){
	action= "addGroup";
}
%>

<portlet:actionURL var="submitGroup" name="<%=action %>">
</portlet:actionURL>
<form method="post" action="<%=submitGroup%>">
	<input type="hidden" name='<portlet:namespace/>groupId' value="<%=group.getId() %>">

	<label><fmt:message key="nombre" /></label>
	<input name="<portlet:namespace/>name" value="<%=group.getName() %>"></input>

	<label><fmt:message key="nombreDisplay" /></label>
	<input name="<portlet:namespace/>displayName" value="<%=group.getDisplayName() %>"></input>
	
	<label><fmt:message key="descripcion" /></label>
	<textarea name="<portlet:namespace/>description"><%=group.getDescription() %></textarea>
	
	<br>
	<button type="submit" class="btn btn-primary"><fmt:message key="enviar" /></button>
</form>