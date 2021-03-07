    package com.example.weatherappmvc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkRequest.Builder;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import view.MainActivityViewImplementor;

public class
MainActivity extends AppCompatActivity {
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final String BUNDLE_TEXTVIEW_APPROVEDTIME = "classname.textview.approvedtime";
    private static final String BUNDLE_TEXTVIEW_LATITUDE = "classname.textview.latitude";
    private static final String BUNDLE_TEXTVIEW_LONGITUDE = "classname.textview.longitude";
    private static final String BUNDLE_TEXTVIEW_UPDATE_DATA = "classname.textview.update_data";
    private static final String LAST_DOWNLOAD = "last_download";
    private static final String LAST_DOWNLOAD_EDITOR = "last_download_editor";

    private static long lastDownload = 0;
    private static boolean isConnected = true;

    private ConnectivityManager cm;
    private NetworkCallback callback;
    private NetworkRequest networkRequest;
    MainActivityViewImplementor mvcView;

    private Parcelable mListState;
    private Parcelable mTextViewApprovedState;
    private Parcelable mTextViewLatitudeState;;
    private Parcelable mTextViewLongitudeState;;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleReadGlobalVariable();
        mvcView = new MainActivityViewImplementor(this, null);
        setContentView(mvcView.getRootView());
        mvcView.initComponents();
        checkInternet(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mvcView.getRecyclerView().getLayoutManager().onSaveInstanceState();
        mTextViewApprovedState = mvcView.getApprovedTimeTextView().onSaveInstanceState();
        mTextViewLatitudeState = mvcView.getLatitudeTextView().onSaveInstanceState();
        mTextViewLongitudeState = mvcView.getLongitudeTextView().onSaveInstanceState();

        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mListState);
        outState.putParcelable(BUNDLE_TEXTVIEW_APPROVEDTIME,mTextViewApprovedState);
        outState.putParcelable(BUNDLE_TEXTVIEW_LATITUDE, mTextViewLatitudeState);
        outState.putParcelable(BUNDLE_TEXTVIEW_LONGITUDE, mTextViewLongitudeState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mListState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mTextViewApprovedState = savedInstanceState.getParcelable(BUNDLE_TEXTVIEW_APPROVEDTIME);
            mTextViewLatitudeState = savedInstanceState.getParcelable(BUNDLE_TEXTVIEW_LATITUDE);
            mTextViewLongitudeState = savedInstanceState.getParcelable(BUNDLE_TEXTVIEW_LONGITUDE);

            mvcView.getRecyclerView().getLayoutManager().onRestoreInstanceState(mListState);
            mvcView.getApprovedTimeTextView().onRestoreInstanceState(mTextViewApprovedState);
            mvcView.getLatitudeTextView().onRestoreInstanceState(mTextViewLatitudeState);
            mvcView.getLongitudeTextView().onRestoreInstanceState(mTextViewLongitudeState);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        if (System.currentTimeMillis() - lastDownload > 3_600_000) { // 1 hour
            System.out.println("ON START LAST DOWNLOAD: " + lastDownload);
            downloadForecast(null);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        cm.registerNetworkCallback(networkRequest, callback);
        mvcView.bindDataToView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        super.onPause();
        cm.unregisterNetworkCallback(callback);
    }

    /**
     * Invoked in onStart method. Will request for new data after an hour has passed
     * Also updates the lastDownload variable after.
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadForecast(View view) {
        mvcView.getFreshData();
        updateGlobalVariable();
    }

    /**
     * Continuously updates whether there is an Internet connection or not.
     * Displays an message to inform the user the Internet status.
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void checkInternet(Context context) {
        try{
            cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

            networkRequest = new Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build();
            callback = new NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    isConnected = true;
                    Toast.makeText(context, "Found Internet Connection", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    isConnected = false;
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            };
        }catch (Exception e){
            isConnected = false;
        }
        isConnected = false;
    }

    /**
     * Used to fetch the saved value of lastDownload. If lastDownload has no value
     * it will return with a default value of 0 instead.
     */
    public void handleReadGlobalVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences(LAST_DOWNLOAD_EDITOR, MODE_PRIVATE);
        lastDownload = sharedPreferences.getLong("lastDownload", 0);
    }

    /**
     * Updates the value of lastDownload and stores it with the SharedPreference API.
     * Uses a key value which is bound the lastDownload value to find it after the application
     * has been closed.
     */
    public void updateGlobalVariable(){
        lastDownload = System.currentTimeMillis();
        SharedPreferences.Editor editor = getSharedPreferences(LAST_DOWNLOAD_EDITOR, MODE_PRIVATE).edit();
        editor.putLong("lastDownload", lastDownload);
        editor.apply();
    }

}