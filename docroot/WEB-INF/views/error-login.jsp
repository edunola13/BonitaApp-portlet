<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<portlet:defineObjects />
<fmt:setBundle basename="content.Languaje"/>

<h5><fmt:message key="error-login1" /></h5>
<h6><fmt:message key="error-login2" /></h6>
<h6><fmt:message key="error-login3" /></h6>
<a href="<%= renderRequest.getAttribute("updateDataActionUrl")%>" class="btn btn-primary"><fmt:message key="actualizarDatos" /></a>