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
boolean doTaskAjax = GetterUtil.getBoolean(portletPreferences.getValue("doTaskAjax", StringPool.TRUE));
boolean useAssignRelease = GetterUtil.getBoolean(portletPreferences.getValue("useAssignRelease", StringPool.TRUE));
boolean confirmStartCase = GetterUtil.getBoolean(portletPreferences.getValue("confirmStartCase", StringPool.TRUE));
boolean updateInbox = GetterUtil.getBoolean(portletPreferences.getValue("updateInbox", StringPool.TRUE));
String updateInterval= portletPreferences.getValue("updateInterval", "300");
%>

<aui:form action="<%= configurationURL %>" method="post" name="fm">
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<aui:input label="Show Title" name="preferences--title--" type="checkbox" value="<%= title %>" />
    <aui:input label="Task Form - Pop Up" name="preferences--doTaskAjax--" type="checkbox" value="<%= doTaskAjax %>" />
    <aui:input label="Use Assign/Release" name="preferences--useAssignRelease--" type="checkbox" value="<%= useAssignRelease %>" />
    <aui:input label="Confirm Start Case" name="preferences--confirmStartCase--" type="checkbox" value="<%= confirmStartCase %>" />
    <aui:input label="Update Inbox" name="preferences--updateInbox--" type="checkbox" value="<%= updateInbox %>" />
    <aui:input label="Update Interval(Seconds)" name="preferences--updateInterval--" type="text" value='<%= updateInterval %>' />

    <aui:button-row>
       <aui:button type="submit" />
    </aui:button-row>
</aui:form>