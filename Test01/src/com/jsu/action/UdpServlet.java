package com.jsu.action;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.junit.Test;

import com.jsu.po.TzData;

public class UdpServlet {


	private static final int PORT = 3388;
	private ArrayList<TzData> alldata;

	@Test
	public void testName() throws Exception {
			alldata = new ArrayList<>();
	        DatagramSocket ds = new DatagramSocket(PORT);
	        
	        DatagramPacket dp ;
	        byte[] echo = new byte[1];
			echo[0] = (byte)1;
	        
	        while(true)
	        {
	            byte[]buf = new byte[1024];
	            dp	= new DatagramPacket(buf,buf.length);
	            ds.receive(dp);
	            
	            String ip = dp.getAddress().getHostAddress();
	            int port = dp.getPort();
	            String text = new String(dp.getData(),0,dp.getLength());
	            if(text.equals("exit"))
	                {
	                    System.out.println(ip+"退出会话......");
	                    break;
	                }
	            
	            String temp = text;
	            
	            String splies[] = temp.split("\n");
	            for(int i =1 ; i < splies.length  ; i ++){
		            TzData date = new TzData();
		            
		            String splits[] = splies[i].split("\\|");
		            
		            date.setSourceMac(splits[0]);
		            date.setTargetMac(splits[1]);
		            date.setFrameBigClass(splits[2]);
		            date.setFrameSmallClass(splits[3]);
		            date.setChannel(splits[4]);
		            date.setrSSI(splits[5]);
		            
		            double p ;
		            double n ;
		            
		            double distance = 10*(((-60) - Double.parseDouble(date.getrSSI()))/40);
		            
		            DecimalFormat df = new DecimalFormat("0.00");
		            
		            date.setDistance(Double.parseDouble(df.format(Math.abs(distance))));
		            
		         
		      //      if(date.getSourceMac().equals("B0:E2:35:9E:F5:82"))
		            
		            	System.out.println(date.toString());
//		            else{
//		           // 	System.out.println("NO");
//		            }
		            alldata.add(date);
		            alldata.size();
	            }
	           
	          
	          //返回一个字节给探针设备
				InetAddress addr = dp.getAddress();
				dp = new DatagramPacket(echo, echo.length);
				dp.setAddress(addr);
				ds.send(dp);

	        }
	 
	        ds.close();
	
	}

}
