package com.jsu.action;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.struts2.ServletActionContext;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.jsu.daq.Server;
import com.opensymphony.xwork2.ActionSupport;

public class BeginAction extends ActionSupport{
	File file;
	
	private static org.dom4j.Element  root;
	static org.dom4j.Document document;
	private static String path;
	
	
	@Override
	public String execute() throws Exception {
		
		//BeginAction be = new BeginAction();
		String str = ServletActionContext.getServletContext().getRealPath("/");
		path = str+"WEB-INF\\mac_search.xml";
		
		DaqThread t1 = new DaqThread();
		t1.start();

		
		return SUCCESS;
	}

	
	class DaqThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub

			
			Server.begin(path);
			
		}
		
	}
	
}
