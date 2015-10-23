package com.bonitaApp.hook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bonitaApi.BonitaApi;

import com.liferay.portal.kernel.events.Action;

public class LogoutActionPre extends Action {
	/*
	 * Es pre logout para que todavia exista la variable de sesion y poder cortar la conexion
	 * @see com.liferay.portal.kernel.events.Action#run(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void run(HttpServletRequest req, HttpServletResponse res){
		HttpSession session= req.getSession();
		BonitaApi bonita= (BonitaApi)session.getAttribute("BONITA_API_PORT");
		if(bonita != null){
			try{
				System.out.println("## Borrando Session contra el servidor Bonita");
				bonita.logout();
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		System.out.println("## Custom Logout Bonita App");
	}
}
