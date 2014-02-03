package com.ominext.omibook.utils;

import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	public static boolean is3gOrWifiAvaiable(Context paramContext) {
		return (isInternetAvailable(paramContext))
				|| (isWifiAvailable(paramContext));
	}

	private static boolean isInternetAvailable(Context paramContext) {
		NetworkInfo[] arrayOfNetworkInfo = ((ConnectivityManager) paramContext
				.getSystemService("connectivity")).getAllNetworkInfo();
		int i = 0;
		if ((arrayOfNetworkInfo != null) && (arrayOfNetworkInfo.length > 0))
			i = arrayOfNetworkInfo.length;
		for (int j = 0;; j++) {
			if (j >= i)
				return false;
			if (arrayOfNetworkInfo[j].isConnected())
				return true;
		}
	}
	public static boolean isWifiAvailable(Context paramContext) {
		NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
				.getSystemService("connectivity")).getNetworkInfo(1);
		return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}
