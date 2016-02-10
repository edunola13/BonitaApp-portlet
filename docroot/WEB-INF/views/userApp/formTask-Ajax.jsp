<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %> 
<%@page import="bonitaClass.Task" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="content.Languaje"/>

<portlet:defineObjects />

<%if((Boolean)resourceRequest.getAttribute("errorAssign")){ %>
	<h5><fmt:message key="errorAsignando"/></h5>
<%}else{ %>
	<%if((Boolean)resourceRequest.getAttribute("estado")){ %>
		<%Task task= (Task)resourceRequest.getAttribute("task"); %>
		<h5><fmt:message key="fechaLimite"/>: <%=task.getDueDateString() %></h5>
		<p><%=task.getDisplayDescription() %></p>	
	
		<form id="bonitaform" target="iframe" method="post" action="<%=resourceRequest.getAttribute("url")%>"> 
			<input type="hidden" name="locale" value="<%= resourceRequest.getLocale()%>"> 
			<input type="hidden" name="formlocale" value="<%= resourceRequest.getLocale()%>"> 
			<input type="hidden" name="password" value="<%= resourceRequest.getAttribute("password")%>"> 
			<input type="hidden" name="username" value="<%= resourceRequest.getAttribute("userName")%>">
		</form>
		
		<iframe id="bonitaform" name="iframe" width="99%" height="90%"></iframe>
		<script type="text/javascript">/*<![CDATA[*/
			document.getElementById("bonitaform").submit();
			document.getElementById("bonitaform").remove();
			$("#dotask").find(".modal-header h3").html("<%=task.getProcess().getName() %> - <%=task.getDisplayName() %> - Case ID: <%= task.getCaseId()%> - Task ID: <%=task.getId() %>");
		/*]]>*/</script>
	<% }else{%>
		<h5><fmt:message key="tareaCompletada"/></h5>
	<%}%>
<%} %>