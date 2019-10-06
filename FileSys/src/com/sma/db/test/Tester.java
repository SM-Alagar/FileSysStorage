package com.sma.db.test;

import org.json.simple.JSONObject;

import com.sma.db.FileStorageManager;

public class Tester {
	public static void main(String args[]) {
		FileStorageManager fileStorage = FileStorageManager.instantiate("D:\\SMAlagar\\DB");
//		fileStorage.setDataa("SAMPLEKEY5", "VALUE");
//		fileStorage.setDataa("SAMPLEKEY6", "VALUE");
//		fileStorage.setDataa("SAMPLEKEY7", "VALUE");
//		fileStorage.setDataa("SAMPLEKEY8", "VALUE");
//		fileStorage.setDataa("SAMPLEKEY9", "VALUE");
		
//		JSONObject object = fileStorage.setData("MUTHALAGAR_SETHURAMAN_PAVITHRA_S","{\"MUTHU\":\"DATAIS THIS\"}");
		JSONObject object = fileStorage.getData("MUTHALAGAR_SETHURAMAN_PAVITHRA_M");
//		JSONObject object2 = fileStorage.deleteData("MUTHALAGAR_SETHURAMAN_PAVITHRA_S");
		JSONObject object3 = fileStorage.getData("MUTHALAGAR_SETHURAMAN_PAVITHRA_S");
		printData(object);
//		printData(object2);
		printData(object3);
//		FileStorageManager fileStorage2 = FileStorageManager.instantiate();
//		JSONObject object6 = fileStorage2.setData("MUTHALAGAR_SETHURAMAN_PAVITHRA_M","{\"MUTHU\":\"DETAIL 1\"}");
//		JSONObject object7 = fileStorage2.setData("MUTHALAGAR_SETHURAMAN_PAVITHRA_S","{\"MUTHU2\":\"DETAIL 2\"}");
//		JSONObject object4 = fileStorage2.getData("MUTHALAGAR_SETHURAMAN_PAVITHRA_M");
//		JSONObject object5 = fileStorage2.getData("MUTHALAGAR_SETHURAMAN_PAVITHRA_S");
//		printData(object4);
//		printData(object5);
		
		fileStorage.setData("SMALAGAR", "{\"MUTHU2\":\"SETTING FOR FEW SECONDS\"}", 10);
		
		//System.out.println("DATE IS : "+fileStorage.getData("KEY HERE"));
		//fileStorage.deleteData("KEY HERE");
		
		
	}
	
	public static void printData(JSONObject o) {
		System.out.println(o.get("code"));
		System.out.println(o.get("message"));
		System.out.println(o.get("value"));
		System.out.println("");
	}
}
