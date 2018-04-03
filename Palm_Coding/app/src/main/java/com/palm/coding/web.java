package com.palm.coding;

import android.app.*;
import android.content.*;
import android.media.*;
import android.net.*;
import android.net.http.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import android.service.quicksettings.*;

public class web extends Activity
{
	TextView tv;
	private WebView wv;
	String Title;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		
		Intent intent = this.getIntent();
		String code = intent.getStringExtra("code");
		String file = intent.getStringExtra("filename");
		String flocal = intent.getStringExtra("flocal");
		
		// setTitle("My new title");
		
		tv = (TextView)findViewById(R.id.textview);
		tv.setText(file + " 파일을 불러왔습니다\n(파일 경로 : " + flocal + "/" + file +")");
		
		wv = (WebView) this.findViewById(R.id.webview);
		wv.getSettings().setLoadsImagesAutomatically(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setPluginState(WebSettings.PluginState.ON); 
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setSupportMultipleWindows(true);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wv.getSettings().setLightTouchEnabled(true); 
		wv.getSettings().setSavePassword(true); 
		wv.getSettings().setSaveFormData(true); 
		wv.setWebChromeClient(new WebChromeClient());
		wv.setWebViewClient(new WebViewClient());
		wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		// wv.loadData(code,"text/html; charset=UTF-8",null);
		wv.loadUrl("file:///" + flocal + "/" + file);
		// wv.loadUrl("http://secretloca.dothome.co.kr/php.html"); // 서버 사용시
		// wv.loadUrl("http://localhost:8099/"); // localhost
		
		wv.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
					setTitle(view.getTitle());
				}
			});
		
        wv.setWebChromeClient(new WebChromeClient() {

				public void stopMediaPlayer() {
					MediaPlayer player = new MediaPlayer();

					if (player.isPlaying()) {
						player.stop();
					}
					}
			});}
			
	public class WebClient extends WebViewClient {
		@Override
		public void onReceivedSslError(WebView view,
			SslErrorHandler handler, SslError error){
			handler.proceed();
		}
	}
			
	private class MyWebViewClient extends WebViewClient { 
	
		@Override
        public void onPageFinished(WebView view, String url) {
            if(url.endsWith(".mp4")) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url);
                i.setDataAndType(uri, "video/ogv");
                startActivity(i);
            }
            super.onPageFinished(view, url);            
        }
		}
	
		// 이 아래부터 옵션 메뉴
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.wmenu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.browser:

				Intent intent = this.getIntent();
				String code = intent.getStringExtra("code");
				String file = intent.getStringExtra("filename");
				String flocal = intent.getStringExtra("flocal");
				
				Intent i = new Intent(Intent.ACTION_DEFAULT);
				Uri u = Uri.parse("file://" + flocal + "/" + file);
				i.setData(u);
				startActivity(i);

				return true;

		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()){
			wv.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		wv.destroy();
		wv = null;
	}
}
