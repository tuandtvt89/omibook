package com.ominext.omibook.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.ominext.omibook.helper.ServiceHandler;

public class MainActivity extends Activity {
	// URL to get contacts JSON
	private static String url = "http://dev.ominext.com/omibook/comic/getListComic";

	// JSON Node names
	private static final String TAG_COMICS = "Comic";
	private static final String TAG_NAME = "name";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_THUMB = "thumb";
	private static final String TAG_FILE = "file";
	private static final String TAG_CATEGORY = "Category";
	private static final String TAG_NAME_CAT = "cat_name";

	// contacts JSONArray
	JSONArray contacts = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new GetComics().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetComics extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			// pDialog = new ProgressDialog(MainActivity.this);
			// pDialog.setMessage("Please wait...");
			// pDialog.setCancelable(false);
			// pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					contacts = jsonObj.getJSONArray("data");
					Log.d("lenght", ""+contacts.length());
					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);

						// Comic node is JSON Object
						JSONObject comic = c.getJSONObject(TAG_COMICS);
						String name = comic.getString(TAG_NAME);
						String file = comic.getString(TAG_FILE);
						String thumb = comic.getString(TAG_THUMB);
						String description = comic.getString(TAG_DESCRIPTION);
						// Comic node is JSON Object
						JSONObject category = c.getJSONObject(TAG_CATEGORY);
						String name_cat = category.getString(TAG_NAME_CAT);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
		}

	}

}
