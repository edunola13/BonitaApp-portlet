<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Role" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<Role> roles= (List<Role>)renderRequest.getAttribute("roles");
%>

<div id="bonita-adminOrg">
<jsp:include page="../../view-sections/header-adminOrg.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita-adminOrg" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="descripcion" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(Role role: roles){ %>
			<tr>
				<td><%=role.getId() %></td>
				<td><%=role.getDisplayName()%></td>
				<td><%=role.getDescription() %></td>			
			</tr>
		<%} %>
	</tbody>
</table>

<jsp:include page="../../view-sections/footer-adminOrg.jsp"></jsp:include>
</div>