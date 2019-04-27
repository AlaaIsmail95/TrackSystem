package com.example.tracksystem.tracksystem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class NetInfo {
    private ConnectivityManager connManager;
    private WifiManager wifiManager = null;
    private WifiInfo wifiInfo = null;

    public NetInfo(Context context) {
        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

    }


    public String getWifiIpAddress() {
        if (null == wifiManager || null == wifiInfo)
            return "";
        int ipAddress = wifiInfo.getIpAddress();
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
    }


    public String getIPAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            NetworkInterface n = null;
            while (en.hasMoreElements()) {
                n = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = n.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipaddress = inetAddress.getHostAddress();
                        break;
                    }
                }
            }

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipaddress;
    }
}