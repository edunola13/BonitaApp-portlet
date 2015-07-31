<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<%
boolean title = GetterUtil.getBoolean(portletPreferences.getValue("title", StringPool.FALSE));
%>

<aui:form action="<%= configurationURL %>" method="post" name="fm">
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

    <aui:input label="Show Title" name="preferences--title--" type="checkbox" value="<%= title %>" />

    <aui:button-row>
       <aui:button type="submit" />
    </aui:button-row>
</aui:form>