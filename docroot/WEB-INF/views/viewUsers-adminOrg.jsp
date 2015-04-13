<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.User" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<User> users= (List<User>)renderRequest.getAttribute("users");
%>

<jsp:include page="../view-sections/header-adminOrg.jsp"></jsp:include>

<jsp:include page="../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita-adminOrg" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="apellido" /></th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="username" /></th>
			<th><fmt:message key="estado" /></th>
			<th><fmt:message key="funciones" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(User user: users){ %>
			<tr>
				<td><%=user.getId() %></td>
				<td><%=user.getFirstName() %></td>
				<td><%=user.getLastName() %></td>
				<td><%=user.getUserName() %></td>
				<%if(user.getEnabled()) {%>
					<td>ACTIVE</td>
					<td>
						<portlet:actionURL var="deactivateUser" name="deactivateUser">
							<portlet:param name="userId" value="<%=Long.toString(user.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= deactivateUser%>" class="btn btn-primary"><fmt:message key="deshabilitar" /></a>
					</td>
				<%}else{ %>
					<td>INACTIVE</td>
					<td>
						<portlet:actionURL var="activateUser" name="activateUser">
							<portlet:param name="userId" value="<%=Long.toString(user.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= activateUser%>" class="btn btn-primary"><fmt:message key="habilitar" /></a>
					</td>
				<%} %>				
			</tr>
		<%} %>
	</tbody>
</table>

<jsp:include page="../view-sections/footer-adminOrg.jsp"></jsp:include>