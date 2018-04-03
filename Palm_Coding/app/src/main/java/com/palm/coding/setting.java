package com.palm.coding;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;
import android.graphics.drawable.*;
import org.apache.commons.codec.net.*;

import java.util.ArrayList; 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.*;
import android.content.*;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.net.*;

public class setting extends Activity 
{
	int numtest=0; // 크기에 관련된 숫자 제한을 위해 만듦
	String title; // 다이얼로그 제목
	String color; // 색상 코드
	
	ArrayList<String> in;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		Toast.makeText(setting.this, "setting 액티비티 시작", Toast.LENGTH_SHORT).show();
		
		Button text_size = (Button) findViewById(R.id.text_size);
		text_size.setOnClickListener(new OnClickListener() { 
		public void onClick(View v) { 
		
			AlertDialog.Builder ts = new
			AlertDialog.Builder(setting.this);
			ts.setTitle("글자 크기");
			ts.setMessage("사이즈는 pt 이며\n10 ~ 50 사이 숫자를 입력하세요");
			final EditText tsize = new EditText(setting.this);
			tsize.setText("22");
			tsize.setHint("기본 22");
			tsize.setTextColor(Color.BLACK);
			ts.setView(tsize);
			ts.setNegativeButton("취소", new
				DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}});

			ts.setPositiveButton("확인", new
				DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 여기서부터 변경 이벤트 추가해야함
						numtest = Integer.parseInt(tsize.getText().toString());
						if (numtest >= 10 && numtest <= 50) {
							Intent ts = new Intent(setting.this,MainActivity.class);
							ts.putExtra("ts",tsize.getText().toString());
						}
						else
							Toast.makeText(setting.this, "10 ~ 50 사이로 입력해주세요", Toast.LENGTH_SHORT).show();
						// 여기까지
					}});
			ts.show();
	
		}});
	    
		// 배경
		Button back = (Button) findViewById(R.id.background);
		back.setOnClickListener(new OnClickListener() { 
			public void onClick(View v) { 

			Toast.makeText(setting.this, "정상 작동", Toast.LENGTH_SHORT).show();

		}});
		
		// 글자 색상
		Button tc = (Button) findViewById(R.id.tc);
		tc.setOnClickListener(new OnClickListener() { 
				public void onClick(View v) { 

					Toast.makeText(setting.this, "정상 작동", Toast.LENGTH_SHORT).show();
					set();
					
		}});
		
		}
	
	// 색상 private
	private void set(){
		
		AlertDialog.Builder set = new
		AlertDialog.Builder(setting.this);
		set.setTitle("색상");
		set.setMessage("색상 코드 (예 : #000000)");
		final EditText tc = new EditText(setting.this);
		tc.setText("#000000");
		tc.setHint("기본 #000000");
		tc.setTextColor(Color.BLACK);
		
		in = new ArrayList<String>();
		in.add("빨강");
		in.add("초록");
		in.add("파랑");

//어댑터 생성
		ArrayAdapter<String> adapter = new ArrayAdapter<String>
		(this, android.R.layout.simple_list_item_1, in);

//어댑터 연결
		ListView list = new ListView(setting.this);
		list.setAdapter(adapter);

//클릭 이벤트 설정
		list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					if (in.get(position) == "빨강") {
						color = "ff0000";
						Intent ts = new Intent(setting.this,MainActivity.class);
						ts.putExtra("ts",color);
					}

					else if (in.get(position) == "초록") {
						color = "00ff00";
						Intent ts = new Intent(setting.this,MainActivity.class);
						ts.putExtra("ts",color);
					}

					else if (in.get(position) == "파랑") {
						color = "0000ff";
						Intent ts = new Intent(setting.this,MainActivity.class);
						ts.putExtra("ts",color);
					}

				}
			});
		
		set.setView(list);
		set.setNegativeButton("취소", new
			DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}});
		
		set.show();
		}
		
	@Override
    protected void onDestroy() {
        Toast.makeText(getApplicationContext(),"적용 완료",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
		
		}
