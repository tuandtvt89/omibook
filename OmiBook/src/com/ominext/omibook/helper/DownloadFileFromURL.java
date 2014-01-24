package com.ominext.omibook.helper;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.AvoidXfermode.Mode;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

	// Progress Dialog
	private ProgressDialog pDialog;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;
	// Activity
	private Activity activity;
	// Activity
	private String id;
	// mode download 0: zip file 1: image file
	private int downloadMode;

	/**
	 * 
	 * @param constructor
	 */
	public DownloadFileFromURL(Activity activity, String id, int dowloadMode) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.id = id;
		this.downloadMode = dowloadMode;
	}

	/**
	 * Before starting background thread Show Progress Bar Dialog
	 * */
	@SuppressWarnings("deprecation")
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (downloadMode == 0) {
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	}

	/**
	 * Downloading file in background thread
	 * */
	@SuppressLint("SdCardPath")
	@Override
	protected String doInBackground(String... f_url) {
		int count;
		for (int i = 0; i < f_url.length; i++) {
			try {
				URL url = new URL(f_url[i]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();
				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);
				// Output stream to write file
				String path;
				if (i == 0)
					path = "/sdcard/Omibook/" + id + "/" + id + "_content.zip";
				else
					path = "/sdcard/Omibook/" + id + "/" + id + "_thumb.gif";
				OutputStream output = new FileOutputStream(path);
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					// writing data to file
					output.write(data, 0, count);
				}
				// flushing output
				output.flush();
				// closing streams
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
		}

		return null;
	}

	/**
	 * Updating progress bar
	 * */
	protected void onProgressUpdate(String... progress) {
		// setting progress percentage
		if (downloadMode == 0)
			pDialog.setProgress(Integer.parseInt(progress[0]));
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(String file_url) {
		// dismiss the dialog after the file was downloaded
		if (downloadMode == 0)
			pDialog.dismiss();
	}
}
