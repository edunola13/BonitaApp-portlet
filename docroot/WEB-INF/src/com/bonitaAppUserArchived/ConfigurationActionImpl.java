package com.bonitaAppUserArchived;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
//import javax.portlet.PortletPreferences;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

public class ConfigurationActionImpl extends DefaultConfigurationAction {
	@Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		//Aca podria validar etc.
		
        super.processAction(portletConfig, actionRequest, actionResponse);

        //PortletPreferences prefs = actionRequest.getPreferences();
        //String doTaskAjax = prefs.getValue("doTaskAjax", "true");
        //System.out.println(doTaskAjax)
        //No es necesario hacer prefs.store(); ya que se hace automaticamente todo el guardar por el super.processAction
	}
}
