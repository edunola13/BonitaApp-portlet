<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<%String rtaAction= (String)renderRequest.getAttribute("rtaAction"); %>
<%if(rtaAction != null){
	if(rtaAction.equals("error")){%>
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<fmt:message key="errorAccion" />  			
		</div>
<%	}else { %>
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<fmt:message key="exitoAccion" />  			
		</div>
<%	}
}%>