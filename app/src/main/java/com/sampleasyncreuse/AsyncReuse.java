package com.sampleasyncreuse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Aneh on 3/19/2015.
 */

@SuppressWarnings("ALL")
public class AsyncReuse extends AsyncTask<Void, Void, Void> {

    public GetResponse getResponse = null;
    String urlParam, requestMethod = "POST";
    String URLs;
    boolean dialogE = true;
    Activity activity;
    String dialogText = "Please Wait...";
    Context context;

    // Dialog builder
    private ProgressDialog Dialog;
    String response = "{\"status\":\"0\",\"msg\":\"Sorry something went wrong try again\"}";


    public onRefreshCompleteListener refreshCompleteListener;


    public void setCustomRefreshListener(onRefreshCompleteListener listener) {
        this.refreshCompleteListener = listener;
    }


    public interface onRefreshCompleteListener {

        void setOnCompleteRefreshListner(boolean isCompleted);
    }


    public AsyncReuse() {
        this.refreshCompleteListener = null;
    }

    public AsyncReuse(String url, boolean dialog, Activity activity1, Context cont) {
        URLs = url;
        dialogE = dialog;
        activity = activity1;
        context = cont;
    }

    public AsyncReuse(String url, boolean dialog) {
        URLs = url;
        dialogE = dialog;
    }

    public void getObjectQ(String param) {
        urlParam = param;
    }

    public void getRequestMethod(String method) {
        requestMethod = method;
    }

    public void setDialogtext(String text) {
        dialogText = text;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("dialog", "--" + dialogE);
        if (dialogE) {
            Dialog = new ProgressDialog(activity);
            Dialog.setMessage(dialogText);
            Dialog.setCancelable(false);
            Dialog.show();
        }
    }


    @Override
    protected Void doInBackground(Void... voids) {
        if (!isNetworkAvailable(context)) {
            response = "{\"status\":\"0\",\"msg\":\"No Internet connection.\"}";
            closeDialog(dialogE);
        } else {
            try {
                URL url;
                String urlParameters = urlParam;
                if (requestMethod.toLowerCase().equals("get")) {
                    url = new URL(URLs + "?" + urlParameters);
                } else {
                    url = new URL(URLs);
                }
                byte[] postData = new byte[0];

                postData = urlParameters.toString().getBytes("UTF-8");
                int postDataLength = postData.length;

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod(requestMethod);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.getOutputStream().write(postData);
                if (requestMethod.toLowerCase().equals("post")) {
                    Log.e("requestLowercase", "===" + requestMethod.toLowerCase() + " param:" + urlParameters);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(urlParameters);

                    writer.flush();
                    writer.close();
                    os.close();
                }
                conn.connect();

                int statusCode = conn.getResponseCode();
                Log.e("Status", "" + statusCode);
                switch (statusCode) {
                    case HttpURLConnection.HTTP_OK:
                        // throw some exception
                        InputStream is = conn.getInputStream();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                        String line;
                        StringBuffer responseData = new StringBuffer();
                        while ((line = rd.readLine()) != null) {
                            responseData.append(line);
                            responseData.append('\r');
                        }
                        rd.close();
                        response = responseData.toString();
                        break;
                    case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                        response = "{\"status\":\"0\",\"msg\":\"Connection timeout.\"}";
                        break;
                }
                closeDialog(dialogE);
            } catch (UnsupportedEncodingException e) {
                closeDialog(dialogE);
                e.printStackTrace();
            } catch (MalformedURLException e) {
                closeDialog(dialogE);
                e.printStackTrace();
            } catch (ProtocolException e) {
                closeDialog(dialogE);
                e.printStackTrace();
            } catch (IOException e) {
                closeDialog(dialogE);
                e.printStackTrace();
            }
        }
        return null;
    }

    private void closeDialog(boolean flag) {
        if (flag) {
            Dialog.dismiss();
        } else {
            /* Nothing to do*/
        }
    }

    public boolean isNetworkAvailable(Context context) {
        boolean isConnected;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("ResonseAsync", response.toString());
        if (Dialog != null) {
            Dialog.dismiss();
        }
        if (refreshCompleteListener != null)
            refreshCompleteListener.setOnCompleteRefreshListner(true);
        getResponse.getData(response.toString());
    }
}
