package com.conti.networktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView responseText;
    public static final String MY_URL = "http://rap2.taobao.org:38080/app/mock/262475/wiki";
    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private static final int HTTP_OK=1;

    private Wiki wiki=null;

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what==HTTP_OK){
                new MyPopupWindow().onButtonShowPopupWindowClick(mLinearLayout,HttpActivity.this, wiki);
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
//        ActionBar actionBar=getSupportActionBar();
//        if(actionBar!=null){actionBar.hide();}

        Button sendRequest = findViewById(R.id.send_request);
        responseText = findViewById(R.id.response_text);

        mLinearLayout=findViewById(R.id.activity_http);
        mImageView=findViewById(R.id.poi_pic_name);
        sendRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_request) {
//            sendRequestWithHttpURLConnection();
//            sendRequestWithOkHttp();
//            setMyPopupWindow(mLinearLayout,this);

            HttpGetUtil.sendOkHttpGetRequest(MY_URL,new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    assert response.body() != null;
                    String responseData=response.body().string();
                    Gson gson=new Gson();
                    wiki=gson.fromJson(responseData,Wiki.class);
                    Log.d("HttpActivity", "wiki: " + wiki.getWiki() + " , error_ok: " + wiki.getError_ok());

                    Message msg=new Message();
                    msg.what=HTTP_OK;
                    handler.sendMessage(msg);

                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("HttpActivity","Http request failed...");
                }
            });
        }
    }

    private void sendRequestWithHttpURLConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(MY_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }


        }).start();
    }

    private void showResponse(final String response) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
    private void changePicture(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mImageView.setImageDrawable(getDrawable(R.drawable.pic2));
//                mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.pic2,null));
            }
        });
    }



    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(MY_URL).build();
                    Response response = client.newCall(request).execute();
                    assert response.body() != null;
                    String resData = response.body().string();
//                    showResponse(resData);
//                    parseJSONWithJSONObject(resData);
                    parseJSONWithGSON(resData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {

        Gson gson = new Gson();
        Wiki wiki = gson.fromJson(jsonData, Wiki.class);
        Log.d("HttpActivity", "wiki: " + wiki.getWiki() + " , error_ok: " + wiki.getError_ok());

        new MyPopupWindow().onButtonShowPopupWindowClick(mLinearLayout,this,wiki);

//        setMyPopupWindow();
    }

    private void setMyPopupWindow(View view,Context context) {
        changePicture();
        new MyPopupWindow().onButtonShowPopupWindowClick(view,context,wiki);

    }

    private void parseJSONWithJSONObject(String jsonData) {

        try {
//            JSONArray jsonArray=new JSONArray(jsonData);
//            JSONObject jsonObject=jsonArray.getJSONObject(0);
            JSONObject jsonObject = new JSONObject(jsonData);
            String wiki = jsonObject.getString("wiki");
            String error_ok = jsonObject.getString("error_ok");
//            Toast.makeText(this,wiki+error_ok,Toast.LENGTH_LONG).show();
            Log.d("HttpActivity", "wiki: " + wiki + " , error_ok: " + error_ok);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}