<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<liferay-ui:error key="error" ><fmt:message key="errorAccion" /></liferay-ui:error>
<%if(SessionMessages.contains(renderRequest, "success")) {%>
	<div class="alert alert-info">
		<fmt:message key="exitoAccion" />
	</div>
<%} %>