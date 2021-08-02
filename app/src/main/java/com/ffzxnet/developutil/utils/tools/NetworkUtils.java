package com.ffzxnet.developutil.utils.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class NetworkUtils {
    /**
     * 是否有网络连接
     *
     * @param paramContext
     * @return
     */
    public static boolean hasNetwork(Context paramContext) {
        try {
            ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Network network = localConnectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = localConnectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    return true;
                }
            } else {
                NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
                if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable())) {
                    return true;
                }
            }
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return false;
    }

    /**
     * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = connectMgr.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectMgr.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
        } else {
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null) {
                return info.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }
}
