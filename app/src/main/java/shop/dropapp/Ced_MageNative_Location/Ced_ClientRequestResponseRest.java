package shop.dropapp.Ced_MageNative_Location;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import shop.dropapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Ced_ClientRequestResponseRest extends AsyncTask<String, String, String> {
    public Ced_AsyncResponse delegate = null;

    Activity c;
    Context con;
    String req = "";
    HashMap<String, String> params;
    String data;
    boolean connect = true;
    Ced_NewLoader cedNewLoader;
    String Splash = "";
    int responseCode;
    String url;
    JSONObject data_post;
    Ced_SessionManagement session;
    Ced_SessionManagement_login login;

    public Ced_ClientRequestResponseRest(Ced_AsyncResponse cedAsyncResponse, Activity context) {
        delegate = cedAsyncResponse;
        c = context;
        req = "GET";
        session = new Ced_SessionManagement(context);
        login = new Ced_SessionManagement_login(context);
    }

    public Ced_ClientRequestResponseRest(Ced_AsyncResponse cedAsyncResponse, Activity context, String splash) {
        delegate = cedAsyncResponse;
        c = context;
        req = "GET";
        Splash = splash;
        session = new Ced_SessionManagement(context);
        login = new Ced_SessionManagement_login(context);
    }

    public Ced_ClientRequestResponseRest(Ced_AsyncResponse cedAsyncResponse, Activity context, String RequestMethod, String postparam) {
        delegate = cedAsyncResponse;
        c = context;
        req = RequestMethod;
        data = postparam;
        session = new Ced_SessionManagement(context);
        login = new Ced_SessionManagement_login(context);
    }

    public Ced_ClientRequestResponseRest(Ced_AsyncResponse cedAsyncResponse, Activity context, String RequestMethod, String postparam, String splash) {
        Splash = splash;
        delegate = cedAsyncResponse;
        c = context;
        req = RequestMethod;
        data = postparam;
        Log.i("REposnse", "" + data);
        session = new Ced_SessionManagement(context);
        login = new Ced_SessionManagement_login(context);
    }

    public Ced_ClientRequestResponseRest(Ced_AsyncResponse cedAsyncResponse, Context context, String RequestMethod, String postparam, String splash) {
        Splash = splash;
        delegate = cedAsyncResponse;
        con = context;
        req = RequestMethod;
        data = postparam;
        Log.i("REposnse", "" + data);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (Splash.isEmpty()) {
            cedNewLoader = new Ced_NewLoader(c);
            cedNewLoader.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String json = "";
        url = params[0];
        Log.i("REposnse", "" + url);
        if (req.equals("GET")) {
            json = Client(url);
        } else {
            json = ClientPost(url);
        }
        Log.i("REposnse", "" + json);
        return json;
    }

    @Override
    protected void onPostExecute(String av) {
        super.onPostExecute(av);
        if (connect) {

            try {
                JSONObject jsonObject = new JSONObject(av);
                if (jsonObject.has("successresponse")) {
                    if (Splash.equals("hyperlocal")) {
                        Object object = new JSONObject(jsonObject.getString("successresponse"));
                        /*Object object = array.get(0);*/
                        delegate.processFinish(object);
                    } else {
                        JSONArray array = new JSONArray(jsonObject.getString("successresponse"));
                        Object object = array.get(0);
                        delegate.processFinish(object);
                    }
                } else {
                    Toast.makeText(c, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing()) {
                    this.cedNewLoader.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.cedNewLoader = null;
            }
        } else {
            try {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing()) {
                    this.cedNewLoader.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.cedNewLoader = null;
            }
            try {
                c.runOnUiThread(new Runnable() {
                    public void run() {
                        final Dialog listDialog = new Dialog(c, R.style.PauseDialog);
                        ((ViewGroup) ((ViewGroup) listDialog.getWindow().getDecorView()).getChildAt(0)).getChildAt(1).setBackgroundColor(c.getResources().getColor(R.color.AppTheme));
                        listDialog.setTitle(c.getResources().getString(R.string.oops));
                        LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.magenative_activity_no_module, null, false);
                        listDialog.setContentView(v);
                        listDialog.setCancelable(true);
                        TextView conti = (TextView) listDialog.findViewById(R.id.conti);
                        conti.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listDialog.dismiss();
                                if (req.equals("GET")) {
                                    Ced_ClientRequestResponseRest ced_requestwithoutLoader = new Ced_ClientRequestResponseRest(delegate, c);
                                    ced_requestwithoutLoader.execute(url);
                                } else {
                                    if (Splash.isEmpty()) {
                                        Ced_ClientRequestResponseRest ced_requestwithoutLoader = null;
                                        try {
                                            ced_requestwithoutLoader = new Ced_ClientRequestResponseRest(delegate, c, req, data);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        ced_requestwithoutLoader.execute(url);
                                    } else {
                                        Ced_ClientRequestResponseRest ced_requestwithoutLoader = new Ced_ClientRequestResponseRest(delegate, c, req, data, Splash);
                                        ced_requestwithoutLoader.execute(url);
                                    }
                                }

                            }
                        });
                        listDialog.show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public String ClientPost(String url) {
        Log.i("REposnse", "" + url);

        URL url1;
        String response = "";
        try {
            url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            JSONObject jsonObject = new JSONObject(data);
            data_post = new JSONObject();
            data_post.put("parameters", jsonObject);
            Log.i("REposnse", "" + data_post);
            writer.write(data_post.toString());
            writer.flush();
            writer.close();
            os.close();
            responseCode = conn.getResponseCode();
            Log.i("REposnse", "" + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                JSONObject object = new JSONObject();
                object.put("successresponse", response);
                response = object.toString();
            } else {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            connect = false;
        }

        return response;
    }

    public String Client(String url) {
        String result = "";
        try {
            URL apiurl = null;
            HttpURLConnection conn;
            String line;
            BufferedReader rd;
            apiurl = new URL(url);
            conn = (HttpURLConnection) apiurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
            responseCode = conn.getResponseCode();
            Log.i("REposnse", "" + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
                JSONObject object = new JSONObject();
                object.put("successresponse", result);
                result = object.toString();
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            connect = false;
        }
        return result;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.i("REposnse", "" + params);
        return result.toString();
    }
}

