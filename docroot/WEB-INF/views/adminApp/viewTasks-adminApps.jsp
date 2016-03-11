<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="bonitaClass.Task" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<!-- En si no es necesario lo del locale ya que liferay lo setea -->
<fmt:setLocale value="<%=renderRequest.getLocale() %>"/>
<fmt:setBundle basename="content.Languaje"/>

<%
List<Task> tasks= (List<Task>)renderRequest.getAttribute("tasks");
%>

<div id="bonita-adminApps">
<jsp:include page="../../view-sections/header.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita-adminApps" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="proceso" /></th>
			<th><fmt:message key="caso" /></th>
			<th><fmt:message key="alcanzada"/></th>
			<th><fmt:message key="estado" /></th>
			<th><fmt:message key="prioridad" /></th>
			<th><fmt:message key="deadLine" /></th>
			<th><fmt:message key="asignado" /></th>			
			<th><fmt:message key="funciones" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(Task task: tasks){ %>
			<tr>
				<td><%=task.getId() %></td>
				<td><%=task.getDisplayName() %>
				<td><%=task.getProcess().getDisplayName() %></td>
				<td><%=task.getCaseId() %></td>
				<td><%=task.getReachedStateDateString() %></td>
				<td><%=task.getState() %></td>
				<td><%=task.getPriority() %></td>
				<td>
				<%if(task.exceededDeadline()){ %>
					<fmt:message key="excedido" /> <%=task.timeExceededDeadline() %>
				<%}else{ %>
					<fmt:message key="resta" /> <%=task.timeToDeadline() %>
				<%} %>
				</td>
				<%if(task.getAssignedId() == null){%>
					<td>NO</td>
					<td>
						<portlet:resourceURL var="assignTask" id="assignTask">
							<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>						
						</portlet:resourceURL>
						<button class="btn btn-primary" onclick="assignTask('<%=assignTask %>')"><fmt:message key="asignar" /></button>
					</td>
				<%}else{ %>
					<td><%=task.getAssignedId().getUserName() %></td>
					<td>
						<portlet:actionURL var="releaseId" name="releaseId">
							<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= releaseId%>" class="btn btn-primary"><fmt:message key="liberar" /></a>				
					</td>
				<%} %>				
			</tr>
		<%} %>
	</tbody>	
</table>

<aui:script>
	function assignTask(url) {
  		AUI().use( 'aui-io-deprecated',
     		'liferay-util-window',
     		function(A) {
     			Liferay.Util.openWindow({
                   	dialog: {
                       centered: true,
                       destroyOnClose: true,
                       cache: false,
                       width: "400",
                       modal: true
                  	},
                  	title: 'Assign Task',
                  	id:'assigntask'
              	});
                      
              	Liferay.provide(
                    window,
                    '<portlet:namespace/>closePopup',
                    function(popupIdToClose) {
                    	var popupDialog = Liferay.Util.Window.getById(popupIdToClose);
                      	popupDialog.destroy();
                      	location.reload();
                    },
                    ['liferay-util-window']
               	);
   		});
   		//Elimino datos anteriores, en caso de que haya
   		$("#assigntask").find(".modal-body").html("");
   		$("#assigntask").find(".modal-header h3").html("Assign Task");
   		$.ajax({
			url:url,
			success:function(data){
				$("#assigntask").find(".modal-body").html(data);
			},
			error: function(){
				alert("error");	
			}				
		});
  	}	
</aui:script>

<jsp:include page="../../view-sections/footer-adminApps.jsp"></jsp:include>
</div>