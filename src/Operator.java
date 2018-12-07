import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Scanner;

@SuppressWarnings("resource")

public class Operator extends User {
	// ʵ��һ���ڴ˴���д����
	Operator(String name, String password, String role) {
		super(name, password, role);
	}

	public void uploadFile() {
		long timestamp;
		String fileSorcTrace;
		Scanner in = new Scanner(System.in);
		String fileSever = "d:\\Multithreading\\Files\\";
		String ID, creator, description, filename;

		System.out.print("*********************");
		System.out.print("�ϴ��ļ�");
		System.out.println("*********************");

		// ��ȷ�õ��ļ�·��
		System.out.println("�������ļ��洢·��:");
		fileSorcTrace = in.nextLine();
		File saveFile = new File(fileSorcTrace);
		while (saveFile.exists() == false) {
			System.out.println("�ļ�·������!");
			System.out.println("�����������ļ��洢·��(����س����˳�����):");
			fileSorcTrace = in.nextLine();
			if (fileSorcTrace.length() == 0)
				return;
			saveFile = new File(fileSorcTrace);
		}

		try {
			creator = getName();
			timestamp = new Date().getTime();
			ID = (new Integer(DataProcessing.getDocNumber())).toString();
			filename = fileSorcTrace.substring(fileSorcTrace.lastIndexOf('\\') + 1);
			System.out.print("������Ը��ļ�������(����س�����):");
			if ((description = in.nextLine()).length() == 0)
				description = "(�ϴ���δд��д����)";
			FileInputStream fileInput = new FileInputStream(saveFile);
			FileOutputStream fileOutput = new FileOutputStream(
					fileSever + ID + filename.substring(filename.lastIndexOf('.')));
			FileChannel readFileChannel = fileInput.getChannel();
			FileChannel writeFileChannel = fileOutput.getChannel();
			writeFileChannel.transferFrom(readFileChannel, 0, readFileChannel.size());
			DataProcessing.insertDoc(ID, creator, timestamp, description, filename);
			fileInput.close();
			fileOutput.close();

		} catch (IOException e) {
			System.out.println("�ϴ�ʧ��");
			return;
		}
		System.out.println("�ϴ��ɹ�!");

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
				System.out.println("1.�ļ��б�.");
				System.out.print("                     ");
				System.out.println("2.�ϴ��ļ�.");
				System.out.print("                     ");
				System.out.println("3.�����ļ�.");
				System.out.print("                     ");
				System.out.println("4.�޸�����.");
				System.out.print("                     ");
				System.out.println("5.�˳�.");
				String answer;
				answer = in.nextLine();
				if (answer.matches("1|2|3|4|5")) {
					selection = answer.charAt(0);
					break;
				}
				System.out.print("*********************");
				System.out.print("�������,��������ȷ��ѡ��!");
				System.out.println("*********************");
			}
			switch (selection) {
			case '1':
				showFileList();
				break;
			case '2':
				uploadFile();
				break;
			case '3':
				downloadFile();
				break;
			case '4':
				changePassword();
				break;
			default:
				exitSystem();
				break;
			}
		}
	}
}
