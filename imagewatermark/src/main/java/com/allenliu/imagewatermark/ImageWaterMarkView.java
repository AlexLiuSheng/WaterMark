package com.allenliu.imagewatermark;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.allenliu.imagewatermark.DensityUtil.sp2px;


/**
 * Created by Allen Liu on 2017/2/14.狗日的光棍节
 * 水印设置和显示控件
 */

/**
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .   ' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * <p>
 * .............................................
 * 佛祖保佑             永无BUG
 */
public class ImageWaterMarkView extends ImageView implements IWaterMarkMethod {
    /**
     * 图片水印Paint
     */
    private Paint imagePaint;
    private TextPaint textPaint;
    /**
     * 蒙层Paint;
     */
    private Paint layerDashPaint;
    private Paint layerPaint;
    /**
     * 序号Paint
     */
    private Paint indexPaint;
    private int imageWidth;
    private int imageHeight;
    private int bitmapWidth;
    private int bitmapHeight;
    private Paint placeHolderPaint;
    public List<WaterMarkParamBean> paramList;
    /**
     * 缩放比
     */
    float scale = 1;
    float textSize;
    Canvas canvas;
    int startX;
    int startY;
    int endX;
    int endY;
    /**
     * 判断点击区域数组
     */
    private Region[] regions;
    private PotClickListener clickListener;
    public static final int OTHER_REGION_CLICK = 10000;
    boolean isNeedDrawIndex = true;
    boolean isNeedDrawLayer = true;
    boolean isNeedDrawDemoText = true;
    private float textMarkWidth;
    private float textMarkHeight;

    private int layerImagePaintColor = Color.parseColor("#222222");
    private int layerDashPaintColor = Color.parseColor("#FFFFFF");
    private int placeHolderImagePaintColor = Color.parseColor("#FFFFFF");
    private int layerImagePaintAlpha = 51;
    private int placeHolderImagePaintAlpha = 127;
    private Bitmap defaultIndexIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_custom_bg);

    public ImageWaterMarkView(Context context) {
        super(context);
        init();
    }

    public ImageWaterMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageWaterMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void caculateScale() {
        int screenWdith = DensityUtil.getScreenSize(getContext()).x;
        scale = ((float) screenWdith) / bitmapWidth;
        clickListener = null;
        requestLayout();
        invalidate();
    }


    private void init() {
        textSize = sp2px(getContext(), 10);
        imagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        layerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        layerDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indexPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indexPaint.setDither(true);
        placeHolderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paramList = new ArrayList<>();
        layerDashPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        imagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        indexPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        layerPaint.setStyle(Paint.Style.FILL);
        layerPaint.setColor(getLayerImagePaintColor());
        layerPaint.setAlpha(getLayerImagePaintAlpha());
        layerDashPaint.setStyle(Paint.Style.STROKE);
        layerDashPaint.setColor(getLayerDashPaintColor());
        //设置虚线
        DashPathEffect effect = new DashPathEffect(new float[]{4, 4}, 1);
        layerDashPaint.setPathEffect(effect);
        indexPaint.setTextSize(textSize);
        indexPaint.setColor(Color.WHITE);
        setScaleType(ScaleType.FIT_CENTER);
        placeHolderPaint.setStyle(Paint.Style.FILL);
        placeHolderPaint.setColor(getPlaceHolderImagePaintColor());
        placeHolderPaint.setAlpha(getPlaceHolderImagePaintAlpha());
        imagePaint.setColor(Color.WHITE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (bitmapWidth != 0 && bitmapHeight != 0) {
            int screenWdith = DensityUtil.getScreenSize(getContext()).x;
            int imgHeight = (screenWdith * bitmapHeight) / bitmapWidth;
            super.onMeasure(MeasureSpec.makeMeasureSpec(screenWdith, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(imgHeight, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        imageWidth = getMeasuredWidth();
        imageHeight = getMeasuredHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        if (paramList != null && paramList.size() > 0) {
            regions = new Region[paramList.size()];
            for (int i = 0; i < paramList.size(); i++) {
                WaterMarkParamBean waterMarkParamBean = paramList.get(i);
                WaterMarkType type = waterMarkParamBean.getType();

                startX = (int) (waterMarkParamBean.getLeft() * scale);
                startY = (int) (waterMarkParamBean.getTop() * scale);
                endX = (int) (startX + waterMarkParamBean.getWidth() * scale);
                //如果是字体 没有height 那怕是就先计算一哈高度哦
                if (type == WaterMarkType.TYPE_TEXT) {
                    drawText(waterMarkParamBean);
                }
                endY = (int) (startY + waterMarkParamBean.getHeight() * scale);
                caculateBonds(i);
                if (isNeedDrawLayer)
                    drawLayer();
                //纯图片水印
                if (type == WaterMarkType.TYPE_IMG) {
                    drawImage(waterMarkParamBean);
                    //纯文字水印
                }
                //画文字索引
                String text = String.valueOf(i + 1);
                if (isNeedDrawIndex)
                    drawIndex(text);

            }
        }
    }

    public float getTextMarkWidth() {
        return textMarkWidth;
    }

    public float getTextMarkHeight() {
        return textMarkHeight;
    }

    /**
     * 获取X
     *
     * @return
     */
    public float getTextPositionX() {
        return startX;
    }

    /**
     * 获取Y
     *
     * @return
     */
    public float getTextPositionY() {
        return startY;
    }

    /**
     * 画文字水印一
     *
     * @param waterMarkParamBean
     */
    private void drawText(WaterMarkParamBean waterMarkParamBean) {
        //设置透明度
        double opacity = waterMarkParamBean.getOpacity();
        textPaint.setAlpha((int) (255 * opacity));
        textPaint.setColor(Color.parseColor(waterMarkParamBean.getFontColor()));
        Typeface tf;
        if (waterMarkParamBean.getFontFamily() != null) {
            tf = Typeface.createFromFile(waterMarkParamBean.getFontFamily());
        } else {
            tf = Typeface.createFromAsset(getContext().getAssets(), "defaultFont.TTF");
        }
        textPaint.setTypeface(tf);
        textPaint.setTextSize(waterMarkParamBean.getFontSize() * scale);

        String name;
        if (waterMarkParamBean.getUserInputText() != null) {
            name = waterMarkParamBean.getUserInputText();
        } else {
            if (isNeedDrawDemoText)
                name = waterMarkParamBean.getName();
            else
                name = "";
        }
        if (name.length() > waterMarkParamBean.getMaxLength()) {
            Toast.makeText(getContext(), "最多只能设置" + waterMarkParamBean.getMaxLength() + "个字", Toast.LENGTH_SHORT).show();
            name = name.substring(0, waterMarkParamBean.getMaxLength());
        }

        String align = waterMarkParamBean.getAlign();
        Layout.Alignment alignment;
        if(align!=null) {
            if (align.equals("center")) {
                alignment = Layout.Alignment.ALIGN_CENTER;
            } else if (align.equals("left")) {
                alignment = Layout.Alignment.ALIGN_NORMAL;
            } else {
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
            }
        }else{
            alignment = Layout.Alignment.ALIGN_NORMAL;
        }

        //用这B来实现文字自动换行
        int w = Math.abs((int) (waterMarkParamBean.getWidth() * scale));
        StaticLayout layout = new StaticLayout(name, textPaint, w, alignment, 1.0F, 0.0F, false);

        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
        canvas.translate(startX, startY);
        layout.draw(canvas);
        canvas.restore();
        textMarkWidth = layout.getWidth();
        textMarkHeight = layout.getHeight();
        waterMarkParamBean.setHeight(layout.getHeight() / scale);
    }

    /**
     * 画图片
     *
     * @param waterMarkParamBean
     */
    private void drawImage(WaterMarkParamBean waterMarkParamBean) {
        //画图片
        //设置透明度
        double opacity = waterMarkParamBean.getOpacity();
        imagePaint.setAlpha((int) (255 * opacity));
//                    //下载完展位图然后设置
        if (waterMarkParamBean.getWaterMarkPath() != null) {
            imagePaint.setStyle(Paint.Style.FILL);
            imagePaint.setColor(Color.WHITE);
            Bitmap bitmap = BitmapFactory.decodeFile(waterMarkParamBean.getWaterMarkPath());
            canvas.drawBitmap(bitmap, null, new RectF(startX, startY, endX, endY), imagePaint);
        } else {
            if (!isNeedDrawLayer && !isNeedDrawIndex) {
            } else
                canvas.drawRect(new RectF(startX, startY, endX, endY), placeHolderPaint);
        }
    }

    /**
     * 计算边界
     *
     * @param i
     */
    private void caculateBonds(int i) {
        Region re = new Region();
        Path path = new Path();
        path.moveTo(startX - 5 * scale, startY - 5 * scale);
        path.lineTo(endX + 5 * scale, startY - 5 * scale);
        path.lineTo(endX + 5 * scale, endY + 5 * scale);
        path.lineTo(startX - 5 * scale, endY + 5 * scale);
        path.close();
        RectF r = new RectF();
        //计算边界
        path.computeBounds(r, true);
        //设置区域路径和剪辑描述的区域
        re.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
        regions[i] = re;
    }

    /**
     * 画蒙层
     */
    private void drawLayer() {
        //画蒙层
        RectF layerRect = new RectF(startX - 5 * scale, startY - 5 * scale, endX + 5 * scale, endY + 5 * scale);
        canvas.drawRoundRect(layerRect, 4, 4, layerPaint);
        //画虚线
        canvas.drawRoundRect(layerRect, 4, 4, layerDashPaint);

    }

    /**
     * 画索引图
     *
     * @param text
     */
    private void drawIndex(String text) {
        //画索引
        Bitmap index = getDefaultIndexIcon();
        int indexWH = (DensityUtil.dip2px(getContext(), 15));
        int padding = (int) (5);
        RectF rectF = new RectF(endX - (indexWH + padding), endY - (indexWH + padding), endX - padding, endY - padding);
        canvas.drawBitmap(index, null, rectF, indexPaint);

        indexPaint.setTextSize(textSize);
        Paint.FontMetrics fm = indexPaint.getFontMetrics();
        float textH = fm.descent - fm.ascent;
        canvas.drawText(text, rectF.left + rectF.width() / 3, rectF.centerY() + textH / 4, indexPaint);
    }

    boolean isOtherClick = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getY() > imageHeight) {
                    return false;
                } else {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();

                for (int i = 0; i < regions.length; i++) {
                    if (regions[i].contains((int) x, (int) y)) {
                        isOtherClick = false;
                        if (clickListener != null) {
                            clickListener.onClick(this, i);
                            break;
                        }
                        //其他区域点击
                    } else {
                        isOtherClick = true;
                    }
                }
                if (isOtherClick) {
                    if (clickListener != null) {
                        clickListener.onClick(this, OTHER_REGION_CLICK);
                    }
                    isOtherClick = false;
                }
                break;
        }
        return true;
    }


    public String saveBitmapToFile(String filePath) {
        isNeedDrawIndex = false;
        isNeedDrawLayer = false;
        isNeedDrawDemoText = false;
        invalidate();
        String path = bitmapToFile(getContext(), filePath, convertViewToBitmap(this));
        isNeedDrawLayer = true;
        isNeedDrawIndex = true;
        isNeedDrawDemoText = true;
        return path;
    }

    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        //把view中的内容绘制在画布上
        view.draw(canvas);
        return bitmap;
    }

    /**
     * @param context
     * @param bm
     * @return
     */
    private String bitmapToFile(Context context, String filePath, Bitmap bm) {
        if (bm == null)
            return "";
        String name = filePath;
        File myCaptureFile = new File(name);
        try {
            BufferedOutputStream bos = null;
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return name;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否需画index
     *
     * @param isNeedDrawIndex
     */
    @Override
    public IWaterMarkMethod setIsNeedDrawIndex(boolean isNeedDrawIndex) {
        this.isNeedDrawIndex = isNeedDrawIndex;
        invalidate();
        return this;
    }

    @Override
    public boolean isNeedDrawIndex() {
        return isNeedDrawIndex;
    }

    @Override
    public IWaterMarkMethod saveBitmapToFile() {
        return this;
    }

    @Override
    public IWaterMarkMethod setOnDotClickListener(IWaterMarkMethod.PotClickListener l) {
        clickListener = l;
        return this;
    }

    @Override
    public IWaterMarkMethod setIsNeedDrawDemoText(boolean isNeedDrawDemoText) {
        return this;
    }

    @Override
    public IWaterMarkMethod setLayerImagePaintColor(int color) {
        this.layerImagePaintColor = color;
        return this;
    }

    @Override
    public IWaterMarkMethod setLayerImagePaintAlpha(int alpha) {
        layerImagePaintAlpha = alpha;
        return this;
    }

    @Override
    public int getLayerImagePaintAlpha() {
        return layerImagePaintAlpha;
    }

    @Override
    public int getLayerImagePaintColor() {
        return layerImagePaintColor;
    }

    @Override
    public IWaterMarkMethod setLayerDashPaintColor(int color) {
        this.layerDashPaintColor = color;
        return this;
    }

    @Override
    public int getLayerDashPaintColor() {
        return layerDashPaintColor;
    }

    @Override
    public IWaterMarkMethod setPlaceHolderImagePaintColor(int color) {
        this.placeHolderImagePaintColor = color;
        return this;
    }

    @Override
    public int getPlaceHolderImagePaintColor() {
        return placeHolderImagePaintColor;
    }

    @Override
    public IWaterMarkMethod setPlaceHolderImagePaintAlpha(int alpha) {
        placeHolderImagePaintAlpha = alpha;
        return this;
    }

    @Override
    public int getPlaceHolderImagePaintAlpha() {
        return placeHolderImagePaintAlpha;
    }

    /**
     * 是否需要画蒙层
     *
     * @param isNeedDrawLayer
     */
    @Override
    public IWaterMarkMethod setIsNeedDrawLayer(boolean isNeedDrawLayer) {
        this.isNeedDrawLayer = isNeedDrawLayer;
        invalidate();
        return this;
    }

    @Override
    public boolean isNeedDrawLayer() {
        return isNeedDrawLayer;
    }

    /**
     * 设置水印数据
     */
    @Override
    public IWaterMarkMethod setWaterMarkData(List<WaterMarkParamBean> paramList) {
        if (this.paramList == null)
            this.paramList = paramList;
        else {
            this.paramList.clear();
            this.paramList.addAll(paramList);
        }
        invalidate();
        return this;
    }

    @Override
    public IWaterMarkMethod setDefaultIndexIcon(Bitmap icon) {

        defaultIndexIcon = icon;
        return this;
    }

    @Override
    public Bitmap getDefaultIndexIcon() {
        return defaultIndexIcon;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (bm != null) {
            bitmapWidth = bm.getWidth();
            bitmapHeight = bm.getHeight();
            caculateScale();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable != null) {
            bitmapWidth = drawable.getIntrinsicWidth();
            bitmapHeight = drawable.getIntrinsicHeight();
            caculateScale();
        }
    }

    public void setGravity(int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.gravity = gravity;
        setLayoutParams(params);
    }
}
