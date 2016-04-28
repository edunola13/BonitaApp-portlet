<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery.dataTables.css" rel="stylesheet" />
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<portlet:actionURL var="update" name="updateData">
</portlet:actionURL>

<%if(portletPreferences.getValue("title", "false").equals("true")){ %>
<h3><fmt:message key="tituloControlPanel" /></h3>
<%} %>

<%String section= (String)renderRequest.getAttribute("section"); %>
<ul class="nav nav-tabs">
	<li class="<%=((section.equals("cases")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("casesActionUrl")%>"><fmt:message key="casosActivos" /></a></li>
<%-- 	<li class="<%=((section.equals("archivedCases")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("archivedCasesActionUrlActionUrl")%>"><fmt:message key="casosArchivos" /></a></li> --%>
	<li class="pull-right"><a href="<%= update%>"><fmt:message key="actualizarDatos" /></a></li> 
</ul>