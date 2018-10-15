package jonghoon.pc_remote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Main2Activity extends AppCompatActivity {

    int port;
    Socket so;

    String ip;
    boolean Send_Success;

    private Button esc, f5, shift_f5, up, down, left, right;
    BufferedWriter w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        esc = (Button) findViewById(R.id.esc);
        f5 = (Button) findViewById(R.id.f5);
        shift_f5 = (Button) findViewById(R.id.shift_f5);
        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);

        Intent i = getIntent();
        port = i.getExtras().getInt("port_num");
        ip = i.getExtras().getString("ip_num");


        // 연결이 되었으면 연결이 되었음을 PC 로 전송함
        Send_Message("OK");

        esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Message("ESC");
            }
        });

        f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Message("F5");
            }
        });

        shift_f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Message("Shift + F5");
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Message("Up");
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Message("Down");
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Message("Left");
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Message("Right");
            }
        });
    }

    @Override
    // 액티비티가 종료되었을 때 발생 (앱 종료 시 실행)
    protected void onDestroy() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    so = new Socket(ip, port); // 소켓 연결
                    w = new BufferedWriter(new OutputStreamWriter(so.getOutputStream()));
                    w.write("END\n"); // PC 로 명령 입력
                    w.flush(); // PC 로 명령을 전송

                    so.close(); // 소켓을 닫음
                    w.close(); // BufferedWriter 클래스를 닫음
                }
                catch (IOException e) {
                    finish();
                }
            }
        });
        t.start();

        Toast.makeText(getApplicationContext(), "PC 와 연결을 종료합니다.", Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }
    // PC 로 명령을 전송하는 메소드
    private void Send_Message(final String msg) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    so = new Socket(ip, port); // 소켓 연결
                    w = new BufferedWriter(new OutputStreamWriter(so.getOutputStream()));
                    w.write(msg + "\n"); // PC 로 명령 입력
                    w.flush(); // PC 로 명령을 전송

                    Send_Success = true;
                }
                catch (IOException e) {
                    Send_Success = false;
                    e.printStackTrace();
                }

                // 쓰레드에서 UI 를 처리하기 위해 사용
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // PC 와 연결에 성공했으면
                        if (msg.equals("OK") && Send_Success == true)
                            Toast.makeText(getApplicationContext(), "PC 와 연결되었습니다", Toast.LENGTH_SHORT).show();
                        else if (msg.equals("OK") && Send_Success == false)
                            Toast.makeText(getApplicationContext(), "PC 와 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        t.start();
    }
}