package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public abstract class FrameUser extends JFrame {

	private static final long serialVersionUID = 1L;

	private User user;
	@SuppressWarnings("unused")
	private FrameCheckPassword frameCheckPassword;
	private FrameDownloadFile frameDownloadFile;
	protected JButton changePasswordButton;
	protected JButton downloadFileButton;
	protected JButton logoutButton;
	protected JLabel lableName;

	public FrameUser(User user) {
		this.user = user;
		frameDownloadFile = new FrameDownloadFile(user);
		changePasswordButton = new JButton("��������");
		downloadFileButton = new JButton("�����ĵ�");
		logoutButton = new JButton("ע��");

		changePasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent eventChangwPassward) {
				// ����֤���뷽�������������
				frameCheckPassword = new FrameCheckPassword(user, new FrameChangePassword(user));
				setVisible(false);
			}
		});

		downloadFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent eventDownload) {
				frameDownloadFile.initial();
				setVisible(false);
			}
		});

		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent eventLogout) {
				setVisible(false);
				FrameLogin frameLogin = new FrameLogin();
				if (DataProcessing.cookies != null) {
					DataProcessing.cookies[3] += "|Restart";
				}
				frameLogin.initLoginWindow();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(FrameUser.this, "�Ƿ��˳�ϵͳ��", "ѯ��",
						JOptionPane.YES_NO_OPTION)) {
					DataProcessing.systemQuit();
				} else {
					FrameUser.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
	}

	public String getName() {
		return user.getName();
	}

	public String getRole() {
		return user.getRole();
	}

	public String getPassword() {
		return user.getPassword();
	}

	public abstract void init();
}
