package com.ominext.omibook.helper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ominext.omibook.model.ComicSubModel;
import com.ominext.omibook.utils.Constant;

public class ParsePlist {

	public void parse(String xml) throws XmlPullParserException, IOException {

		final ArrayList<ComicSubModel> dataModel = new ArrayList<ComicSubModel>();
		ComicSubModel model = null;

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser parser = factory.newPullParser();

		parser.setInput(new StringReader(xml));
		int eventType = parser.getEventType();

		boolean done = false;
		boolean parsingArray = false;

		String name = null;
		String key = null;

		while (eventType != XmlPullParser.END_DOCUMENT && !done) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				// Log.d(TAG, "START_DOCUMENT");
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();

				if (name.equalsIgnoreCase("dict")) {
					// root dict element
					model = new ComicSubModel();
				} else if (name.equalsIgnoreCase("key")) {
					key = parser.nextText();
				} else if (name.equalsIgnoreCase("integer")) {
					model.setPageIndex(Integer.parseInt(parser.nextText()));
				} else if (name.equalsIgnoreCase("string")) {
					if (key.equalsIgnoreCase(Constant.KEYPAGEIMAGE)) {
						model.setPageImageName(parser.nextText());
					} else if (key.equalsIgnoreCase(Constant.KEYPAGEJAP)) {
						model.setJapanSub(parser.nextText());
					} else if (key.equalsIgnoreCase(Constant.KEYPAGEENG)) {
						model.setEngSub(parser.nextText());
					}
				}

				break;
			case XmlPullParser.END_TAG:
				name = parser.getName();

				if (name.equalsIgnoreCase("dict")) {
					// Log.d(TAG, "END_TAG dict");
					dataModel.add(model);
					model = null;
				} else if (name.equalsIgnoreCase("array")) {
					parsingArray = false;
				} else if (name.equalsIgnoreCase("plist")) {
					done = true;
				}

				break;
			case XmlPullParser.END_DOCUMENT:
				// Log.d(TAG, "END_DOCUMENT");
				break;

			}
			eventType = parser.next();
		}
		
		System.out.println("End document" +
				": NumberArrayList : " + dataModel.size());
	}
}