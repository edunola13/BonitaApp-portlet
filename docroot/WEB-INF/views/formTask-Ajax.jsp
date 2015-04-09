<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="bonitaClass.Task" %>

<portlet:defineObjects />

<%if((Boolean)resourceRequest.getAttribute("estado")){ %>
	<%Task task= (Task)resourceRequest.getAttribute("task"); %>
	<h4><%=task.getDisplayName() %> - Case ID: <%= task.getCaseId()%> - Task ID: <%=task.getId() %></h4>
	<h5>Fecha Limite Estimada: <%=task.getDueDateString() %></h5>
	<p><%=task.getDisplayDescription() %></p>	

	<form id="bonitaform" target="iframe" method="post" action="<%=resourceRequest.getAttribute("url")%>"> 
		<input type="hidden" name="locale" value="en"> 
		<input type="hidden" name="formlocale" value="en"> 
		<input type="hidden" name="password" value="<%= resourceRequest.getAttribute("password")%>"> 
		<input type="hidden" name="username" value="<%= resourceRequest.getAttribute("userName")%>">
	</form>
	
	<iframe id="bonitaform" name="iframe" width="99%" height="90%"></iframe>
	<script type="text/javascript">/*<![CDATA[*/
		document.getElementById("bonitaform").submit();
		document.getElementById("bonitaform").remove();
	/*]]>*/</script>
<% }else{%>
	<h5>Tarea ya completada</h5>
<%}%>