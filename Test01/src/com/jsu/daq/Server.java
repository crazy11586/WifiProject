package com.jsu.daq;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Server {

	private static ServerSocket serverSocket;

	public static void main(String args[]) throws Exception {
		// String line2 = "data={ \"id\": \"00cccdc3\", \"data\": [ { \"mac\":
		// \"a8:57:4e:fd:ed:5d\", \"rssi\": \"-69\",\"ch\": \"5\"}, { \"mac\":
		// \"a4:44:d1:ae:1b:03\", \"rssi\": \"-81\",\"ch\": \"5\", \"ts\":
		// \"jsuccw\", \"tmc\": \"a8:57:4e:fd:ed:5d\", \"tc\": \"Y\", \"ds\":
		// \"Y\" }], \"time\": \"Mon Feb 20 16:11:58 2017\",\"lat\":
		// \"\",\"lon\": \"\" }";
		// Change2WifiDate(line2);
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

	public static void Change2WifiDate(String jsonstr) throws UnsupportedEncodingException {
		String temp = jsonstr.substring(5, jsonstr.length());
		JsonParser parse = new JsonParser(); // ´´½¨json½âÎöÆ÷
		ArrayList<WifiData> alldata = new ArrayList<>();
		JsonObject json = (JsonObject) parse.parse(temp);
		Wifi wifi = new Wifi();

		wifi.setId(json.get("id").getAsString());
		wifi.setTime(json.get("time").getAsString());
		wifi.setLat(json.get("lat").getAsString());
		wifi.setLon(json.get("lon").getAsString());

		JsonArray array = json.get("data").getAsJsonArray();

		for (int i = 0; i < array.size(); i++) {

			WifiData wifiData = new WifiData();
			JsonObject subObject = array.get(i).getAsJsonObject();

			wifiData.setMac(subObject.get("mac") == null ? "-1" : subObject.get("mac").getAsString());
			wifiData.setRssi(subObject.get("rssi") == null ? "-1" : subObject.get("rssi").getAsString());
			wifiData.setCh(subObject.get("ch") == null ? "-1" : subObject.get("ch").getAsString());
			wifiData.setTs(subObject.get("ts") == null ? "-1"
					: new String(subObject.get("ts").getAsString().getBytes(), "UTF-8"));
			wifiData.setTmc(subObject.get("tmc") == null ? "-1" : subObject.get("tmc").getAsString());
			wifiData.setTc(subObject.get("tc") == null ? "-1" : subObject.get("tc").getAsString());
			wifiData.setDs(subObject.get("ds") == null ? "-1" : subObject.get("ds").getAsString());

			String path = "http://mac.51240.com/" + wifiData.getMac() + "__mac/";

			Document document1;
			try {
				document1 = Jsoup.connect(path).timeout(2000000).get();
				Elements lis = document1.select("table table tr");
				Elements element = lis.get(1).select("td");
				String type = element.get(1).text();
				wifiData.setPhoneFirm(type);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			alldata.add(wifiData);
		}
		wifi.setData(alldata);

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

}
