<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Case" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<Case> cases= (List<Case>)renderRequest.getAttribute("cases");
%>

<div id="bonita-userApps">
<jsp:include page="../../view-sections/header.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="proceso" /></th>
			<th><fmt:message key="estado" /></th>
			<th><fmt:message key="iniciado-por" /></th>
			<th><fmt:message key="iniciado" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(Case caso: cases){ %>
			<tr>
				<td><%=caso.getId() %></td>
				<td><%=caso.getProcess().getName() %></td>
				<td><%=caso.getState() %></td>
				<td><%=caso.getStartedBy() %></td>
				<td><%=caso.getBeginDateString() %></td>
			</tr>
		<%} %>
	</tbody>
</table>

<jsp:include page="../../view-sections/footer.jsp"></jsp:include>
</div>