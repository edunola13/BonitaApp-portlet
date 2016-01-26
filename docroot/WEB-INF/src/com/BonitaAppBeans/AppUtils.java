package com.bonitaAppBeans;

import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.util.PortalUtil;

public class AppUtils {

	public String getUrlParameter(RenderRequest render, String name){
		HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(render));
		return httpReq.getParameter(name);
	}
}
