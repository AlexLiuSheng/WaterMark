package com.allenliu.watermark;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.allenliu.imagewatermark.ImageWaterMarkView;
import com.allenliu.imagewatermark.WaterMarkParamBean;
import com.allenliu.imagewatermark.WaterMarkType;

import java.util.ArrayList;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {
    ImageWaterMarkView waterMarkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waterMarkView= (ImageWaterMarkView) findViewById(R.id.watermarkview);
        waterMarkView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.demo));
        WaterMarkParamBean paramBean = new WaterMarkParamBean().setType(WaterMarkType.TYPE_IMG)
                .setLeft(100)
                .setTop(100)
                .setWidth(200)
                .setHeight(200);
        WaterMarkParamBean paramBean2 = new WaterMarkParamBean().setType(WaterMarkType.TYPE_IMG)
                .setLeft(500)
                .setTop(500)
                .setWidth(800)
                .setHeight(800);
        WaterMarkParamBean paramBean3 = new WaterMarkParamBean().setType(WaterMarkType.TYPE_TEXT)
                .setLeft(300)
                .setTop(300)
                .setWidth(800)
                .setHeight(500)
                .setUserInputText("测试测试")
                .setFontColor("#000000")
                .setFontSize(200);
        ArrayList<WaterMarkParamBean> list = new ArrayList<>();
        list.add(paramBean2);
        list.add(paramBean3);
        list.add(paramBean);
        waterMarkView.setWaterMarkData(list);
    }
}
