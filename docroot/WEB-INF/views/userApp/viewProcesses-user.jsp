<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Process" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<Process> processes= (List<Process>)renderRequest.getAttribute("processes");
%>

<jsp:include page="../../view-sections/header.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="funciones" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(Process process: processes){ %>
			<tr>
				<td><%=process.getId() %></td>
				<td><%=process.getDisplayName() %></td>
				<td>
					<portlet:actionURL var="startCase" name="startCase">
						<portlet:param name="processId" value="<%=Long.toString(process.getId()) %>"/>
					</portlet:actionURL>
					<a href="<%= startCase%>" class="btn btn-primary" onclick="return confirm('<fmt:message key="confirm-iniciarCaso" />')"><fmt:message key="iniciarCaso" /></a>
				</td>
			</tr>
		<%} %>
	</tbody>
</table>

<jsp:include page="../../view-sections/footer.jsp"></jsp:include>