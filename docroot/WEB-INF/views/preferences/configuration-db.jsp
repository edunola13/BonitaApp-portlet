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
String host = portletPreferences.getValue("host", "10.36.0.17");
String port = portletPreferences.getValue("port", "1521");
String schema = portletPreferences.getValue("schema", "XE");
String userdb = portletPreferences.getValue("user", "MUNICIPIO");
String pass = portletPreferences.getValue("pass", "123456");
boolean active = GetterUtil.getBoolean(portletPreferences.getValue("active", StringPool.FALSE));
%>

<aui:form action="<%= configurationURL %>" method="post" name="fm">
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<aui:input label="Show Title" name="preferences--title--" type="checkbox" value="<%= title %>" />
	
	<h4>Data Base</h4>
    <aui:input label="Active" name="preferences--active--" type="checkbox" value="<%= active %>" />
    <aui:input label="Host" name="preferences--host--" type="text" value="<%= host %>" />
    <aui:input label="Port" name="preferences--port--" type="text" value="<%= port %>" />
    <aui:input label="Schema" name="preferences--schema--" type="text" value="<%= schema %>" />
    <aui:input label="User" name="preferences--user--" type="text" value="<%= userdb %>" />
    <aui:input label="Password" name="preferences--pass--" type="text" value="<%= pass %>" />

    <aui:button-row>
       <aui:button type="submit" />
    </aui:button-row>
</aui:form>