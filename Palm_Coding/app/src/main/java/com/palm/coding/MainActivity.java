package com.palm.coding;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import android.view.View.OnClickListener;

public class MainActivity extends Activity implements TextWatcher
{
	
	ConnectivityManager cManager; 
	NetworkInfo mobile; 
	NetworkInfo wifi; 
	
	private BackPressCloseHandler backPressCloseHandler;
	
	private EditText coding;
	TextView ln,fina,cksa;
	String filena;
	
	int dir_int=0; // 폴더 클릭 x : 0 , 클릭 o : 1
	
	String code; // 사용자가 작성한 html5 코드 저장
	
	private String of; // 파일 경로
	private String dir; // 파일 경로 저장
	
	private String Back; // 다이얼로그 글자 변경
	
	int savenum=0; // 0 은 저장 안함, 1 은 저장함
	int sa=2; // 새 파일 또는 불러오기 안한 상태에서 저장 막기 위한 숫자
	int setnum=22; // 글자 크기 (기본 22)
	
	String bcc="#ffffff";
	String tcc="#000000";
	
	ArrayList<String> LV;
	ArrayList<String> on;
	// ListView LV; // ListView
	String ext; // 파일 확장자
	
	public static String defaultUrl = "http://secretloca.dothome.co.kr/VerCheak.html";

	Handler handler = new Handler();
	
	InputMethodManager iManager;
	
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	
		savenum = 0;
		fina.setText(filena);
		cksa.setText("*");
		
		ln.setText("");
		if (coding.getLineCount()>=0) {
			for (int i=1; i<=coding.getLineCount(); i++) {
				ln.append(i+"\n");
		}}
		
	}
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		of = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"PalmCoding";
		dir = of;
		
		coding = (EditText)findViewById(R.id.coding);
		coding.setTextSize(setnum);
	
		fina = (TextView)findViewById(R.id.fina);
		ln = (TextView)findViewById(R.id.ln);
		ln.setTextSize(setnum);
		ln.setText("1");
		
		coding.addTextChangedListener(this);

		// 실행 시 입력 못하게 막는 코드
		if (sa == 2) {
			coding.setEnabled(false);
		}
		// 여기까지 실행 시 입력 못하게 막는 코드
		
		cksa = (TextView)findViewById(R.id.cksa);
		
		final Button save = (Button) findViewById(R.id.save);
		
		coding = (EditText) findViewById(R.id.coding);
		coding.setHint("새 파일 또는 불러오기를 사용하세요");
		
		iManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		
		// 글자 크기 변경 이벤트
		Intent intent = this.getIntent();
		String tss = intent.getStringExtra("ts");
		if (tss == null) {
		ln.setTextSize(setnum);
		coding.setTextSize(setnum);
		}
		else {
		setnum = Integer.parseInt(tss); 
		Toast.makeText(this, "변경됨", Toast.LENGTH_SHORT).show();
		ln.setTextSize(setnum);
		coding.setTextSize(setnum);
		}
		 
		// 배경 색상 변경 이벤트
		bcc = intent.getStringExtra("bc");
		if (bcc == null) {
			ln.setTextSize(setnum);
			coding.setTextSize(setnum);
		}
		else {
			Toast.makeText(this, "변경됨", Toast.LENGTH_SHORT).show();
			coding.setBackgroundColor(Color.parseColor(bcc));
		}
		
		// 글자 색상 변경 이벤트
		tcc  = intent.getStringExtra("tc");
		if (tcc == null) {
			ln.setTextSize(setnum);
			coding.setTextSize(setnum);
		}
		else {
			Toast.makeText(this, "변경됨", Toast.LENGTH_SHORT).show();
			coding.setTextColor(Color.parseColor(tcc));
		}
		
		// 불러오기 버튼 이벤트
		Button open = (Button) findViewById(R.id.open);
		open.setOnClickListener(new OnClickListener() { 
		public void onClick(View v) { 
		
		onTextRead();
		
		fina.setText(filena);
		}});
		
		// 저장 버튼 이벤트
		save.setOnClickListener(new OnClickListener() { 
		public void onClick(View v) { 
		
		if (sa == 2) {
			Toast.makeText(MainActivity .this, "파일을 불러오시거나 새 파일을 만드세요!", Toast.LENGTH_SHORT).show();
		}
		else {
		String title = fina.getText().toString().trim();
		if(title.length()>0){ 
		onTextWriting(title,coding.getText().toString());
        }
		savenum=1;
			
		fina.setText(filena);
		cksa.setText("");
		}
		}});
		
		// 새 파일 버튼의 이벤트
		Button setcode = (Button) findViewById(R.id.setcode);
		setcode.setOnClickListener(new OnClickListener() { 
		public void onClick(View v) { 
		
		// 다이얼로그
			AlertDialog.Builder fl = new
			AlertDialog.Builder(MainActivity.this);
			fl.setTitle("파일 확장자 선택");
			
			LV = new ArrayList<String>();
			LV.add(".html (HTML5)");
			LV.add(".css (CSS 3)");
			LV.add(".js (Java Script)");
			LV.add("테스트 코드 html");
			
			//어댑터 생성
			ArrayAdapter<String> adapter = new ArrayAdapter<String>
			(MainActivity.this, android.R.layout.simple_list_item_1, LV);

			ListView el = new ListView(MainActivity.this);
			el.setAdapter(adapter);

			fl.setView(el);
			
			el.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						
						if (LV.get(position) == ".html (HTML5)") {
							ext = ".html";
							NewFile();
						}
						else if (LV.get(position) == ".css (CSS 3)") {
							ext = ".css";
							NewFile();
						}
						else if (LV.get(position) == ".js (Java Script)") {
							ext = ".js";
							NewFile();
						}
						else if (LV.get(position) == "테스트 코드 html") {
							ext = "테스트 코드 html";
							NewFile();
						}
					}});
			
			fl.setNegativeButton("취소", new
				DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}});
			fl.show();
					}
			
		// 다이얼로그
		private void NewFile(){
			
			AlertDialog.Builder builder = new
			AlertDialog.Builder(MainActivity.this);
			builder.setTitle("경고");
			builder.setMessage("새 파일을 생성할 경우 \n작성 중인 파일은 닫힙니다\n그래도 생성하시겠습니까?\n" + ext + " 은 생략하여 입력해주세요\n(저장 또는 불러오기, 삭제때 사용됩니다)\n(공백일 시 파일 저장을 할 수 없습니다\n파일을 불러오시거나 새 파일을 만드세요)");
			final EditText nwfi = new EditText(MainActivity.this);
			nwfi.setHint(ext + " 파일의 이름을 적어주세요");
			nwfi.setTextColor(Color.BLACK);
			builder.setView(nwfi); // 이 부분 때문에 앱이 강제 종료됨
			builder.setNegativeButton("취소", new
				DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}});
			
			builder.setPositiveButton("확인", new
			DialogInterface.OnClickListener() {
				@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if (ext == ".html") {
						
						coding.setText("<!DOCTYPE html>\n<html>\n<head>\n\n<meta charset=\"UTF-8\" />\n<title> New Title </title>\n\n</head>\n<body>\n\n\n\n</body>\n</html>");
						filena = nwfi.getText().toString()+ext;
						fina.setText(filena);
						cksa.setText("*");
						Toast.makeText(MainActivity .this, "파일 이름 : " + filena, Toast.LENGTH_SHORT).show();
						sa = 0;
						coding.setEnabled(true);
						}
						
						else if (ext == ".css") {

							filena = nwfi.getText().toString()+ext;
							coding.setText("/* CSS ("+filena+") */\n@charset \"UTF-8\";");
							fina.setText(filena);
							cksa.setText("*");
							Toast.makeText(MainActivity .this, "파일 이름 : " + filena, Toast.LENGTH_SHORT).show();
							sa = 0;
							coding.setEnabled(true);
						}
						
						else if (ext == ".js") {

							filena = nwfi.getText().toString()+ext;
							coding.setText("/* Java Script ("+filena+") */");
							fina.setText(filena);
							cksa.setText("*");
							Toast.makeText(MainActivity .this, "파일 이름 : " + filena, Toast.LENGTH_SHORT).show();
							sa = 0;
							coding.setEnabled(true);
						}
						else if (ext == "테스트 코드 html") {
							
							String TestCode="<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>HTML5 테스트코드</title>\n<style type=\"text/css\">canvas\n { border:5px solid black;}\n</style>\n<script type=\"text/javascript\">\nvar x = 30;\nvar y = 30;\nvar r = 20;\nvar xspeed = 3;\nvar yspeed = 3;\n//var prevTime = 0;\nfunction drawArc() {	\nvar c = document.getElementById(\"canvas1\");\n	if(!c.getContext) {	\n	alert('Canvas그리기를 지원하는 웹브라우저가 아닙니다');	\n	return;	}	\nvar ctx = c.getContext(\"2d\");	\nctx.clearRect(0,0,c.width,c.height);\n	x += xspeed;	\ny += yspeed;	\nctx.beginPath();\n	ctx.arc(x,y,r, 0, (Math.PI/180)*360);\n	ctx.strokeStyle='rgb(0,0,0)';\n	ctx.stroke();\n	if((x-r)<=0) {//왼쪽끝	\n	x=r;	\n	xspeed *= -1	\n}else if((y-r)<=0) {//상단	\n	y=r;	\n	yspeed *= -1	\n}else if((x+r)>=c.width) {	\n	x = c.width-r;	\n	xspeed *= -1	\n}else if((y+r)>=c.height) {	\n	y = c.height-r;	\n	yspeed *= -2	}	\n/*	var now = Date.now();	console.log(now-prevTime);	prevTime = now;	*/}var intval;function moveBall() {	intval = setInterval(function(){drawArc();}, 30);}function stopBall() {	clearInterval(intval);}</script></head><body><canvas id=\"canvas1\" width=\"300\" height=\"200\"></canvas><p><input type=\"button\" value=\"START\" onclick=\"moveBall();\"><input type=\"button\" value=\"STOP\" onclick=\"stopBall();\"></body></html>";
							
							filena = nwfi.getText().toString()+".html";
							coding.setText(TestCode);
							fina.setText(filena);
							cksa.setText("*");
							Toast.makeText(MainActivity .this, "파일 이름 : " + filena, Toast.LENGTH_SHORT).show();
							sa = 0;
							coding.setEnabled(true);
						}
						
					}});
					
			builder.show();
		}});
		
		// 웹뷰로 확인 버튼의 이벤트
		Button webview = (Button) findViewById(R.id.webview);
		webview.setOnClickListener(new OnClickListener() { 
		public void onClick(View v) { 
		
		if (sa == 2) {
			Toast.makeText(MainActivity .this, "파일을 불러오시거나 새 파일을 만드세요!", Toast.LENGTH_SHORT).show();
		}
		else if (savenum == 0) {
			Toast.makeText(MainActivity .this, filena + " 파일을 저장해주세요!", Toast.LENGTH_SHORT).show();
		}
		else if (savenum == 1) {
			
			cManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
			mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
			wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 

			if(mobile.isConnected() || wifi.isConnected())
			{
				Intent cs = new Intent(MainActivity.this,web.class);
				cs.putExtra("code",coding.getText().toString());
				cs.putExtra("filename",fina.getText().toString());
				cs.putExtra("flocal",of.toString());
				startActivity(cs);
			}
			else
				Toast.makeText(MainActivity .this, "네트워크 연결 오류 (연결을 확인하세요)", Toast.LENGTH_SHORT).show();
		}
		}});
		
	// 뒤로가기 핸들러
	backPressCloseHandler = new BackPressCloseHandler(this);
	}

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
		
	// 이 아래부터 옵션 메뉴
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// HTML5 강좌
			case R.id.study:
				Intent std = new Intent(MainActivity.this,help.class);
				startActivity(std);
				return true;

			// Setting
			case R.id.setting:
				Intent st = new Intent(MainActivity.this,setting.class);
				startActivity(st);
				return true;
				// Toast.makeText(MainActivity .this, "준비 중입니다...", Toast.LENGTH_SHORT).show();
				
			// Version Cheak
			case R.id.version:
				
				cManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
				mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
				wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 

				if(mobile.isConnected() || wifi.isConnected())
				{
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
					StrictMode.setThreadPolicy(policy);

					String NewVer = null;
					StringBuffer sb = new StringBuffer();
					try {
						URL url = new URL("http://secretloca.dothome.co.kr/VerCheak.txt");
						BufferedReader reader = new BufferedReader(
							new InputStreamReader(url.openStream())); 

						String str = null;
						while((str = reader.readLine()) != null){
							sb.append(str);
						}
						NewVer = sb.toString();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String Ver="Beta Ver 0.55";

					AlertDialog.Builder builder = new
						AlertDialog.Builder(MainActivity.this);
					builder.setTitle("App 정보");
					builder.setMessage("\nApp 이름 : Palm 코딩\n최신 Version : " + NewVer + "\n현재 Version : " + Ver);

					if (NewVer.equals(Ver)) {

						builder.setPositiveButton("최신 Version 입니다 (닫기)", new
							DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}});
					}
					else {
						builder.setNegativeButton("최신 Version 다운", new
							DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

									Intent Down = new Intent
									(Intent.ACTION_VIEW, Uri.parse
									 ("http://naver.me/xoxxEZFd"));
									startActivity(Down);
								}});
						builder.setPositiveButton("닫기", new
							DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}});
					}

					builder.show();
				}
				else
					Toast.makeText(MainActivity .this, "네트워크 연결 오류 (연결을 확인하세요)", Toast.LENGTH_SHORT).show();
				
				return true;
			
			// 문의하기
			case R.id.email:
				Toast.makeText(MainActivity .this, "무슨 문제점이 있으신가요?", Toast.LENGTH_SHORT).show();
				Intent mail = new Intent(Intent.ACTION_SEND); 
				mail.setType("plain/text"); 
				mail.putExtra(Intent.EXTRA_EMAIL, new String[] {"palmcoding@gmail.com"}); 
				mail.putExtra(Intent.EXTRA_SUBJECT, "Palm 코딩 문의"); 
				startActivity(mail);
				return true;

		}
		
		return false;
	}
	
	// 파일 저장, 읽기, 삭제
	private void onTextWriting(String title,String body){
		File file;
		file = new File(of);
		if(!file.exists()){
			file.mkdirs();
		}
		file = new File(of+File.separator+title);
		try{
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter buw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			buw.write(body);
			buw.close();
			fos.close();
			Toast.makeText(MainActivity .this, "파일 : " + filena + " 저장 완료", Toast.LENGTH_SHORT).show();
		}catch(IOException e){

		}
    }
	
    private void onTextRead(){
		
		if (dir_int == 0) {
			Back = "닫기";
		}
		else if (dir_int == 1) {
			Back = "뒤로";
		}
		
		final ArrayList<File> filelist = new ArrayList<File>();
		final File files = new File(of);
		
		if(!files.exists()){
			files.mkdirs();
		}
		if(files.listFiles().length>0){
			for(File file : files.listFiles(new TextFileFilter())){
				filelist.add(file);    
			}
		}
		final CharSequence[] filename = new CharSequence[filelist.size()];
		for(int i = 0 ; i < filelist.size() ; i++){
			filename[i] = filelist.get(i).getName();
		}
		
		new AlertDialog.Builder(this)
			.setTitle("불러올 수 있는 파일 목록")
			.setItems(filename, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					
					File selecttext = filelist.get(arg1);
					
					if (selecttext.isDirectory()) {
						of = "/"+filelist.get(arg1);
						dir_int = 1;
						onTextRead(); // 선택한 폴더 들어가기
					}
					else {
					
					try{
						String body;
						StringBuffer bodytext = new StringBuffer();
							
						FileInputStream fis = new FileInputStream(selecttext);
						BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
						while((body = bufferReader.readLine())!=null){
							bodytext.append(body+"\n"); // \n 있어야 출력할 때 줄 띄운 것도 정상적으로 출력 그렇지 않으면 한 줄로 출력
						}
						filena = selecttext.getName();
						coding.setText(bodytext.toString());
						Toast.makeText(MainActivity .this, "파일 : " + filena + " 불러오기 완료", Toast.LENGTH_SHORT).show();
						sa = 0;
						cksa.setText("*");
						coding.setEnabled(true);
					}catch(IOException e){
						Toast.makeText(MainActivity .this, "파일 : " + filena + " 불러오기 실패", Toast.LENGTH_SHORT).show();
					}
				}}
		    }
			)
			
			.setPositiveButton(Back, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
						if (dir_int == 0) {
							// 비워두기
						}
						else if (dir_int == 1) {
					        of = files.getParent(); // 폴더 뒤로가기
							if (of.equals(dir))
								dir_int = 0;
							else
								dir_int = 1;
						onTextRead();
					}
				}
			})
			
			.setNegativeButton("파일 삭제", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					deltext();
				}
			}).show();
    }
	private void deltext(){
		final ArrayList<File> filelist = new ArrayList<File>();
		File files = new File(of);
		if(!files.exists()){
			files.mkdirs();
		}
		if(files.listFiles().length>0){
			for(File file : files.listFiles(new TextFileFilter())){
				filelist.add(file);    
			}
		}
		CharSequence[] filename = new CharSequence[filelist.size()];
		for(int i = 0 ; i < filelist.size() ; i++){
			filename[i] = filelist.get(i).getName();
		}
		new AlertDialog.Builder(this)
			.setTitle("삭제할 수 있는 파일 목록")
			.setItems(filename, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					filelist.get(arg1).delete();
					deltext(); // 다이얼로그 한 번 더 호출
					Toast.makeText(MainActivity .this, "파일 : " + filena + " 삭제 완료", Toast.LENGTH_SHORT).show();
					coding.setText("");
					cksa.setText("");
					fina.setText("");
					sa=2;
					coding.setEnabled(false);
				}
			}).setNegativeButton("뒤로", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					onTextRead();
				}
			}).show();
    }
	class TextFileFilter implements FileFilter{
		public boolean accept(File file) {
			// TODO Auto-generated method stub
			if(file.getName().endsWith(".html")||file.getName().endsWith(".css")||file.getName().endsWith(".js")||file.isDirectory())
				return true;
			return false;
		}
		
	}}
