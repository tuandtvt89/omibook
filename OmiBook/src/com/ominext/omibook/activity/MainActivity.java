package com.ominext.omibook.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ominext.omibook.helper.AlertDialogManager;
import com.ominext.omibook.helper.DownloadFileFromURL;
import com.ominext.omibook.helper.ImageLoader;
import com.ominext.omibook.helper.ParsePlist;
import com.ominext.omibook.helper.ServiceHandler;
import com.ominext.omibook.helper.ParsePlist;
import com.ominext.omibook.model.Comic;
import com.ominext.omibook.model.ComicSubModel;
import com.ominext.omibook.utils.Utils;
import com.ominext.ominext.adapter.ComicListAdapter;

public class MainActivity extends Activity {
	// URL to get contacts JSON
	private static String url = "http://dev.ominext.com/omibook/comic/getListComic";
	// File url to download
	private static String file_url = "http://api.androidhive.info/progressdialog/hive.jpg";
	// JSON Node names
	private static final String TAG_COMICS = "Comic";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_THUMB = "thumb";
	private static final String TAG_FILE = "file";
	private static final String TAG_CATEGORY = "Category";
	private static final String TAG_NAME_CAT = "cat_name";

	// contacts JSONArray
	JSONArray contacts = null;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	// Progress Dialog
	private ProgressDialog pDialog;
	// list comics
	private ArrayList<Comic> comicList;
	// list view
	private ListView comicLv;
	private ComicListAdapter adapter;
	// content View
	private ImageView thumbImg;
	private TextView descriptionTv;

	// image loader
	public ImageLoader imageLoader;
	// conten layout
	public ImageButton thumbPlayBtn;

	public int currentSelector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stories_list);
		Utils.createFolder(getApplicationContext());
		currentSelector = 0;
		comicLv = (ListView) findViewById(R.id.comicLv);
		thumbImg = (ImageView) findViewById(R.id.thumbImg);
		descriptionTv = (TextView) findViewById(R.id.descriptionTv);
		comicList = new ArrayList<Comic>();
		imageLoader = new ImageLoader(getApplicationContext());
		thumbPlayBtn = (ImageButton) findViewById(R.id.thumbPlayBtn);
		
		thumbPlayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "view",
						Toast.LENGTH_LONG).show();
				Utils.createComicFolder(getApplicationContext(),
						comicList.get(currentSelector).getId());
				new DownloadFileFromURL(MainActivity.this, comicList.get(
						currentSelector).getId(), 0).execute(
						comicList.get(currentSelector).getContentUrl(),
						comicList.get(currentSelector).getThumbUrl());
			}
		});
		new GetComics().execute();

		/*
		 * Demo reading Plist file in Asset folder Parse Plist ->
		 * ArrayList<Object>
		 */
		String xml = Utils.readPlistFromAssets(this);
		ParsePlist parsePlist = new ParsePlist();
		try {
			parsePlist.parse(xml);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Đang tải ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

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
					Log.d("lenght", "" + contacts.length());
					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						Comic comicItem = new Comic();
						// Comic node is JSON Object
						JSONObject comic = c.getJSONObject(TAG_COMICS);
						String name = comic.getString(TAG_NAME);
						comicItem.setName(name);
						String id = comic.getString(TAG_ID);
						comicItem.setId(id);
						String file = comic.getString(TAG_FILE);
						comicItem.setContentUrl(file);
						String thumb = comic.getString(TAG_THUMB);
						comicItem.setThumbUrl(thumb);
						String description = comic.getString(TAG_DESCRIPTION);
						comicItem.setDescription(description);
						// Category node is JSON Object
						JSONObject category = c.getJSONObject(TAG_CATEGORY);
						String name_cat = category.getString(TAG_NAME_CAT);
						comicItem.setCat_name(name_cat);
						comicList.add(comicItem);
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
			// dismiss the dialog after getting all tracks
			pDialog.dismiss();
			// updating UI from Background Thread
			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// Getting adapter by passing xml data ArrayList
					adapter = new ComicListAdapter(MainActivity.this, comicList);
					comicLv.setAdapter(adapter);

					// Click event for single list row
					comicLv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							descriptionTv.setText(comicList.get(position)
									.getDescription());
							imageLoader.DisplayImage(comicList.get(position)
									.getThumbUrl(), thumbImg);
							currentSelector = position;
						}
					});
				}
			});
		}
	}
}
