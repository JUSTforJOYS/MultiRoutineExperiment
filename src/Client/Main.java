package Client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

//Fixed bug:cookie�ļ��洢ʧ��. ԭ��:�������ط����õ���cookie��·����û�������޸ĵ�ȫ����.
//���� ����һ��final�ľ�̬����,���ں��ڵ��޸�
//Fixed bug:�ļ�������������س�ʱ,�ᵼ���ļ�����ʧ
//ԭ��:�ַ����г����˻س�,���з��ַ���ʱ��������,�����ļ���Ϊ��
//���� ��ӶԻس��ļ���,ʹ�û����»س�����ʾȷ��,��ȥ�����еĻس�
//���� ���ڷָ����Ĵ���,�û���Ȼ�п������ļ�������ʹ�õ�������ָ���
//�Ľ� ��һ������ı�,��֪�û�����ʹ�ø��ַ�
//TODO bug:�������ļ�����ʧ��ʱ,�û��ò����κ���Ϣ(����Ϊ�ϴ��ɹ�)
public class Main {
	public static void main(String[] args) {

		// ���س�ʼ��
		JFrame initialFram = new JFrame();
		try {
			DataProcessing.initLocalCookies();
		} catch (DataException dataE) {

			JOptionPane.showMessageDialog(initialFram, dataE.getMessage() + "�����ʼ��ʧ��.", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			DataProcessing.systemQuit();
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(initialFram, e.getMessage() + "���ݿ��ļ�δ�ҵ�,�����ʼ��ʧ�ܣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
			return;
		}

		// ��½
		FrameLogin frameLogin = new FrameLogin();
		frameLogin.initLoginWindow();
	}

}