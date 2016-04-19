package com.sampleasyncreuse;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GetResponse {

    private AsyncReuse requestServer;
    private CheckInternet checkInternet;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*checkInternet = new CheckInternet();
        context = MainActivity.this;
        checkInternet.isNetworkAvailable(context, MainActivity.this);

        if (checkInternet.isConnected) {*/
        executeServerReq();

        requestServer.getObjectQ("id=8");
        requestServer.getRequestMethod("POST");
        requestServer.setDialogtext("Loading data from server");
        requestServer.execute();
       /* }*/
    }

    private void executeServerReq() {
        requestServer = new AsyncReuse(URLs.listURL, true, this, getApplicationContext());
        requestServer.getResponse = this;
    }

    @Override
    public void getData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            Log.e("here", "----------------" + jsonObject.toString());
            TextView textView = (TextView) findViewById(R.id.opt);
            textView.setText(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
