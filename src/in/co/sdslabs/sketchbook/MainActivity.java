package in.co.sdslabs.sketchbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	private DrawingView drawView;
	
	private ImageButton newFile , brush , eraser , saveFile , colorChoose;
	private float smallBrush , mediumBrush , largeBrush;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawView = (DrawingView) findViewById(R.id.drawing);
		
		newFile = (ImageButton) findViewById(R.id.newFile);
		saveFile = (ImageButton) findViewById(R.id.saveFile);
		brush = (ImageButton) findViewById(R.id.brush);
		eraser = (ImageButton) findViewById(R.id.eraser);
		colorChoose = (ImageButton) findViewById(R.id.colorchoose);
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);
		
		newFile.setOnClickListener(this);
		saveFile.setOnClickListener(this);
		brush.setOnClickListener(this);
		eraser.setOnClickListener(this);
		colorChoose.setOnClickListener(this);
		
		drawView.setBrushSize(mediumBrush);
	}
	
	private void SaveImage(Bitmap finalBitmap) {

	    String root = Environment.getExternalStorageDirectory().toString();
	    File sketchbk = new File("/sdcard/SketchBook");
    	sketchbk.mkdirs();
    	Random generator = new Random();
    	int n = 10000;
    	n = generator.nextInt(n);
    	String enname = "Image-"+ n +".jpg";
    	File file = new File (sketchbk,enname);
    	if(file.exists()) file.delete();
	    try {
	           FileOutputStream out = new FileOutputStream(file);
	           finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	           out.flush();
	           out.close();

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.newFile : 
		{
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        drawView.startNew();
			        dialog.dismiss();
			    }
			});
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			newDialog.show();
			
			break;
		}
		case R.id.saveFile :
		{
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			    	drawView.setDrawingCacheEnabled(true);
			    	
			    	// Custom Location to be done 
			    	/*String imgSaved = MediaStore.Images.Media.insertImage(
			    		    getContentResolver(), drawView.getDrawingCache(),
			    		    UUID.randomUUID().toString()+".png", "drawing");*/
			    	SaveImage(drawView.getDrawingCache());
			    	sendBroadcast(new Intent(
			    			Intent.ACTION_MEDIA_MOUNTED,
			    			            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			    	
			    	
			    	//if(imgSaved!=null){
			    	    Toast savedToast = Toast.makeText(getApplicationContext(),
			    	        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
			    	    savedToast.show();
			    	//}
			    	/*else{
			    	    Toast unsavedToast = Toast.makeText(getApplicationContext(),
			    	        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
			    	    unsavedToast.show();
			    	}*/
			    }
			});
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			saveDialog.show();
			
			break;
			
		}
		case R.id.brush :
		{
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(smallBrush);
			        drawView.setLastBrushSize(smallBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(mediumBrush);
			        drawView.setLastBrushSize(mediumBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(largeBrush);
			        drawView.setLastBrushSize(largeBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			
			brushDialog.show();
			
			break;
		}
		case R.id.eraser :
		{
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setErase(true);
			        drawView.setBrushSize(smallBrush);			    
			        brushDialog.dismiss();
			    }
			});
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setErase(true);
			        drawView.setBrushSize(mediumBrush);
			        brushDialog.dismiss();
			    }
			});
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setErase(true);
			        drawView.setBrushSize(largeBrush);			        
			        brushDialog.dismiss();
			    }
			});
			
			brushDialog.show();
			
			break;
		}
		case R.id.colorchoose :
		{
			final Dialog colorDialog = new Dialog(this);
			colorDialog.setTitle("Choose color : ");
			colorDialog.setContentView(R.layout.color_chooser);
			
			
			
			ImageButton redBtn = (ImageButton)colorDialog.findViewById(R.id.color_red);
			redBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        
			    	String color = v.getTag().toString();
			    	drawView.setColor(color);
			    	drawView.setErase(false);
			        colorDialog.dismiss();
			    }
			});
			
			ImageButton  greenBtn = (ImageButton)colorDialog.findViewById(R.id.color_green);
			greenBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        
			    	String color = v.getTag().toString();
			    	drawView.setColor(color);
			    	drawView.setErase(false);
			        colorDialog.dismiss();
			    }
			});
			
			ImageButton pinkBtn = (ImageButton)colorDialog.findViewById(R.id.color_pink);
			pinkBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        
			    	String color = v.getTag().toString();
			    	drawView.setColor(color);
			    	drawView.setErase(false);
			        colorDialog.dismiss();
			    }
			});
			
			ImageButton blueBtn = (ImageButton)colorDialog.findViewById(R.id.color_blue);
			blueBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        
			    	String color = v.getTag().toString();
			    	drawView.setColor(color);
			    	drawView.setErase(false);
			        colorDialog.dismiss();
			    }
			});
			
			ImageButton brownBtn = (ImageButton)colorDialog.findViewById(R.id.color_brown);
			brownBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        
			    	String color = v.getTag().toString();
			    	drawView.setColor(color);
			    	drawView.setErase(false);
			        colorDialog.dismiss();
			    }
			});
			
			ImageButton blackBtn = (ImageButton)colorDialog.findViewById(R.id.color_black);
			blackBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        
			    	String color = v.getTag().toString();
			    	drawView.setColor(color);
			    	drawView.setErase(false);
			        colorDialog.dismiss();
			    }
			});
			
			
			colorDialog.show();
		}
		
		}
		
		
		
	}
	
	

}
