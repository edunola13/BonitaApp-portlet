<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="bonitaClass.Task" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="content.Languaje"/>

<portlet:defineObjects />

<div id="bonita-taskForm">
<%if((Boolean)renderRequest.getAttribute("realizando")){ %>
	<%if((Boolean)renderRequest.getAttribute("estado")){ %>
		<%Task task= (Task)renderRequest.getAttribute("task"); %>
		<h4><%=task.getProcess().getName() %> - <%=task.getDisplayName() %> - Case ID: <%= task.getCaseId()%> - Task ID: <%=task.getId() %></h4>
		<h5><fmt:message key="fechaLimite"/>: <%=task.getDueDateString() %></h5>
		<p><%=task.getDisplayDescription() %></p>
	
		<form id="bonitaform" target="iframe" method="post" action="<%=renderRequest.getAttribute("url")%>"> 
			<input type="hidden" name="locale" value="en"> 
			<input type="hidden" name="formlocale" value="en"> 
			<input type="hidden" name="password" value="<%=renderRequest.getAttribute("password")%>"> 
			<input type="hidden" name="username" value="<%=renderRequest.getAttribute("userName")%>"> 
		</form>
	
		<iframe id="bonitaform" name="iframe" width="100%" height="400px"></iframe>
		<script type="text/javascript">/*<![CDATA[*/
			document.getElementById("bonitaform").submit();
			document.getElementById("bonitaform").remove();
		/*]]>*/</script>
		<script>
			<%if(renderRequest.getAttribute("scrollToPortlet") != null){ %>
			   	document.getElementById("bonita-taskForm").scrollIntoView(true);
			<%} %>
		</script>
	<% }else{%>
		<h5><fmt:message key="tareaCompletada"/></h5>
	<%}%>
<%}else{%>
	<h4><fmt:message key="tareaNoSeleccionada"/></h4>
<%}%>
</div>