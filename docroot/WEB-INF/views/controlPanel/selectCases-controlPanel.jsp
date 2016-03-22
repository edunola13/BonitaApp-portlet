<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="bonitaClass.Case" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />

<%
List<Case> cases= (List<Case>)resourceRequest.getAttribute("cases");
%>

<select class="span12" onchange="changeCase()">
	<option value="0">Seleccione un Caso</option>
	<%for(Case caso: cases) { %>					
		<option value="<%= caso.getId()%>"><%= caso.getId()%></option>		  			
	<%} %>
</select>