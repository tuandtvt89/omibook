package com.ominext.ominext.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ominext.omibook.activity.R;
import com.ominext.omibook.helper.ImageLoader;
import com.ominext.omibook.model.Comic;

public class ComicListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<Comic> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public ComicListAdapter(Activity a, ArrayList<Comic> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.item_row, null);
		TextView nameTv = (TextView) vi.findViewById(R.id.nameTv); // duration
		ImageView thumbImg = (ImageView) vi.findViewById(R.id.thumbImg); // thumb
																			// image
		Comic comic = new Comic();
		comic = data.get(position);

		// Setting all values in listview
		nameTv.setText(comic.getName());
		imageLoader.DisplayImage(comic.getThumbUrl(), thumbImg);
		return vi;
	}

}
