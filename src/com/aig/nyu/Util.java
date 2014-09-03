package com.aig.nyu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Util extends Activity
{
	private static final String DATA_FILE_DIR = "pass";
	private static final String DATA_FILE_NAME = "passdata.txt";
	private static final String CASE_TO_UNLOCK = "unlock";
	
	
	public static void CheckInternalStorage(Context cxt) throws Exception
	{
		
		
		
		
		// Throw exception if context is null
		if (cxt == null)
		{
			// Change to illegal argument exception?
			throw new Exception(
					"Error in reading internal storage. Passed context is null.");
		}

		String passwordFromFile = null;

		//directory to check
		File dir = cxt.getFilesDir();

		File dataFile = new File(dir, DATA_FILE_NAME);

		// Check if file exists
		if (dataFile.exists())
		{
			// Create reader
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String line = "";
			int i = 0;
			while ((line = reader.readLine()) != null)
			{
				i++;
				Log.d("jj", line);
				passwordFromFile = line;
				
			}
			reader.close();

			// pass the known password to the UnlockActivity so we can compare
			// it to what the user enters.
			// make a bundle...
			Intent goToUnlockActivity = new Intent(cxt, UnlockActivity.class);
			goToUnlockActivity.putExtra(CASE_TO_UNLOCK, passwordFromFile);
			((Activity)cxt).startActivityForResult(goToUnlockActivity,0);
			
			
		}

		if (!dataFile.canRead())
		{
			// Change to IOException?
			throw new Exception(
					"Error while loading internal file. Unable to read the data file.");
		}
	}
	
	

	public static void WriteToInternalStorage(String password, Context context) throws IOException
	{

		FileWriter writer = null;

		try
		{
			// writing to Android internal storage directory here.
			// Making a directory called pass to put the file into.
			// will encrypt later and compare hash password to hash password
			// if that much security is needed. Though internal storage is
			// usually
			// secure enough for most apps already by using MODE_PRIVATE.
			//File dirc = context.getDir(DATA_FILE_DIR, Context.MODE_PRIVATE);
			//File file = new File(dirc, DATA_FILE_NAME);
			//boolean deleted = file.delete();
			//Log.d("DELETED", Boolean.toString(deleted));

			//Making a file directory on the device.
			
			File dir = context.getFilesDir();
			Log.d("SEE ", dir.getAbsolutePath());
			File dataFile = new File(dir, DATA_FILE_NAME);

			writer = new FileWriter(dataFile,false);
            Log.d("DATA FILE", dataFile.getAbsolutePath());
			
			String hashPass = Util.md5(password.trim());
			writer.write(hashPass);
			
			writer.flush();
			writer.close();

		} catch (Exception e)
		{
			Log.e("Writing Error", "Error: " + e.getMessage());
		}

		writer.close();
	}
	
	
	
	public static String md5(String input){
		String result = null;
        String special = "$1$";
        String res = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] md5 = algorithm.digest();
            String tmp = "";
            for (int i = 0; i < md5.length; i++) {
                tmp = (Integer.toHexString(0xFF & md5[i]));
                if (tmp.length() == 1) {
                    res += "0" + tmp;
                } else {
                    res += tmp;
                }
            }
        } catch (NoSuchAlgorithmException ex) {}
        
        result = special + res;
        return result;
    }
	
	
	public static String splitCamelCase(String s)
	{
		return s.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), ",");
	}
	
	
}
