<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>  
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

<portlet:resourceURL var="formRole1" id="formRole">
	<portlet:param name="roleId" value="0"/>
</portlet:resourceURL>
<button class="btn btn-primary" onclick="formRole('<%=formRole1 %>')" style="margin-bottom: 10px;"><fmt:message key="agregar" /></button>

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
		<%for(Role role: roles){ %>
			<tr>
				<td><%=role.getId() %></td>
				<td><%=role.getDisplayName()%></td>
				<td><%=role.getDescription() %></td>
				<td>
					<portlet:resourceURL var="formRole2" id="formRole">
						<portlet:param name="roleId" value="<%=Long.toString(role.getId()) %>"/>
					</portlet:resourceURL>
					<button class="btn btn-primary" onclick="formRole('<%=formRole2 %>')"><fmt:message key="modificar" /></button>
					<portlet:actionURL var="deleteRole" name="deleteRole">
						<portlet:param name="roleId" value="<%=Long.toString(role.getId()) %>"/>
					</portlet:actionURL>
					<a href="<%= deleteRole%>" class="btn btn-primary" onclick="return confirm('<fmt:message key="confirm-eliminarRole" />')"><fmt:message key="eliminar" /></a>
				</td>
			</tr>
		<%} %>
	</tbody>
</table>

<aui:script>
	function formRole(url) {
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
                  	title: 'Form Role',
                  	id:'form-role'
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
   		$("#form-role").find(".modal-body").html("");
   		$("#form-role").find(".modal-header h3").html("Form Role");
   		$.ajax({
			url:url,
			success:function(data){
				$("#form-role").find(".modal-body").html(data);
			},
			error: function(){
				alert("error");	
			}				
		});
  	}	
</aui:script>

<jsp:include page="../../view-sections/footer-adminOrg.jsp"></jsp:include>
</div>