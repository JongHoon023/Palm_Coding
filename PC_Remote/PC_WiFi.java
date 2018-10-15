import java.awt.AWTException;
import java.awt.Container;
import java.awt.Font;
import java.awt.List;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PC_WiFi {
	
	static ServerSocket ss;
	static Socket so;
	static Robot robot;
	static boolean bt_stat = false;
	
	static Thread t;

	public static void main(String[] args) {
		
		JFrame jf = new JFrame("PC 원격 제어 프로그램");
		Container c = jf.getContentPane();
		jf.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		jf.setSize(500, 300);
		
		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		
		p.add(new JLabel("인증 키 : "));
		JTextField port = new JTextField(4);
		port.setAlignmentX(JTextField.CENTER);
		p.add(port);
		
		Font font = new Font("굴림", Font.BOLD, 30);
		
		port.setFont(font);
		
		JLabel ip = new JLabel();
		ip.setFont(font);
		
		try {
			ip.setText(Inet4Address.getLocalHost().getHostAddress()); // 컴퓨터의 IP 를 가져와 띄워줌
		} 
		catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		p2.add(ip);
		
		JButton bt = new JButton("연결 대기");
		
		p3.add(bt);
		
		List log = new List();
		p4.add(log);
		
		// 연결 대기 버튼 이벤트
		bt.addActionListener((e) -> {
			
			if (bt_stat == false) {
				bt.setEnabled(false); // 연결 중일 때 버튼 클릭 막기
				
				t = new Thread(() -> {
					try {
						ss = new ServerSocket(Integer.parseInt(port.getText().toString())); // 서버 소켓을 입력받은 포트로 열어줌
						
						log.add("연결 대기중...");
						
						while (true) {
							so = ss.accept(); // 핸드폰과 연결될 때까지 대기 상태
						
							BufferedReader r = new BufferedReader(new InputStreamReader(so.getInputStream()));
							String command = r.readLine(); // 핸드폰에서 명령을 보내면 command 에 받아서 대입
							
							// Robot 클래스를 이용해 원격 조종 가능하게 해줌
							try {
								robot = new Robot();
							} catch (AWTException err) {
								log.add("원격 제어 불가능");
							}
							
							if (command.equals("OK")) { // 연결이 됐으면
								log.add("연결 되었습니다");
								bt.setEnabled(true); // 버튼 제어 가능하게 만듦
								bt.setText("연결 강제 종료");
							}
							else if (command.equals("END")) { // 핸드폰과의 연결이 끊어졌으면
								so.close(); // 소켓을 닫음
								r.close(); // BufferBufferedReader 클래스 닫음

								System.exit(0); // 강제 종료 시킴
								break;
							}
							// 명령에 따른 이벤트 발생
							else if (command.equals("ESC")) {
								robot.keyPress(KeyEvent.VK_ESCAPE);
								robot.keyRelease(KeyEvent.VK_ESCAPE);
							}
							else if (command.equals("F5")) {
								robot.keyPress(KeyEvent.VK_F5);
								robot.keyRelease(KeyEvent.VK_F5);
							}
							
							else if (command.equals("Left")) {
								robot.keyPress(KeyEvent.VK_LEFT);
								robot.keyRelease(KeyEvent.VK_LEFT);
							}
							else if (command.equals("Right")) {
								robot.keyPress(KeyEvent.VK_RIGHT);
								robot.keyRelease(KeyEvent.VK_RIGHT);
							}
							else if (command.equals("Shift + F5")) {
								robot.keyPress(KeyEvent.VK_SHIFT);
								robot.keyPress(KeyEvent.VK_F5);
								robot.keyRelease(KeyEvent.VK_SHIFT);
								robot.keyRelease(KeyEvent.VK_F5);
							}
							else if (command.equals("Up")) {
								robot.keyPress(KeyEvent.VK_UP);
								robot.keyRelease(KeyEvent.VK_UP);
							}
							else if (command.equals("Down")) {
								robot.keyPress(KeyEvent.VK_DOWN);
								robot.keyRelease(KeyEvent.VK_DOWN);
							}
							log.add("명령어 : " + command); // 명령어가 정상적으로 실행됐으면 로그를 추가해줌
						}
						bt.setText("연결 강제 종료");
					}
					catch (Exception err) {
						log.add(err.getMessage());
					}
				});
				t.start();
			}
			else { // 연결 강제 종료
				try {
					ss.close();
					so.close();
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		jf.add(p);
		jf.add(p2);
		jf.add(p3);
		jf.add(p4);
		
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 윈도우 종료 시
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ss.close();
					so.close();
				} 
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally {
					System.exit(0);
				}
			}
			
		});
	}
}
