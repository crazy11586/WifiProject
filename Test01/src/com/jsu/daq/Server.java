package com.jsu.daq;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jsu.tools.LruCache;
import com.mysql.jdbc.util.LRUCache;
/**
 * 
 * 	String line2 = "data={ \"id\": \"00cccdc3\", \"data\": [ { \"mac\":
		 \"a8:57:4e:fd:ed:5d\", \"rssi\": \"-69\",\"ch\": \"5\"}, { \"mac\":
		 \"a4:44:d1:ae:1b:03\", \"rssi\": \"-81\",\"ch\": \"5\", \"ts\":
		 \"jsuccw\", \"tmc\": \"a8:57:4e:fd:ed:5d\", \"tc\": \"Y\", \"ds\":
		 \"Y\" }], \"time\": \"Mon Feb 20 16:11:58 2017\",\"lat\":
		 \"\",\"lon\": \"\" }";
		 
	Change2WifiDate(line2);
 * @author Administrator
 *
 */

public class Server {

	private static ServerSocket serverSocket;
	private static org.dom4j.Element  root;
	private static String CurrentMac = "00-01-6C";
	private static boolean flag;
	static org.dom4j.Document document;
	private static LruCache<String,String> lrucache;
	
	/***
	 * ����
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		lrucache = new LruCache<String, String>(100);
		
		InitReader();
		InitDaq();

	}

	/**
	 * ��ʼ��dom4j,��������xml�ļ�
	 * @throws DocumentException
	 * @throws SAXException 
	 */
	private static void InitReader() throws DocumentException, SAXException {
		
		SAXReader saxReader = new SAXReader();
		saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		document = saxReader.read(new File("WebContent/WEB-INF/mac_search.xml"));
		root = document.getRootElement();
		
	}

	/**
	 * ��ʼ��Socket�˿ڣ���������̽�봫�������
	 * @throws DocumentException
	 */
	private static void InitDaq() throws IOException, UnsupportedEncodingException {
		serverSocket = new ServerSocket(10002);
		
		while (true) {

			Socket socket = serverSocket.accept();

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String line = in.readLine();

			while (line != null) {

				if (line.length() >= 4 && line.startsWith("data")) {

					Change2WifiDate(line);

				}

				line = in.readLine();

			}

			in.close();

			socket.close();

		}
		
	}
	
	/**
	 * �����յ���JSON����תΪ������д洢
	 * @param json�ַ���
	 * @throws UnsupportedEncodingException
	 */
	public static void Change2WifiDate(String jsonstr) throws UnsupportedEncodingException {
		
		JsonParser parse = new JsonParser(); // ����json������
		
		ArrayList<WifiData> alldata = new ArrayList<>();
		
		JsonObject json = (JsonObject) parse.parse(jsonstr.substring(5, jsonstr.length()));
		
		
		Wifi wifi = new Wifi();

		wifi.setId(json.get("id").getAsString());
		wifi.setTime(json.get("time").getAsString());
		wifi.setLat(json.get("lat").getAsString());
		wifi.setLon(json.get("lon").getAsString());

		JsonArray array = json.get("data").getAsJsonArray();

		//�����ɼ���������
		for (int i = 0; i < array.size(); i++) {

			WifiData wifiData = new WifiData();
			
			JsonObject subObject = array.get(i).getAsJsonObject();

			wifiData.setMac(subObject.get("mac") == null ? "-1" : subObject.get("mac").getAsString());
			wifiData.setRssi(subObject.get("rssi") == null ? "-1" : subObject.get("rssi").getAsString());
			wifiData.setCh(subObject.get("ch") == null ? "-1" : subObject.get("ch").getAsString());
			wifiData.setTs(subObject.get("ts") == null ? "-1" : new String(subObject.get("ts").getAsString().getBytes(), "UTF-8"));
			wifiData.setTmc(subObject.get("tmc") == null ? "-1" : subObject.get("tmc").getAsString());
			wifiData.setTc(subObject.get("tc") == null ? "-1" : subObject.get("tc").getAsString());
			wifiData.setDs(subObject.get("ds") == null ? "-1" : subObject.get("ds").getAsString());
			
			CurrentMac= wifiData.getMac().substring(0,8);
			flag = false;
			
			//����ڻ���������ڣ�ֱ��ȡ��
			if(lrucache.contains(CurrentMac)){
				
				wifiData.setPhoneFirm(lrucache.get(CurrentMac));
			
				System.out.println("Lru");
			
			}else{
				//�ж��Ƿ���Xml�ļ�����
				for (Iterator iter = root.elementIterator(); iter.hasNext();)
		        {
		        	org.dom4j.Element e =  (org.dom4j.Element) iter.next();       
		            if(e.element("key").getText().equals(CurrentMac)){
		            	wifiData.setPhoneFirm(e.element("value").getText());
		            	flag = true;
		            	lrucache.put(CurrentMac, e.element("value").getText());
		            	System.out.println("xml");
		            }
	
		        }
				//�������ϻ�ȡ
				if(!flag){
					System.out.println("no");
					String url = "http://www.imfirewall.com/ip-mac-lookup/get_mac_info.php?mac="+wifiData.getMac();
					JSONObject jsonoj;
					org.dom4j.Element Element_father;
					org.dom4j.Element Element_son_key;
					org.dom4j.Element Element_son_value;
					try {
						jsonoj = readJsonFromUrl(url);	  
						Element_father = root.addElement("wifi_item");
						Element_son_key = Element_father.addElement("key");
						Element_son_value = Element_father.addElement("value");
						Element_son_key.setText(CurrentMac);
						/**
						 * д��xml�Լ�lru
						 */
					    if("true".equals(jsonoj.get("success").toString())){
					    	String tempstr = ((JSONObject) jsonoj.get("result")).get("mac_producer").toString();
					    	wifiData.setPhoneFirm(tempstr);
							Element_son_value.setText(tempstr);
							lrucache.put(CurrentMac, tempstr);
					    }else{
					    	wifiData.setPhoneFirm("Private Enterprise");
					    	Element_son_value.setText("Private Enterprise");
					    	lrucache.put(CurrentMac, "Private Enterprise");
					    }
					    System.out.println("web");
					   
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			alldata.add(wifiData);
		}
		
		/**
		 * �������������д��xml��
		 */
	        try { 
	        	FileOutputStream out = new FileOutputStream("WebContent/WEB-INF/mac_search.xml");
		        //OutputFormat format = OutputFormat.createCompactFormat();//���������ļ������ֽ����ʺϵ���
		        OutputFormat format = OutputFormat.createPrettyPrint();//��׼�����֣��ʺϲ鿴ʱ��ʾ��
		        //1.����д���ļ�
		        format.setEncoding("utf-8");//ָ���ļ���ʽ 
		        XMLWriter writer = new XMLWriter(out,format);
				writer.write(document);
		        writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//д���ļ�
	        
		
	    
		wifi.setData(alldata);
		
		//������
		for (WifiData Element : alldata) {
			if ("-1".equals(Element.getTs())) {
				System.out.println(Element.toString1());
			} else if ("-1".equals(Element.getDs())) {
				System.out.println(Element.toString2());
			} else {
				System.out.println(Element.toString3());
			}
		}
	}
	
	/**
	 * ͨ��http��url��ַ��ת��ΪJSON����
	 * @param url��ַ
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	     // System.out.println("ͬʱ ������Ҳ�ܿ��� ����return�ˣ���Ȼ��ִ��finally�ģ�");
	    }
	  }
	
	/**
	 * ��ȡhttp����ֵ
	 * @param rd
	 * @return
	 * @throws IOException
	 */
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

}
