<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>  
<%@ page import="bonitaClass.User" %>
<%@ page import="java.util.*" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%
List<User> users= (List<User>)renderRequest.getAttribute("users");
%>

<div id="bonita-adminOrg">
<jsp:include page="../../view-sections/header-adminOrg.jsp"></jsp:include>

<jsp:include page="../../view-sections/alert.jsp"></jsp:include>

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
				<%}else{ %>
					<td>INACTIVE</td>
				<%} %>
				<td>
				<portlet:resourceURL var="memberships" id="memberships">
					<portlet:param name="userName" value="<%=user.getUserName() %>"/>
				</portlet:resourceURL>
				<button class="btn btn-primary" onclick="memberships('<%=memberships %>')"><fmt:message key="membresias" /></button>
				<%if(user.getEnabled()) {%>
					<portlet:actionURL var="deactivateUser" name="deactivateUser">
						<portlet:param name="userId" value="<%=Long.toString(user.getId()) %>"/>
					</portlet:actionURL>
					<a href="<%= deactivateUser%>" class="btn btn-primary"><fmt:message key="deshabilitar" /></a>
				<%}else{ %>
					<portlet:actionURL var="activateUser" name="activateUser">
						<portlet:param name="userId" value="<%=Long.toString(user.getId()) %>"/>
					</portlet:actionURL>
					<a href="<%= activateUser%>" class="btn btn-primary"><fmt:message key="habilitar" /></a>
				<%} %>
				</td>				
			</tr>
		<%} %>
	</tbody>
</table>

<aui:script>
	function memberships(url) {
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
                  	title: 'Memberships',
                  	id:'memberships-form'
              	});
                      
              	Liferay.provide(
                    window,
                    '<portlet:namespace/>closePopup',
                    function(popupIdToClose) {
                    	var popupDialog = Liferay.Util.Window.getById(popupIdToClose);
                      	popupDialog.destroy();
                      	window.location.href='<%= renderRequest.getAttribute("usersActionUrl")%>';
                    },
                    ['liferay-util-window']
               	);
   		});
   		//Elimino datos anteriores, en caso de que haya
   		$("#memberships-form").find(".modal-body").html("");
   		$("#memberships-form").find(".modal-header h3").html("Memberships");
   		$.ajax({
			url:url,
			success:function(data){
				$("#memberships-form").find(".modal-body").html(data);
			},
			error: function(){
				alert("error");	
			}				
		});
  	}	
</aui:script>

<jsp:include page="../../view-sections/footer-adminOrg.jsp"></jsp:include>
</div>