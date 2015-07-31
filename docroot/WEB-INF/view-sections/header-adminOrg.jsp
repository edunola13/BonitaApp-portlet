<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery.dataTables.css" rel="stylesheet" />
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<portlet:actionURL var="update" name="updateData">
</portlet:actionURL>

<%if(portletPreferences.getValue("title", "false").equals("true")){ %>
<h3><fmt:message key="tituloAdminOrg" /></h3>
<%} %>

<%String section= (String)renderRequest.getAttribute("section"); %>
<ul class="nav nav-tabs">
	<li class="<%=((section.equals("users")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("usersActionUrl")%>"><fmt:message key="users" /></a></li>
	<li class="<%=((section.equals("groups")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("groupsActionUrl")%>"><fmt:message key="grupos" /></a></li>
  	<li class="<%=((section.equals("roles")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("rolesActionUrl")%>"><fmt:message key="roles" /></a></li>
  	<li class="pull-right"><a href="<%= update%>"><fmt:message key="actualizarDatos" /></a></li>     	
</ul>