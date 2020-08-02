package com.conti.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreadActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text;

    public static  final int CHANGE_TEXT=1;


    private  Handler handler=new Handler(new Handler.Callback() {

        public boolean handleMessage(Message message){
            switch (message.what){
                case CHANGE_TEXT:
                    text.setText("Nice to meet you.");
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        text=findViewById(R.id.text_changed);
        Button changeText=findViewById(R.id.change_text);
        changeText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        text.setText("Nice to be alive.");
                        Message msg=new Message();
                        msg.what=CHANGE_TEXT;
                        handler.sendMessage(msg);
                    }
                }).start();

                break;
            default:
                break;
        }
    }
}