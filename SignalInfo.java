package com.example.testactivity.ui.signal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testactivity.MainActivity;
import com.example.testactivity.R;

public class SignalInfo {

    private Context context;
    private Integer mobileDbm = 0;
    private Integer wifiDbm = 0;
    private TextView mobileTextView;
    private TextView companyTextView;
    private WifiManager wifiManager;
    private TextView wifiTxt;
    private TextView wifiConnect;
    private ImageView wifiImageView;
    private ImageView mobileImageView;


    public SignalInfo(Context context, TextView textView,
                      WifiManager wifiManager, TextView wifiTxt,
                      TextView companyTextView,
                      TextView wifiConnect,
                      ImageView mobileImageView,
                      ImageView wifiImageView
    ) {
        this.context = context;
        this.mobileTextView = textView;
        this.wifiManager = wifiManager;
        this.wifiTxt = wifiTxt;
        this.companyTextView = companyTextView;
        this.wifiConnect = wifiConnect;
        this.mobileImageView = mobileImageView;
        this.wifiImageView = wifiImageView;

    }


    public void listen() {

        new Thread(() -> {
            while (true) {
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                mobileDbm = 0;
                try {
                    @SuppressLint("MissingPermission")
                    CellInfoGsm cellInfo = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                    mobileDbm = cellInfo.getCellSignalStrength().getDbm();
                } catch (Throwable ignored) {
                    //Toast.makeText(context,"Not Gsm- getDbm",Toast.LENGTH_LONG).show();
                }

                try {
                    @SuppressLint("MissingPermission")
                    CellInfoLte cellInfo = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                    mobileDbm = cellInfo.getCellSignalStrength().getDbm();
                } catch (Throwable ignored) {
                    //Toast.makeText(context,"Not Lte- getDbm",Toast.LENGTH_LONG).show();
                }

                try {
                    @SuppressLint("MissingPermission")
                    CellInfoCdma cellInfo = (CellInfoCdma) telephonyManager.getAllCellInfo().get(0);
                    mobileDbm = cellInfo.getCellSignalStrength().getDbm();
                } catch (Throwable ignored) {
                    // Toast.makeText(context,"Not Cdma - getDbm",Toast.LENGTH_LONG).show();
                }
                String company = telephonyManager.getSimOperatorName();
                //while (NetworkConnect.getNetworkConnect(context){
                for (Network network : connMgr.getAllNetworks()) {
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        MainActivity.isWifiConn |= networkInfo.isConnected();
                    } else {
                        MainActivity.isWifiConn |= networkInfo.isConnected();
                    }
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        MainActivity.isMobileConn |= networkInfo.isConnected();
                    }
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        mobileTextView.setText(mobileDbm + " dBm");
                        if (mobileDbm < -110)
                            mobileImageView.setImageResource(R.drawable.ic_baseline_signal_cellular_alt_24);
                        else if (mobileDbm < -90 && mobileDbm >= -109)
                            mobileImageView.setImageResource(R.drawable.ic_baseline_signal_cellular_alt_24_yellow);
                        else if (mobileDbm < -80 && mobileDbm >= -89)
                            mobileImageView.setImageResource(R.drawable.ic_baseline_signal_cellular_good);
                        else if (mobileDbm < -30 && mobileDbm >= -79)
                            mobileImageView.setImageResource(R.drawable.ic_baseline_signal_cellular_alt_24_green);
                        else if (mobileDbm >= -30)
                            mobileImageView.setImageResource(R.drawable.ic_baseline_signal_cellular_off);


                        if (mobileDbm == 0) {
                            companyTextView.setText("Mobile Company");
                        } else {
                            companyTextView.setText(company);
                        }
                        if (MainActivity.isWifiConn) {
                            wifiDbm = wifiManager.getConnectionInfo().getRssi();
                            wifiConnect.setText(R.string.wifi_connect);
                            wifiTxt.setText(wifiDbm + " dBm");
                            if (wifiDbm < -90 && wifiDbm > -126)
                                wifiImageView.setImageResource(R.drawable.ic_baseline_wifi_24_yellow);
                            if (wifiDbm > -90)
                                wifiImageView.setImageResource(R.drawable.ic_baseline_wifi_24);
                        }
                        if (wifiManager.getConnectionInfo().getRssi() <= -127) {
                            wifiConnect.setText(R.string.wifi_disconnect);
                            wifiTxt.setText(0 + " dBm");
                            wifiImageView.setImageResource(R.drawable.ic_baseline_wifi_off_24);
                        }

                        //Toast.makeText(context, mobileDbm +"", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
