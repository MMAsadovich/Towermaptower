package com.example.testactivity.ui.signal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.testactivity.R;
import com.google.android.material.navigation.NavigationView;

import static com.example.testactivity.MainActivity.navigationView;

public class SignalFragment extends Fragment {

    private SignalViewModel galleryViewModel;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(SignalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signal, container, false);

        final TextView mobileTextView = root.findViewById(R.id.text_mobile_dBm);
        final TextView wifiTextView = root.findViewById(R.id.text_wifi_dBm);
        final TextView companyTextView = root.findViewById(R.id.text_mobile_name);
        final TextView wifiConnect = root.findViewById(R.id.text_wifi_name);
        ImageView mobileImg = root.findViewById(R.id.imgMobile);
        ImageView wifiImg = root.findViewById(R.id.imgWifi);
        navigationView.getMenu().getItem(0).setEnabled(true);
        navigationView.getMenu().getItem(1).setEnabled(false);
        navigationView.getMenu().getItem(2).setEnabled(true);

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        new SignalInfo(context, mobileTextView, wifiManager, wifiTextView,
                companyTextView, wifiConnect, mobileImg, wifiImg).listen();

        return root;
    }

}
