<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery.dataTables.css" rel="stylesheet" />
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%String section= (String)renderRequest.getAttribute("section"); %>
<ul class="nav nav-tabs">
	<li class="<%=((section.equals("users")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("usersActionUrl")%>"><fmt:message key="users" /></a></li>
	<li class="<%=((section.equals("groups")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("groupsActionUrl")%>"><fmt:message key="grupos" /></a></li>
  	<li class="<%=((section.equals("roles")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("rolesActionUrl")%>"><fmt:message key="roles" /></a></li>  	
</ul>