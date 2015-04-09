<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="bonitaClass.Task" %>

<portlet:defineObjects />

<%if((Boolean)renderRequest.getAttribute("realizando")){ %>
	<%if((Boolean)renderRequest.getAttribute("estado")){ %>
		<%Task task= (Task)renderRequest.getAttribute("task"); %>
		<h4><%=task.getDisplayName() %> - Case ID: <%= task.getCaseId()%> - Task ID: <%=task.getId() %></h4>
		<h5>Fecha Limite Estimada: <%=task.getDueDateString() %></h5>
		<p><%=task.getDisplayDescription() %></p>
	
		<form id="bonitaform" target="iframe" method="post" action="<%=renderRequest.getAttribute("url")%>"> 
			<input type="hidden" name="<portlet:namespace />locale" value="en"> 
			<input type="hidden" name="<portlet:namespace />formlocale" value="en"> 
			<input type="hidden" name="<portlet:namespace />password" value="<%=renderRequest.getAttribute("password")%>"> 
			<input type="hidden" name="<portlet:namespace />username" value="<%=renderRequest.getAttribute("userName")%>"> 
		</form>
	
		<iframe id="bonitaform" name="iframe" width="100%" height="400px"></iframe>
		<script type="text/javascript">/*<![CDATA[*/document.getElementById("bonitaform").submit();/*]]>*/</script>
	<% }else{%>
		<h5>Tarea ya completada</h5>
	<%}%>
<%}%>