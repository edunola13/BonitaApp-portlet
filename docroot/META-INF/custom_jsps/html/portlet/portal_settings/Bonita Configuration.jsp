<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>
<%@page import="com.BonitaAppBeans.*" %>
<%@ include file="/html/portlet/portal_settings/init.jsp" %>

<liferay-ui:error-marker key="errorSection" value="Bonita configuration" />

<h3>Bonita Configuration</h3>
<%
String version  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.version");
String serverUrl  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.serverUrl");
String userAdmin  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.userAdmin");
String passAdmin  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.passAdmin");
String adminProfile  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.adminProfile");
String defaultGroup  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.defaultGroup");
String defaultRole  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.defaultRole");
String liferayGroups  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.liferayGroups");
String username  = PrefsPropsUtil.getString(company.getCompanyId(), "bonita.username");
%>

<aui:row>
	<aui:col width="<%= 50 %>">
		<aui:input name="settings--bonita.version--"  value="<%=version%>" type="text" label="Version">
			<aui:validator name="required"/>
		</aui:input>

		<aui:input name="settings--bonita.serverUrl--" value="<%=serverUrl%>" type="text" label="Server URL">
			<aui:validator name="required"/>
			<aui:validator name="url"/>
		</aui:input>

		<aui:input name="settings--bonita.userAdmin--" value="<%=userAdmin%>" label="User Admin">
			<aui:validator name="required"/>
		</aui:input>
		
		<aui:input name="settings--bonita.passAdmin--" value="<%=passAdmin%>" label="Password Admin">
			<aui:validator name="required"/>
		</aui:input>
	</aui:col>
	<aui:col width="<%= 50 %>">
		<aui:input name="settings--bonita.adminProfile--"  value="<%=adminProfile%>" type="text" label="Admin Profile">
			<aui:validator name="required"/>
		</aui:input>

		<aui:input name="settings--bonita.defaultGroup--" value="<%=defaultGroup%>" type="text" label="Default Group">
			<aui:validator name="required"/>
		</aui:input>

		<aui:input name="settings--bonita.defaultRole--" value="<%=defaultRole%>" label="Default Role">
			<aui:validator name="required"/>
		</aui:input>
		
		<aui:input name="settings--bonita.liferayGroups--" value="<%=liferayGroups%>" label="Liferay Groups">
			<aui:validator name="required"/>
		</aui:input>
		
		<aui:select name="settings--bonita.username--" label="User Name Bonita Map With">
			<aui:option value="ScreenName" label="Screen Name" selected='<%=username.equals("ScreenName") %>'></aui:option>
			<aui:option value="UserId" label="User Id" selected='<%=username.equals("UserId") %>'></aui:option>
		</aui:select>		
	</aui:col>
</aui:row>