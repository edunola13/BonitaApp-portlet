<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ page import="bonitaClass.Task" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<%
List<Task> tasks= (List<Task>)renderRequest.getAttribute("tasks");
%>

<jsp:include page="../view-sections/header.jsp"></jsp:include>

<jsp:include page="../view-sections/alert.jsp"></jsp:include>

<table id="tabla-bonita" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th>Nombre</th>
			<th>Proceso</th>
			<th>Caso</th>
			<th>Estado</th>
			<th>Prioridad</th>
			<th>DeadLinea</th>
			<th>Asignado</th>			
			<th>Funciones</th>
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
					Excedido <%=task.timeExceededDeadline() %>
				<%}else{ %>
					Resta <%=task.timeToDeadline() %>
				<%} %>
				</td>
				<%if(task.getAssignedId() == 0L){%>
					<td>NO</td>
					<td>
						<portlet:actionURL var="assignId" name="assignId">
							<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= assignId%>" class="btn btn-primary">Asignar</a>
					</td>
				<%}else{ %>
					<td>SI</td>
					<td>
						<portlet:actionURL var="releaseId" name="releaseId">
							<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>
						</portlet:actionURL>
						<a href="<%= releaseId%>" class="btn btn-primary">Liberar</a>
						<%if(renderRequest.getPreferences().getValue("doTaskAjax", "si").equals("no")){ %>
							<portlet:actionURL var="doTask" name="doTask">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>							
							</portlet:actionURL>
							<a href="<%= doTask%>" class="btn btn-primary">Realizar</a>
						<%}else{ %>
							<portlet:resourceURL var="doTaskAjax" id="doTaskAjax">
								<portlet:param name="taskId" value="<%=Long.toString(task.getId()) %>"/>						
							</portlet:resourceURL>
							<button class="btn btn-primary" value="Realizar Tarea" onclick="doTask('<%=doTaskAjax %>')">Realizar - Ajax</button>
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
                  	title: 'Formulario de Tarea',
                  	id:'dotask'
              	});
                      
              	Liferay.provide(
                    window,
                    '<portlet:namespace/>closePopup',
                    function(popupIdToClose) {
                    	var popupDialog = Liferay.Util.Window.getById(popupIdToClose);
                      	popupDialog.destroy();
                    }, 
                    ['liferay-util-window']
               	);
   		});
   		
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

<jsp:include page="../view-sections/footer.jsp"></jsp:include>