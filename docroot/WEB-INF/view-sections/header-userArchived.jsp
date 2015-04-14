<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery.dataTables.css" rel="stylesheet" />
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%String section= (String)renderRequest.getAttribute("section"); %>
<ul class="nav nav-tabs">
	<li class="<%=((section.equals("tasks")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("tasksActionUrl")%>"><fmt:message key="tareas" /></a></li>
	<li class="<%=((section.equals("cases")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("casesActionUrl")%>"><fmt:message key="casos" /></a></li>
</ul>