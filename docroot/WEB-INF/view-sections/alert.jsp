<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<%String rtaAction= (String)renderRequest.getAttribute("rtaAction"); %>
<%if(rtaAction != null){
	if(rtaAction.equals("error")){%>
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
  			La accion ha fallado.
		</div>
<%	}else { %>
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
  			La acción se ha ejecutado correctamente.
		</div>
<%	}
}%>