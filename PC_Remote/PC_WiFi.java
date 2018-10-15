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
		
		JFrame jf = new JFrame("PC ���� ���� ���α׷�");
		Container c = jf.getContentPane();
		jf.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		jf.setSize(500, 300);
		
		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		
		p.add(new JLabel("���� Ű : "));
		JTextField port = new JTextField(4);
		port.setAlignmentX(JTextField.CENTER);
		p.add(port);
		
		Font font = new Font("����", Font.BOLD, 30);
		
		port.setFont(font);
		
		JLabel ip = new JLabel();
		ip.setFont(font);
		
		try {
			ip.setText(Inet4Address.getLocalHost().getHostAddress()); // ��ǻ���� IP �� ������ �����
		} 
		catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		p2.add(ip);
		
		JButton bt = new JButton("���� ���");
		
		p3.add(bt);
		
		List log = new List();
		p4.add(log);
		
		// ���� ��� ��ư �̺�Ʈ
		bt.addActionListener((e) -> {
			
			if (bt_stat == false) {
				bt.setEnabled(false); // ���� ���� �� ��ư Ŭ�� ����
				
				t = new Thread(() -> {
					try {
						ss = new ServerSocket(Integer.parseInt(port.getText().toString())); // ���� ������ �Է¹��� ��Ʈ�� ������
						
						log.add("���� �����...");
						
						while (true) {
							so = ss.accept(); // �ڵ����� ����� ������ ��� ����
						
							BufferedReader r = new BufferedReader(new InputStreamReader(so.getInputStream()));
							String command = r.readLine(); // �ڵ������� ����� ������ command �� �޾Ƽ� ����
							
							// Robot Ŭ������ �̿��� ���� ���� �����ϰ� ����
							try {
								robot = new Robot();
							} catch (AWTException err) {
								log.add("���� ���� �Ұ���");
							}
							
							if (command.equals("OK")) { // ������ ������
								log.add("���� �Ǿ����ϴ�");
								bt.setEnabled(true); // ��ư ���� �����ϰ� ����
								bt.setText("���� ���� ����");
							}
							else if (command.equals("END")) { // �ڵ������� ������ ����������
								so.close(); // ������ ����
								r.close(); // BufferBufferedReader Ŭ���� ����

								System.exit(0); // ���� ���� ��Ŵ
								break;
							}
							// ��ɿ� ���� �̺�Ʈ �߻�
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
							log.add("��ɾ� : " + command); // ��ɾ ���������� ��������� �α׸� �߰�����
						}
						bt.setText("���� ���� ����");
					}
					catch (Exception err) {
						log.add(err.getMessage());
					}
				});
				t.start();
			}
			else { // ���� ���� ����
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
		
		// ������ ���� ��
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
