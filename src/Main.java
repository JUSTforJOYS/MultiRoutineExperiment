import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {
		JFrame initialFram = new JFrame();
		try {
			DataProcessing.Init();
		} catch (DataException dataE) {

			JOptionPane.showMessageDialog(initialFram, dataE.getMessage() + "�����ʼ��ʧ��.", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(initialFram, e.getMessage() + "���ݿ��ļ�δ�ҵ�,�����ʼ��ʧ�ܣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
			return;
		}

		FrameLogin frameLogin = new FrameLogin();
		// ��½
		frameLogin.initLoginWindow();

	}

}