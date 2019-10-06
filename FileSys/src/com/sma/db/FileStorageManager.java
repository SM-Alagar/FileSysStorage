package com.sma.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sma.db.concrete.Result;
import com.sma.db.timer.Time2Live;



public class FileStorageManager {

	private static String path = System.getProperty("user.dir") + File.pathSeparator + "DB" + File.pathSeparator;

	static FileStorageManager fileStorage;
	
//	private static final Logger logMe = Logger.getLogger(FileStorageManager.class);
	// Private Constructor - So that object cannot be created.
	private FileStorageManager() {}

	/**
	 * method Name : instantiate Parameter : no params returns : returns object of
	 * the class FileStorageManager; Description : This method used to instanciate
	 * singleton object of the class. DB File will be created at location of User
	 * Directory path defaultly
	 * 
	 */
	public static FileStorageManager instantiate() {
//		logMe.info("Initializing DB Object with default path");
		if (fileStorage == null) {
			synchronized (FileStorageManager.class) {
				if (fileStorage == null) {
					fileStorage = new FileStorageManager();
				}
			}
		}
//		logMe.info("DB Object Initialized");
		return fileStorage;
	}

	/**
	 * method Name : instantiate Parameter : String filePath - Path of the DB File
	 * to be created. returns : returns object of the class FileStorageManager;
	 * Description : This method used to instantiate singleton object of the class.
	 * DB File will be created at location specified in the argument. If empty or
	 * null, then will create at default path (i.e: user directory)
	 * 
	 */
	public static FileStorageManager instantiate(String filepath) {
//		logMe.info("Initializing DB Object with path specified @: "+filepath);
		if (!filepath.isEmpty()) {
			path = filepath;
		}
		if (fileStorage == null) {
			synchronized (FileStorageManager.class) {
				if (fileStorage == null) {
					fileStorage = new FileStorageManager();
				}
			}
		}
//		logMe.info("Object Initialized");
		return fileStorage;
	}

	/***
	 * method Name : setData Parameter : key and value of data to be stored returns
	 * : returns object of status (code, message, value); Description : This method
	 * used to create/save object using the given KEY. If Same key exist in the
	 * File, then will throw error message
	 * 
	 */
	public JSONObject setData(String key, String value) {
		JSONObject response = new JSONObject();
//		logMe.info("Setting data for key :"+key);
		try {
			Result res = validateKeyAndValue(key, value);
			if(res.getStatusCode()) {
				JSONObject object = fileStorage.getJSONData();
				if (object == null) {
					object = new JSONObject();
					object.put(key, value);
				} else {
					if (object.get(key) != null) {
						response.put("code", 1);
						response.put("message", "error");
						response.put("value", "Key Already available");
						return response;
					} else {
						object.put(key, value);
					}
				}
				response.put("code", 0);
				response.put("message", "success");
				response.put("value", "Inserted");
				saveData(object);
			} else {
				response.put("code", 1);
				response.put("message", "error");
				response.put("value", res.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("code", 1);
			response.put("message", "error");
			response.put("value", "UNKNOWN Error occured :"+e.getMessage());
		}
//		logMe.info("Data setted successfully");
		return response;
	}

	
	/***
	 * method Name : setData 
	 * Parameter : key, value and time in seconds (to delete data after specified time) 
	 * returns : returns object of status (code, message, value); 
	 * Description : This method used to create/save object using the given KEY. If Same key exist in the
	 * File, then will throw error message
	 * 
	 */
	public JSONObject setData(String key, String value, int seconds) {
		JSONObject response = new JSONObject();
//		logMe.info("Setting data for key : "+key+" for Seconds :"+seconds);
		try {
			Result res = validateKeyAndValue(key, value);
			if(res.getStatusCode()) {
				JSONObject object = fileStorage.getJSONData();
				if (object == null) {
					object = new JSONObject();
					object.put(key, value);
				} else {
					if (object.get(key) != null) {
						response.put("code", 1);
						response.put("message", "error");
						response.put("value", "Key Already available");
						return response;
					} else {
						object.put(key, value);
					}
				}
				response.put("code", 0);
				response.put("message", "success");
				response.put("value", "Inserted");
				saveData(object);
				new Time2Live(seconds, key);
			} else {
				response.put("code", 1);
				response.put("message", "error");
				response.put("value", res.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("code", 1);
			response.put("message", "error");
			response.put("value", "UNKNOWN Error occured :"+e.getMessage());
		}

//		logMe.info("Data Setted successfully");
		return response;
	}

	
	/***
	 * method Name : getData 
	 * Parameter : key 
	 * returns : returns object of status (code, message, value); 
	 * Description : This method used to get/retrieve object
	 * using the given KEY.
	 */
	public JSONObject getData(String key) {
		JSONObject response = new JSONObject();
//		logMe.info("Getting data for key :"+key);
		try {
			if (!key.isEmpty()) {
				JSONObject object = getJSONData();
				Object value = object.get(key);
				if (value != null && !value.toString().isEmpty()) {
					response.put("code", 0);
					response.put("message", "success");
					response.put("value", value.toString());
				} else {
					response.put("code", 1);
					response.put("message", "error");
					response.put("value", "Key not found!");
				}
			} else {
				response.put("code", 1);
				response.put("message", "error");
				response.put("value", "Key is empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("code", 1);
			response.put("message", "error");
			response.put("value", "UNKNOWN Error Occured : "+e.getMessage());
		}
		return response;
	}

	/***
	 * method Name : deleteData 
	 * Parameter : key 
	 * returns : returns object of status (code, message, value); 
	 * Description : This method used to delete/remove object.
	 */
	public JSONObject deleteData(String key) {
		JSONObject response = new JSONObject();
		if (!key.isEmpty()) {
			JSONObject object = getJSONData();
			Object value = object.remove(key);
			if (value != null) {
				response.put("code", 0);
				response.put("message", "success");
				response.put("value", value.toString());
				saveData(object);
			} else {
				response.put("code", 1);
				response.put("message", "error");
				response.put("value", "Key not found!");
			}
		} else {
			response.put("code", 1);
			response.put("message", "error");
			response.put("value", "Key is empty");
		}
		return response;
	}

	/***
	 * method Name 	: 	saveData 
	 * Parameter 	: 	JSONObject 
	 * returns 		: 	void
	 * Description 	: 	This method used to save the updated JSONObject into file.
	 * 
	 * 					Method is synchronized, to prevent multiple threads use this method to update the latest data.
	 */
	private synchronized void saveData(JSONObject object) {
		try {
			FileWriter file = new FileWriter(path + File.separator + "dbFile.txt");
			file.write(object.toJSONString());
			file.flush();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * method Name 	: 	setDataAsFile 
	 * Parameter 	: 	key, Value 
	 * returns 		: 	JSONObject
	 * Description 	: 	This method used to save the value in separate file. Name of the file would be KEY.
	 * 					This method also supports  sub directory functionality by getting KEY with dot 
	 */
	public JSONObject setDataAsFile(String key, String value) {
		JSONObject response = new JSONObject();
		try {
			final File myFile;
			if (key != null) {
				FileOutputStream fos = null;
				String subdir = null;
				String str = key;
				if (str.contains(".")) {
					String[] parts = str.split("\\.");
					subdir = parts[0];
				} else {
					key = str;
				}
				final File dir = new File(path);
				if (!dir.exists())
					dir.mkdirs();
				if (subdir != null) {
					final File subdir1 = new File(path + File.separator + subdir);
					if (!subdir1.exists())
						subdir1.mkdirs();
					myFile = new File(subdir1, key + ".txt");
				} else {
					myFile = new File(dir, key + ".txt");
				}
				if (!myFile.exists())
					myFile.createNewFile();

				fos = new FileOutputStream(myFile);
				fos.write(value.getBytes());
				response.put("code", 1);
				response.put("message", "success");
				fos.close();
			}
		} catch (Exception e) {
			try {
				response.put("code", 0);
				response.put("message", "error");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * getData from the given KEY.
	 */
	/***
	 * method Name 	: 	getDataFromFile 
	 * Parameter 	: 	key 
	 * returns 		: 	JSONObject
	 * Description 	: 	This method used to save the value in separate file. Name of the file would be KEY.
	 * 					This method also supports  sub directory functionality by getting KEY with dot 
	 */
	private JSONObject getDataFromFile(String key) {
		JSONObject response = new JSONObject();
		try {
			final File dir;
			if (key != null) {
				String subdir = null;
				String str = key;
				if (str.contains(".")) {
					String[] parts = str.split("\\.");
					subdir = parts[0];
					// key = parts[1];
				}
				if (subdir != null) {
					dir = new File(path + File.separator + subdir);
				} else {
					dir = new File(path);
				}
				final File myFile = new File(dir, key + ".txt");
				// Read text from file
				StringBuilder text = new StringBuilder();
				if (myFile.exists()) {
					BufferedReader br = new BufferedReader(new FileReader(myFile));
					String line;
					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
					br.close();
					response.put("code", 0);
					response.put("message", "success");
					response.put("value", text.toString());
				} else {
					response.put("code", 1);
					response.put("message", "error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/*
	 * This method will delete file from file system
	 * 
	 * @Param key:It is the key which is to be deleted.
	 * 
	 */
	/***
	 * method Name 	: 	deleteDataFile 
	 * Parameter 	: 	key 
	 * returns 		: 	JSONObject
	 * Description 	: 	This method used to delete the file. Name of the file would be KEY.
	 * 					This method also supports  sub directory functionality by getting KEY with dot 
	 */
	public JSONObject deleteDataFile(String key) {
		JSONObject response = new JSONObject();
		try {
			final File dir;
			if (key != null) {
				String subdir = null;
				String str = key;
				if (str.contains(".")) {
					String[] parts = str.split("\\.");
					subdir = parts[0];
					// key = parts[1];
				}
				if (subdir != null) {
					dir = new File(path + File.separator + subdir);
				} else {
					dir = new File(path);
				}
				final File myFile = new File(dir, key + ".txt");
				// Read text from file
				StringBuilder text = new StringBuilder();
				if (myFile.exists()) {
					boolean deleted = myFile.delete();
					if (deleted) {
						response.put("code", 0);
						response.put("message", "success");
						response.put("value", text.toString());
					} else {
						response.put("code", 1);
						response.put("message", "error");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/***
	 * method Name 	: 	getJSONData 
	 * Parameter 	: 	no arguments 
	 * returns 		: 	JSONObject
	 * Description 	: 	This method used to retrive the file content in JSONFormat. This is the single file storage.
	 */
	private JSONObject getJSONData() {
		JSONParser jsonParser = new JSONParser();
		JSONObject object = new JSONObject();
		try {
			File myFile = new File(path + File.separator + "dbFile.txt");
			
			final File dir = new File(path);
			if (!dir.exists())
				dir.mkdirs();
			
			if (!myFile.exists())
				myFile.createNewFile();
			// Read JSON file
			if (myFile.length() != 0) {
				FileReader reader = new FileReader(myFile);
				object = (JSONObject) jsonParser.parse(reader);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	
	/***
	 * method Name 	: 	verifyKeyAndValue 
	 * Parameter 	: 	key, value
	 * returns 		: 	JSONObject
	 * Description 	: 	This method used to verify length of KEY and VALUE which trying to save in file. 
	 * 					Key should be length of 32 chars
	 * 					Value should be length of 16KB size.
	 */
	private Result validateKeyAndValue(String key, String value) {

//		logMe.info("Validating KEY AND VALUE :"+key + " VALUE:"+ value);
		Result result = new Result();
		result.setStatusCode(true);
		result.setMessage("Success");
		result.setData("");
		// Validate Key
		if (key != null && !key.isEmpty() && !key.isBlank()) {
			if (key.length() > 32) {
				// Key should be length upto 32 chars
				result.setStatusCode(false);
				result.setMessage("Length of KEY exceeded. Supports 32 chars only.");
			}
		} else {
			// "invalid key";
			result.setStatusCode(false);
			result.setMessage("Key is invalid.");
		}

		// Validate Value if result is not 1
		if (result.getStatusCode()) {
			if (value != null && !value.isEmpty() && !value.isBlank()) {
				if (value.length() > (16 * 1024)) {
					// Value reached max length; Value can be upto 16KB
					result.setStatusCode(false);
					result.setMessage("Length of Value exceeded. Supports upto 16KB only.");
				} else {
					//validating value (it should be json)
					try {
						JSONParser parser = new JSONParser();
						parser.parse(value);
					}catch(Exception e) {
						//e.printStackTrace();
						result.setStatusCode(false);
						result.setMessage("Its not a valid JSON");
					}
				}
			} else {
				// Invalid Value
				result.setStatusCode(false);
				result.setMessage("Value is invalid");
			}
		}
		
		return result;
	}
}
