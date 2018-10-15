package jonghoon.pc_remote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ip1, ip2, ip3, ip4;
    TextView port;
    Button go;

    String ip;

    private int port_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        port_num = (int)((Math.random()*9999)+2000);

        while (port_num == 3306 || port_num == 1521 || port_num == 8080 || port_num == 1433 || port_num > 9999 || port_num < 1500) {
            port_num = (int)((Math.random()*9999)+2000);
        }

        ip1 = (EditText)findViewById(R.id.ip1);
        ip2 = (EditText)findViewById(R.id.ip2);
        ip3 = (EditText)findViewById(R.id.ip3);
        ip4 = (EditText)findViewById(R.id.ip4);

        port = (TextView)findViewById(R.id.port);

        go = (Button)findViewById(R.id.go);

        port.setText(String.valueOf(port_num));

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ip1.getText().toString().equals("") || ip2.getText().toString().equals("") || ip3.getText().toString().equals("") || ip4.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "ip를 전부 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (Integer.parseInt(ip1.getText().toString()) > 255 || Integer.parseInt(ip2.getText().toString()) > 255 || Integer.parseInt(ip3.getText().toString()) > 255 || Integer.parseInt(ip4.getText().toString()) > 255)
                    Toast.makeText(getApplicationContext(), "ip는 255를 넘을 수 없습니다.", Toast.LENGTH_SHORT).show();
                else {
                    ip = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();

                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    i.putExtra("port_num", port_num);
                    i.putExtra("ip_num", ip);
                    startActivity(i);
                }
            }
        });
    }
}
