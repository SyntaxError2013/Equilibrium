package in.co.sdslabs.sketchbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
	
	
	//Initialise required variables
	private Path drawPath;
	private Paint drawPaint , canvasPaint;
	private int color = 0xFF000000;
	private Canvas canvas;
	private Bitmap bmp;
	
	private float brushSize , lastBrushSize;
	
	private boolean erase=false;
	
	public DrawingView(Context context , AttributeSet attr) {
		super(context , attr);
		setupDrawing();
		
	}
	
	private void setupDrawing()
	{
		
		
		brushSize = getResources().getInteger(R.integer.small_size);
		lastBrushSize = brushSize;
		
		drawPath = new Path();
		drawPaint = new Paint();
		
		drawPaint.setColor(color);
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
	
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bmp);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
	//draw view
		canvas.drawBitmap(bmp, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//detect user touch
		
		float touchX = event.getX();
		float touchY = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    drawPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
		    canvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
		    return false;
		}
		
		invalidate();
		return true;
		
	}
	
	public void startNew(){
	   canvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    invalidate();
	}
	
	public void setBrushSize(float newSize){
		//update size
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
			    newSize, getResources().getDisplayMetrics());
			brushSize=pixelAmount;
			drawPaint.setStrokeWidth(brushSize);
		}
	
	public void setLastBrushSize(float lastSize){
	    lastBrushSize=lastSize;
	}
	
	public float getLastBrushSize(){
	    return lastBrushSize;
	}
	
	public void setErase(boolean isErase){
		//set erase true or false
		erase=isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
		}
	
	public void setColor(String newColor){
		//set color
		invalidate();
		color = Color.parseColor(newColor);
		drawPaint.setColor(color);
		}
}
