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

public class help extends Activity 
{
	ImageView img;
//Activity를 상속
	ArrayList<String> data;
//리스트 를 data에 저장
//스트링은 문자열을 저장하는기능
	@Override
	public void onCreate(Bundle savedInstanceState) {
//안드로이드는 최초로 oncreat메소드를 호출합니다
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
//main과 연결시켜줍니다
		
//데이터를 세팅하는 곳입니다
//add메소드로 추가가 가능합니다
		data = new ArrayList<String>();
		data.add("HTML5 기본 구성");
		data.add("태그");
		data.add("h 태그");

//어댑터 를 생성하는 구간입니다
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_list_item_1, data);
//R로부터 id값을 가져옵니다
//어댑터 를 연결합니다
		ListView list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);

//클릭 이벤트 설정구간입니다
//셋 온 아이템 클릭 리스너 호출!
		list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					{ if (data.get(position) == "HTML5 기본 구성")
//a클릭 됬을때 입니다
						{
							
							AlertDialog.Builder builder = new
							AlertDialog.Builder(help.this);
							builder.setTitle("HTML5 기본 구성");
							builder.setMessage("HTML 5 의 기본 틀을 알아봅시다\n\n<html> : html 의 작성 시작을 알리는 태그\n<head> : html 의 머리 부분을 의미\n(주로 meta 또는 style 적용할 때 주로 쓰임\n[물론 개인 차이가 있음])\n</head> : html 의 머리 부분을 끝냄\n<body> : html 의 몸통 부분을 의미\n(주로 대부분의 태그는 이곳에 적습니다)\n</body> : html 의 몸통을 종료\n</html> : html 의 작성을 종료");
							ImageView img = new ImageView(help.this);
							img.setImageResource(R.drawable.html_home);
							builder.setView(img);
							builder.show();
						
						}

						if (data.get(position) == "태그")
						{
							AlertDialog.Builder builder = new
							AlertDialog.Builder(help.this);
							builder.setTitle("태그");
						    builder.setMessage("태그는 HTML5 의 명령어로\n모든 태그는 < > 안에 태그를 넣어 줍니다\n(즉, <태그> 입니다)\n또한 태그를 끝낼 땐 </ > 안에 끝내는 태그를 넣어줍니다\n(즉, </태그> 입니다)\n하지만 /> 로 끝내야되는 태그도 존재하니 주의하시기 바랍니다");
							ImageView img = new ImageView(help.this);
							img.setImageResource(R.drawable.tag);
							builder.setView(img);
							builder.show();
							
							}
						if (data.get(position) == "h 태그")
						{
							AlertDialog.Builder builder = new
							AlertDialog.Builder(help.this);
							builder.setTitle("h 태그");
							builder.setMessage("h 태그는 글씨를 쓸 수 있는 태그입니다\nh 태그는 <h1> ~ <h6> 까지 있습니다\nh 태그는 숫자의 크기가 작을수록\n글씨 크기가 커집니다\n(최고 크기 : h1 , 최소 크기 : h6)");
							ImageView img = new ImageView(help.this);
							img.setImageResource(R.drawable.h);
							builder.setView(img);
							builder.show();

						}
					}
				}});}}
