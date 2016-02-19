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

<div id="bonita-userApps">
<jsp:include page="../../view-sections/header.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="proceso" /></th>
			<th><fmt:message key="caso" /></th>
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
				<td><%=task.getName() %>
				<td><%=task.getProcess().getName() %></td>
				<td><%=task.getCaseId() %></td>
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
						<%if(renderRequest.getPreferences().getValue("useAssignRelease", "true").equals("true")){ %>
							<portlet:actionURL var="assignId" name="assignId">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>
							</portlet:actionURL>
							<a href="<%= assignId%>" class="btn btn-primary"><fmt:message key="asignar" /></a>
						<%}else{ %>
							<%if(renderRequest.getPreferences().getValue("doTaskAjax", "true").equals("true")){ %>
								<portlet:resourceURL var="doTaskAjax" id="doTaskAjax">
									<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>						
								</portlet:resourceURL>
								<button class="btn btn-primary" value="Realizar Tarea" onclick="doTask('<%=doTaskAjax %>')"><fmt:message key="realizar" /></button>
							<%}else{ %>
								<portlet:actionURL var="doTask" name="doTask">
									<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>							
								</portlet:actionURL>
								<a href="<%= doTask%>" class="btn btn-primary"><fmt:message key="realizar" /></a>							
							<%} %>
						<%} %>
					</td>
				<%}else{ %>
					<td>SI</td>
					<td>
						<%if(renderRequest.getPreferences().getValue("useAssignRelease", "true").equals("true")){ %>
							<portlet:actionURL var="releaseId" name="releaseId">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>
							</portlet:actionURL>
							<a href="<%= releaseId%>" class="btn btn-primary"><fmt:message key="liberar" /></a>
						<%} %>
												
						<%if(renderRequest.getPreferences().getValue("doTaskAjax", "true").equals("true")){ %>
							<portlet:resourceURL var="doTaskAjax2" id="doTaskAjax">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>						
							</portlet:resourceURL>
							<button class="btn btn-primary" value="Realizar Tarea" onclick="doTask('<%=doTaskAjax2 %>')"><fmt:message key="realizar" /></button>
						<%}else{ %>
							<portlet:actionURL var="doTask2" name="doTask">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>							
							</portlet:actionURL>
							<a href="<%= doTask2%>" class="btn btn-primary"><fmt:message key="realizar" /></a>							
						<%} %>
						
					</td>
				<%} %>				
			</tr>
		<%} %>
	</tbody>	
</table>

<aui:script>
	function doTask(url) {
  		AUI().use( 'aui-io-deprecated',
     		'liferay-util-window',
     		function(A) {
     			Liferay.Util.openWindow({
                   	dialog: {
                       centered: true,
                       destroyOnClose: true,
                       cache: false,
                       width: "auto",
                       modal: true
                  	},
                  	title: 'Task Form',
                  	id:'dotask'
              	});
                      
              	Liferay.provide(
                    window,
                    '<portlet:namespace/>closePopup',
                    function(popupIdToClose) {
                    	confirm("asdasdad");
                    	var popupDialog = Liferay.Util.Window.getById(popupIdToClose);
                      	popupDialog.destroy();
                      	window.location.href='<%= renderRequest.getAttribute("tasksActionUrl")%>';
                    }, 
                    ['liferay-util-window']
               	);
           		$("#dotask .close").on("click", function() {
           			window.location.href='<%= renderRequest.getAttribute("tasksActionUrl")%>';
           		});
   		});
   		//Elimino datos anteriores, en caso de que haya
   		$("#dotask").find(".modal-body").html("");
   		$("#dotask").find(".modal-header h3").html("Task Form");
   		$.ajax({
			url:url,
			success:function(data){
				$("#dotask").find(".modal-body").html(data);
			},
			error: function(){
				alert("error");	
			}				
		});  
  	}  
</aui:script>

<jsp:include page="../../view-sections/footer.jsp"></jsp:include>
</div>