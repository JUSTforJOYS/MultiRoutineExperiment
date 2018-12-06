import java.util.Scanner;
@SuppressWarnings("resource")

public class Browser extends User {
	// ʵ��һ���ڴ˴���д����
	Browser(String name, String password, String role) {
		super(name, password, role);
	}

	public void showMenu() {
		Scanner in = new Scanner(System.in);
		char selection;
		while (true) {
			System.out.print("*********************");
			System.out.print("��ѡ��˵�");
			System.out.println("*********************");
			while (true) {
				// �ò�ѭ������û������ѡ��
				System.out.print("                     ");
				System.out.println("1.�����ļ�.");
				System.out.print("                     ");
				System.out.println("2.�ļ��б�.");
				System.out.print("                     ");
				System.out.println("3.��������.");
				System.out.print("                     ");
				System.out.println("4.�˳�.");
				String answer;
				answer = in.nextLine();
				if (answer.matches("1|2|3|4")) {
					selection = answer.charAt(0);
					break;
				}
				System.out.print("*********************");
				System.out.print("�������,��������ȷ��ѡ��!");
				System.out.println("*********************");
			}
			switch (selection) {
			case '1':
				downloadFile();
				break;
			case '2':
				System.out.print("*********************");
				System.out.print("�ļ��б�");
				System.out.println("*********************");
				showFileList();
				break;
			case '3':
				changePassword();
				break;
			default:
				exitSystem();
				break;
			}
		}
	}
}
