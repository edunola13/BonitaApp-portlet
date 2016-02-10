<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Process" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<Process> processes= (List<Process>)renderRequest.getAttribute("processes");
%>

<div id="bonita-adminApps">
<jsp:include page="../../view-sections/header.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita-adminApps" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="version" /></th>
			<th><fmt:message key="estado" /></th>
			<th><fmt:message key="funciones" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(Process process: processes){ %>
			<tr>
				<td><%=process.getId() %></td>
				<td><%=process.getDisplayName() %></td>
				<td><%=process.getVersion() %></td>
				<td><%=process.getState() %></td>
				<td>
					<%if(process.getState().equals("ENABLED")) {%>
						<portlet:actionURL var="disableProcess" name="disableProcess">
							<portlet:param name="processId" value="<%=Long.toString(process.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= disableProcess%>" class="btn btn-primary"><fmt:message key="deshabilitar" /></a>
					<%}else{ %>
						<portlet:actionURL var="enableProcess" name="enableProcess">
							<portlet:param name="processId" value="<%=Long.toString(process.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= enableProcess%>" class="btn btn-primary"><fmt:message key="habilitar" /></a>
						<portlet:actionURL var="deleteProcess" name="deleteProcess">
							<portlet:param name="processId" value="<%=Long.toString(process.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= deleteProcess%>" class="btn btn-primary" onclick="return confirm('<fmt:message key="confirm-eliminarProceso" />')"><fmt:message key="eliminar" /></a>
					<%} %>
				</td>
			</tr>
		<%} %>
	</tbody>
</table>

<jsp:include page="../../view-sections/footer-adminApps.jsp"></jsp:include>
</div>