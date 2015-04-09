<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery.dataTables.css" rel="stylesheet" />
<portlet:defineObjects />

<%String section= (String)renderRequest.getAttribute("section"); %>
<ul class="nav nav-tabs">
	<li class="<%=((section.equals("tasks")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("tasksActionUrl")%>">Tareas</a></li>
	<li class="<%=((section.equals("cases")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("casesActionUrl")%>">Casos</a></li>
  	<li class="<%=((section.equals("processes")) ? "active":"")%>"><a href="<%= renderRequest.getAttribute("processesActionUrl")%>">Procesos</a></li>  	
</ul>