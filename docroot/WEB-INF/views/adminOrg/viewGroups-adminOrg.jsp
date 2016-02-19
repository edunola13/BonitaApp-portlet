<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %> 
<%@ page import="bonitaClass.Group" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<Group> groups= (List<Group>)renderRequest.getAttribute("groups");
%>

<div id="bonita-adminOrg">
<jsp:include page="../../view-sections/header-adminOrg.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

<portlet:resourceURL var="formGroup1" id="formGroup">
	<portlet:param name="groupId" value="0"/>
</portlet:resourceURL>
<button class="btn btn-primary" onclick="formGroup('<%=formGroup1 %>')" style="margin-bottom: 10px;"><fmt:message key="agregar" /></button>


<table id="tabla-bonita-adminOrg" class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>Id</th>
			<th><fmt:message key="nombre" /></th>
			<th><fmt:message key="descripcion" /></th>
			<th><fmt:message key="funciones" /></th>
		</tr>
	</thead>
	<tbody>
		<%for(Group group: groups){ %>
			<tr>
				<td><%=group.getId() %></td>
				<td><%=group.getDisplayName()%></td>
				<td><%=group.getDescription() %></td>
				<td>
					<portlet:resourceURL var="formGroup2" id="formGroup">
						<portlet:param name="groupId" value="<%=Long.toString(group.getId()) %>"/>
					</portlet:resourceURL>
					<button class="btn btn-primary" onclick="formGroup('<%=formGroup2 %>')"><fmt:message key="modificar" /></button>
					<portlet:actionURL var="deleteGroup" name="deleteGroup">
						<portlet:param name="groupId" value="<%=Long.toString(group.getId()) %>"/>
					</portlet:actionURL>
					<a href="<%= deleteGroup%>" class="btn btn-primary" onclick="return confirm('<fmt:message key="confirm-eliminarGroup" />')"><fmt:message key="eliminar" /></a>
				</td>	
			</tr>
		<%} %>
	</tbody>
</table>

<aui:script>
	function formGroup(url) {
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
                  	title: 'Form Group',
                  	id:'form-group'
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
   		//Elimino datos anteriores, en caso de que haya
   		$("#form-group").find(".modal-body").html("");
   		$("#form-group").find(".modal-header h3").html("Form Group");
   		$.ajax({
			url:url,
			success:function(data){
				$("#form-group").find(".modal-body").html(data);
			},
			error: function(){
				alert("error");	
			}				
		});
  	}	
</aui:script>

<jsp:include page="../../view-sections/footer-adminOrg.jsp"></jsp:include>
</div>