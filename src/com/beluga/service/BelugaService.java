package com.beluga.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.beluga.R;



public class BelugaService extends Service implements OnClickListener {
	
	private WindowManager windowManager;
	private ImageButton serviceBtn, b4;
	private FrameLayout fl, flBar;
	private GridLayout gl;
	WindowManager.LayoutParams params;
	RelativeLayout serviceRelativeLayout;
	DisplayMetrics dm;
	int screenWidth;  
    int screenHeight;
    boolean flFlag = false, glFlag = false;
	@SuppressLint("RtlHardcoded")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		dm = getResources().getDisplayMetrics();  
		screenWidth = dm.widthPixels;  
        screenHeight = dm.heightPixels;              	
  
        
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		 
		params= new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER | Gravity.RIGHT;
		params.x = 0;
		params.y = 100;
		
		LayoutInflater inflater = LayoutInflater.from(this);
		fl = (FrameLayout)inflater.inflate(R.layout.service_layout, null);
		fl.setOnClickListener(this);
		
		flBar = (FrameLayout)inflater.inflate(R.layout.service_grid_layout, null);
		b4 = (ImageButton)flBar.findViewById(R.id.service_btn4);
		b4.setOnClickListener(this);
		/*
		fl.setOnTouchListener(new OnTouchListener(){
			
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					//return true;
				case MotionEvent.ACTION_UP:
					
					float x2 = event.getRawX();
	                float y2 = event.getRawY();
					 if (params.x < (screenWidth / 2)) {
						 //go left
						 params.x = 0;
						 windowManager.updateViewLayout(fl, params);

	                  } else if(params.x > (screenWidth / 2)){
	                	  //go right
	                	  params.x = screenWidth;
	                	  windowManager.updateViewLayout(fl, params);

	                  }
					 double distance = Math.sqrt(Math.abs(initialTouchX-x2)*Math.abs(initialTouchX-x2)+Math.abs(initialTouchY-y2)*Math.abs(initialTouchY-y2));//两点之间的距离
					 // Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));//两点之间的距离
					 if (distance < 15) { // 距离较小，当作click事件来处理 
	                     //showToastDialog("点击了");
						 Log.i("service","clicked...");
	                    return false;
	                 } else {
	                	 Log.i("service","move...");
	                     //showToastDialog("滑动了");
	                    return true ;
	                }
					 
					//return false;
					
				case MotionEvent.ACTION_MOVE:
					params.x = initialX
							+ (int) (event.getRawX() - initialTouchX);
					params.y = initialY
							+ (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(fl, params);
					//return true;
				}
				
				return false;
			}
			
		});
		*/
		flFlag = true;
		windowManager.addView(fl, params);
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (flFlag == true){
			windowManager.removeView(fl);
		}
		if (glFlag == true){
			windowManager.removeView(flBar);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("service","clicked...");
		
		if(v.getId() == R.id.service_frame_layout){
			if (fl != null){
				windowManager.removeView(fl);
				flFlag = false; 
				windowManager.addView(flBar, params);
				glFlag = true;
			}
			
		}else if(v.getId() == R.id.service_btn4){
			if (flBar != null){
				windowManager.removeView(flBar);
				glFlag = false;
				windowManager.addView(fl, params);
				flFlag = true;
			}
		}
	}

}
