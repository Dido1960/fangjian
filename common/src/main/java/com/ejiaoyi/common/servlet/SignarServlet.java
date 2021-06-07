package com.ejiaoyi.common.servlet;

import com.ejiaoyi.common.iWebPDF.iWebOffice;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignarServlet extends HttpServlet {
 
	private static final long serialVersionUID = 8031133938454140403L;
 
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        iWebOffice officeServer = new iWebOffice();
        officeServer.ExecuteRun(request,response);
	}
	
	
 
}

