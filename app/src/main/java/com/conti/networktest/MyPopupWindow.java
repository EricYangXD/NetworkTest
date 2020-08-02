package com.conti.networktest;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;


public class MyPopupWindow extends Application {
    private static MyPopupWindow mInstance = null;

    public void onButtonShowPopupWindowClick(View view, Context context, Wiki wiki) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_window, null, false);//上面两行的缩写
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.popup_window_border_radius, null));
        if (wiki.getError_ok().equals("1")) {
            ImageView imageView = popupView.findViewById(R.id.poi_pic_name);
//            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic2, null));
            Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.pic2);
            imageView.setImageBitmap(bitmap);
        }
        TextView textView = popupView.findViewById(R.id.poi_desc0);
        textView.setText("景区地点：" + wiki.getWiki());

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(20);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }


    @Override
    public Context getApplicationContext() {
        return null;
    }

    public static MyPopupWindow getInstance() {
        if (mInstance == null) {
            synchronized (MyPopupWindow.class) {
                if (mInstance == null) {
                    mInstance = new MyPopupWindow();
                }
            }
        }
        return mInstance;
    }
}

