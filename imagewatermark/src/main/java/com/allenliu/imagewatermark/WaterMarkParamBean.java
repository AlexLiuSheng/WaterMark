package com.allenliu.imagewatermark;

import java.io.Serializable;

/**
 * Created by Allen Liu on 2017/1/11.
 */

public class WaterMarkParamBean implements Serializable {
  public static final int TYPE_TEXT=1;
    public static  final int TYPE_IMG=2;
    /**
     * width : 100
     * height : 200
     * left : 223
     * top : 119
     * zoom : 1
     * opacity : 0.7
     * type : template
     * path : http://source-lib.b0.upaiyun.com//uploads//2017-01-03/2834844c5dfb530960c2939d0aea4a2c.jpg
     * id : 3
     */

    private float width;
    private float height;
    private float left;
    private float top;
    private int zoom;
    private double opacity=1;
    private WaterMarkType type;
    private String path;
    private String id;
    private float fontSize;
    private String fontColor;
    private String fontFamily;
    private String rotate;
    private String name;
    //字体网络相对地址
    private String fontPath;
    private boolean isDownloadPlaceHolder=false;
    private String waterMarkPath;
    private String align;
    private int maxLength=Integer.MAX_VALUE;
    private String userInputText;

    public float getWidth() {
        return width;
    }

    public WaterMarkParamBean setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public WaterMarkParamBean setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getLeft() {
        return left;
    }

    public WaterMarkParamBean setLeft(float left) {
        this.left = left;
        return this;
    }

    public float getTop() {
        return top;
    }

    public WaterMarkParamBean setTop(float top) {
        this.top = top;
        return this;
    }

    public int getZoom() {
        return zoom;
    }

    public WaterMarkParamBean setZoom(int zoom) {
        this.zoom = zoom;
        return this;
    }

    public double getOpacity() {
        return opacity;
    }

    public WaterMarkParamBean setOpacity(double opacity) {
        this.opacity = opacity;
        return this;
    }

    public WaterMarkType getType() {
        return type;
    }

    public WaterMarkParamBean setType(WaterMarkType type) {
        this.type = type;
        return this;
    }

    public String getPath() {
        return path;
    }

    public WaterMarkParamBean setPath(String path) {
        this.path = path;
        return this;
    }

    public String getId() {
        return id;
    }

    public WaterMarkParamBean setId(String id) {
        this.id = id;
        return this;
    }

    public float getFontSize() {
        return fontSize;
    }

    public WaterMarkParamBean setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public String getFontColor() {
        return fontColor;
    }

    public WaterMarkParamBean setFontColor(String fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public WaterMarkParamBean setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    public String getRotate() {
        return rotate;
    }

    public WaterMarkParamBean setRotate(String rotate) {
        this.rotate = rotate;
        return this;
    }

    public String getName() {
        return name;
    }

    public WaterMarkParamBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getFontPath() {
        return fontPath;
    }

    public WaterMarkParamBean setFontPath(String fontPath) {
        this.fontPath = fontPath;
        return this;
    }

    public boolean isDownloadPlaceHolder() {
        return isDownloadPlaceHolder;
    }

    public WaterMarkParamBean setDownloadPlaceHolder(boolean downloadPlaceHolder) {
        isDownloadPlaceHolder = downloadPlaceHolder;
        return this;
    }

    public String getWaterMarkPath() {
        return waterMarkPath;
    }

    public WaterMarkParamBean setWaterMarkPath(String waterMarkPath) {
        this.waterMarkPath = waterMarkPath;
        return this;
    }

    public String getAlign() {
        return align;
    }

    public WaterMarkParamBean setAlign(String align) {
        this.align = align;
        return this;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public WaterMarkParamBean setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public String getUserInputText() {
        return userInputText;
    }

    public WaterMarkParamBean setUserInputText(String userInputText) {
        this.userInputText = userInputText;
        return this;
    }
}
