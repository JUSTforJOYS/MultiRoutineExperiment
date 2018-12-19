import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameAdministrator extends FrameUser {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private FrameCheckPassword frameCheckPassword;
	private JButton userManageButton;

	public FrameAdministrator(User user) {
		super(user);
		userManageButton = new JButton("�û�����");

		userManageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent eventManage) {
				// �ȼ�������ٽ����û�����
				frameCheckPassword = new FrameCheckPassword(user, new FrameUserManage(user));
				setVisible(false);
			}
		});
	}

	@Override
	public void init() {
		setFont(new Font("Helvetica", Font.PLAIN, 14));

		JPanel panel = new JPanel();
		GridLayout verticalLayout = new GridLayout(4, 1);
		panel.setLayout(verticalLayout);
		panel.add(userManageButton);
		panel.add(downloadFileButton);
		panel.add(changePasswordButton);
		panel.add(logoutButton);
		getContentPane().add(panel);

		setTitle("����Ա" + "---" + getName());
		setSize(360, 320);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);

	}

}
