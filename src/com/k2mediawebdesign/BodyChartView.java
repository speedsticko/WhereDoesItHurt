package com.k2mediawebdesign;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BodyChartView extends ImageView  {

	private final static int START_DRAGGING = 0;
	private final static int STOP_DRAGGING = 1;

	private int status;

	
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Rect mRect = new Rect();
    private final Paint mPaint = new Paint();
    private float mCurX;
    private float mCurY;
    
    
    
	public BodyChartView(Context context)
	{
		super(context);
		build_view();
	}

	public BodyChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		build_view();
	}
	
	
	public BodyChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		build_view();
	}
	
	private void build_view()
	{
		setBackgroundColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setARGB(255, 255, 255, 255);
	}
	
	  private void drawPoint(float x, float y, float pressure, float width) {
          //Log.i("TouchPaint", "Drawing: " + x + "x" + y + " p="
          //        + pressure + " width=" + width);
          if (width < 1) width = 1;
          if (mBitmap != null) {
              float radius = width;
              int pressureLevel = (int)(pressure * 255);
              mPaint.setARGB(pressureLevel, 255, 255, 255);
              mCanvas.drawCircle(x, y, radius, mPaint);
              mRect.set((int) (x - radius - 2), (int) (y - radius - 2),
                      (int) (x + radius + 2), (int) (y + radius + 2));
              invalidate(mRect);
          }

      }
	
/*    @Override protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	
    	
    	Paint paint = new Paint();
    	paint.setColor(Color.BLUE);
    	for(PainSticker sticker : markers){
    		canvas.drawCircle(sticker.x, sticker.y, 20, paint);	
    	}
    	
        if (mBitmap != null) {
            //canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }*/
	

    
	@Override
	public boolean onTouchEvent(android.view.MotionEvent event) {
        int action = event.getActionMasked();
        
        float x = event.getX();
        float y = event.getY();
        
        PainSticker sticker = new PainSticker();
        sticker.x = x;
        sticker.y = y;
        sticker.intensity = 0;
        
        //markers.add(sticker);
        

        invalidate();
        return true;
	}
	
    @Override protected void onSizeChanged(int w, int h, int oldw,
            int oldh) {
        int curW = mBitmap != null ? mBitmap.getWidth() : 0;
        int curH = mBitmap != null ? mBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }
        
        if (curW < w) curW = w;
        if (curH < h) curH = h;
        
        Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
                                               Bitmap.Config.RGB_565);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (mBitmap != null) {
            newCanvas.drawBitmap(mBitmap, 0, 0, null);
        }
        mBitmap = newBitmap;
        mCanvas = newCanvas;
    }
}
