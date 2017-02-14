package com.allenliu.imagewatermark;

import android.graphics.Bitmap;
import android.view.View;

import java.util.List;

/**
 * Created by Allen Liu on 2017/2/14.
 */

public interface IWaterMarkMethod {
     IWaterMarkMethod setLayerImagePaintColor(int color);
    IWaterMarkMethod setLayerImagePaintAlpha(int alpha);
    int getLayerImagePaintAlpha();
    int getLayerImagePaintColor();
    IWaterMarkMethod setLayerDashPaintColor(int color);
    int getLayerDashPaintColor();
    IWaterMarkMethod setPlaceHolderImagePaintColor(int color);
    int getPlaceHolderImagePaintColor();
    IWaterMarkMethod setPlaceHolderImagePaintAlpha(int alpha);
    int getPlaceHolderImagePaintAlpha();
    IWaterMarkMethod setIsNeedDrawLayer(boolean isNeed);
    boolean isNeedDrawLayer();
    IWaterMarkMethod setIsNeedDrawIndex(boolean isNeedDrawIndex);
    boolean isNeedDrawIndex();
    IWaterMarkMethod saveBitmapToFile();
    IWaterMarkMethod setOnDotClickListener(IWaterMarkMethod.PotClickListener l);
    IWaterMarkMethod setIsNeedDrawDemoText(boolean isNeedDrawDemoText);
    IWaterMarkMethod setWaterMarkData(List<WaterMarkParamBean> paramList);
    IWaterMarkMethod setDefaultIndexIcon(Bitmap icon);
    Bitmap getDefaultIndexIcon();



   interface PotClickListener {
        void onClick(View v, int position);
    }


}
