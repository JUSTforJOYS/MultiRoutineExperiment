import java.io.IOException;
import java.util.*;

@SuppressWarnings("resource")

public class Administrator extends User {
	Administrator(String name, String password, String role) {
		super(name, password, role);
	}

	public void changeUserInfo() {
		System.out.print("*********************");
		System.out.print("�����û���Ϣ");
		System.out.println("*********************");
		Scanner in = new Scanner(System.in);
		User user;

		// ��ȷ�ҵ��û�
		System.out.print("����������ĵ��û����û���:");
		String userName = in.nextLine();
		while (userName.length() == 0)
			userName = in.nextLine();

		while ((user = DataProcessing.searchByName(userName)) == null) {
			System.out.println("�����ڸ��û�");
			System.out.print("��������������ĵ��û����û���:(����س����˳�����):");
			userName = in.nextLine();
			if (userName.length() == 0)
				return;
		}

		// ��ȷ�޸�����
		System.out.print("������������:");
		String newPassword = in.nextLine();
		while (newPassword.length() == 0)
			newPassword = in.nextLine();

		System.out.print("��������һ��������ȷ��:");
		String newPasswordCopy = in.nextLine();
		while (newPasswordCopy.length() == 0)
			newPasswordCopy = in.nextLine();

		while (newPassword.equals(newPasswordCopy) == false) {
			System.out.print("������������벻һ��!");
			System.out.print("�����������û�������(����س��˳�����):");
			newPassword = in.nextLine();
			if (newPassword.length() == 0)
				return;
			System.out.print("��������һ��������ȷ��:");
			newPasswordCopy = in.nextLine();
			while (newPasswordCopy.length() == 0)
				newPasswordCopy = in.nextLine();
		}

		try {
			DataProcessing.updateUser(userName, newPassword, user.getRole());
		} catch (IOException e) {
			System.out.println("����ʧ��!");
		}

	}

	public void delUser() {
		System.out.print("*********************");
		System.out.print("�h���û�");
		System.out.println("*********************");
		Scanner in = new Scanner(System.in);

		// ��ȷ�ҵ��û�
		System.out.print("��������h�����û����û���:");
		String userName = in.nextLine();
		while (userName.length() == 0)
			userName = in.nextLine();

		while (DataProcessing.searchByName(userName) == null) {
			System.out.println("�����ڸ��û�");
			System.out.print("�����������ɾ�����û����û���(����س����˳�����):");
			userName = in.nextLine();
			if (userName.length() == 0)
				return;
		}

		if (userName.equals(getName())) {
			System.out.println("����Ա���܄h���Լ�");
		} else if (userName.equals("AUser")) {
			System.out.println("ϵͳ����Ա�޷��h��");
		} else {
			try {
				if (DataProcessing.deleteUser(userName))
					System.out.println("ɾ���ɹ�");
				else
					System.out.println("ɾ��ʧ��");
			} catch (IOException e) {
				System.out.println("��ϵͳ�ļ�ʧ��,�޷�ɾ��");
			}
		}
	}

	public void addUser() {
		Scanner in = new Scanner(System.in);

		System.out.print("*********************");
		System.out.print("����û�");
		System.out.println("*********************");

		// ��ȷ�õ��û���
		System.out.print("�����������û����û���:");
		String userName = in.nextLine();
		while (userName.length() == 0)
			userName = in.nextLine();

		while (DataProcessing.searchByName(userName) != null) {
			System.out.println("���û����Ѵ���");
			System.out.print("���������������û����û���(����س��˳�����):");
			userName = in.nextLine();
			if (userName.length() == 0)
				return;
		}

		// ��ȷ�õ�����
		System.out.print("�����������û�������:");
		String newPassword = in.nextLine();
		while (newPassword.length() == 0)
			newPassword = in.nextLine();

		System.out.print("��������һ��������ȷ��:");
		String newPasswordCopy = in.nextLine();
		while (newPasswordCopy.length() == 0)
			newPasswordCopy = in.nextLine();

		while (newPassword.equals(newPasswordCopy) == false) {
			System.out.print("������������벻һ��!");
			System.out.print("�����������û�������(����س��˳�����):");
			newPassword = in.nextLine();
			if (newPassword.length() == 0)
				return;
			System.out.print("��������һ��������ȷ��:");
			newPasswordCopy = in.nextLine();
			while (newPasswordCopy.length() == 0)
				newPasswordCopy = in.nextLine();
		}

		// ��ȷ�õ����
		System.out.println("�����������û������:");
		System.out.println("      1.administrator      2.operator      3.browser");
		String newRole = in.nextLine();
		while (newRole.length() == 0)
			newRole = in.nextLine();
		while (newRole.matches("1|2|3") == false) {
			System.out.println("��ݹؼ���ѡ�����");
			System.out.println("������ѡ�������û������(����س��˳�����):");
			System.out.println("      1.administrator      2.operator      3.browser");
			newRole = in.nextLine();
			if (newRole.length() == 0)
				return;
		}

		try {
			if (DataProcessing.insertUser(userName, newPassword, newRole))
				System.out.println("��ӳɹ�");
			else
				System.out.println("���ʧ��");
		} catch (IOException e) {
			System.out.println("��ϵͳ�ļ�ʧ��,�޷����");
		}

	}

	public void listUser() {
		int count = 0;
		Enumeration<User> users = DataProcessing.getAllUser();

		System.out.print("*********************");
		System.out.print("��ʾ�����û���Ϣ");
		System.out.println("*********************");

		while (users.hasMoreElements()) {
			count++;
			System.out.println("Number " + count);
			System.out.println(users.nextElement().toString());
		}
	}

	public void showMenu() {
		Scanner in = new Scanner(System.in);
		char selection = 0;
		while (true) {
			System.out.print("*********************");
			System.out.print("��ѡ��˵�");
			System.out.println("*********************");
			while (true) {
				// �ò�ѭ������û������ѡ��
				System.out.print("                     ");
				System.out.println("1.�����û���Ϣ.");
				System.out.print("                     ");
				System.out.println("2.ɾ���û�.");
				System.out.print("                     ");
				System.out.println("3.����û�.");
				System.out.print("                     ");
				System.out.println("4.��ʾ�����û���Ϣ.");
				System.out.print("                     ");
				System.out.println("5.�����ļ�.");
				System.out.print("                     ");
				System.out.println("6.�ļ��б�.");
				System.out.print("                     ");
				System.out.println("7.�޸�����.");
				System.out.print("                     ");
				System.out.println("8.�˳�.");
				String answer;
				answer = in.nextLine();
				while (answer.length() == 0)
					answer = in.nextLine();
				if (answer.matches("1|2|3|4|5|6|7|8")) {
					selection = answer.charAt(0);
					break;
				}
				System.out.print("*********************");
				System.out.print("�������,��������ȷ��ѡ��!");
				System.out.println("*********************");
			}
			switch (selection) {
			case '1':
				changeUserInfo();
				break;
			case '2':
				delUser();
				break;
			case '3':
				addUser();
				break;
			case '4':
				listUser();
				break;
			case '5':
				downloadFile();
				break;
			case '6':
				showFileList();
				break;
			case '7':
				changePassword();
				break;
			default:
				exitSystem();
				break;
			}
		}
	}
}
