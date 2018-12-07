import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		try {
			DataProcessing.Init();
		} catch (DataException dataE) {
			System.out.println(dataE.getMessage() + "�����ʼ��ʧ��.");
			return;
		} catch (IOException e) {
			System.out.println("���ݿ��ļ�δ�ҵ�,�����ʼ��ʧ�ܣ�");
			return;
		}
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.print("*********************");
			System.out.print("��ӭʹ���ļ�����ϵͳV1.0");
			System.out.println("*********************");
			String selection;
			while (true) {
				System.out.print("                     ");
				System.out.println("1.��½.");
				System.out.print("                     ");
				System.out.println("2.�˳�.");
				selection = in.nextLine();
				if (selection.matches("1|2"))
					break;
				System.out.print("*********************");
				System.out.print("�������,��������ȷ��ѡ��!");
				System.out.println("*********************");
			}
			if (selection.equals("1")) {
				System.out.print("�û���:");
				String name = in.nextLine();
				System.out.print("����:");
				String password = in.nextLine();
				User user = DataProcessing.searchUser(name, password);
				if (user != null) {
					System.out.println("��½�ɹ�!");
					user.showMenu();
					break;
				}
				System.out.println("�û������������!");
			} else {
				break;
			}
		}
		in.close();
	}
}