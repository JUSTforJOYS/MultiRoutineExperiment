import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Scanner;

@SuppressWarnings("resource")

public abstract class User {
	private String name;
	private String password;
	private String role;

	User() {
	}

	User(String name, String password, String role) {
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public boolean changeUserInfo(String password) {
		// д�û���Ϣ���洢
		try {
			if (DataProcessing.updateUser(name, password, role)) {
				this.password = password;
				System.out.println("�޸ĳɹ�");
				return true;
			} else {
				System.out.println("�޸�ʧ��");
				return false;
			}
		} catch (IOException e) {
			System.out.println("��ϵͳ�ļ�ʧ��,�޷��޸�");
			return false;
		}
	}

	public boolean downloadFile() {
		Doc fileInfo;
		String fileID;
		String fileSever = "d:\\Multithreading\\Files\\";
		String fileSaveTrace;
		Scanner in = new Scanner(System.in);

		System.out.print("*********************");
		System.out.print("�����ļ�");
		System.out.println("*********************");

		// ��ȷ�õ��ĵ�ID
		System.out.print("�������ļ�ID:");
		fileID = in.nextLine();
		while (fileID.length() == 0)
			fileID = in.nextLine();
		
		while ((fileInfo = DataProcessing.searchDoc(fileID)) == null) {
			System.out.println("ID����,�Ҳ������ļ�!");
			System.out.print("�����������ļ�ID(����س����˳�):");
			fileID = in.nextLine();
			if (fileID.length() == 0)
				return false;
		}

		System.out.print("�������ļ��洢·��:" + "(����س���ѡ��Ĭ��·��D:\\Files)");
		if ((fileSaveTrace = in.nextLine()).length() == 0)
			fileSaveTrace = "d:\\Files\\";

		try {
			File file = new File(fileSaveTrace + fileInfo.getFilename());
			file.createNewFile();
			FileInputStream fileInput = new FileInputStream(fileSever + fileID + "." + fileInfo.getFileType());
			FileOutputStream fileOutput = new FileOutputStream(file);
			FileChannel readFileChannel = fileInput.getChannel();
			FileChannel writeFileChannel = fileOutput.getChannel();
			writeFileChannel.transferFrom(readFileChannel, 0, readFileChannel.size());
			fileInput.close();
			fileOutput.close();
			System.out.println("���سɹ�");
		} catch (FileNotFoundException e) {
			System.out.println("�ļ���ʧ��");
		} catch (IOException e) {
			System.out.println("����ʧ��");
		}
		return true;
	}

	public void showFileList() {
		Enumeration<Doc> docs = DataProcessing.getAllDocs();

		System.out.print("*********************");
		System.out.print("�ļ��б�");
		System.out.println("*********************");

		while (docs.hasMoreElements()) {
			System.out.println(docs.nextElement().toString() + "\n");
		}
	}

	public void changePassword() {
		Scanner in = new Scanner(System.in);
		System.out.print("*********************");
		System.out.print("�޸�����");
		System.out.println("*********************");

		// ��ȷ�õ�����
		System.out.print("�������µ�����:");
		String newPassword = in.nextLine();
		while (newPassword.length() == 0)
			newPassword = in.nextLine();

		System.out.print("��������һ���µ�������ȷ��:");
		String newPasswordCopy = in.nextLine();
		while (newPasswordCopy.length() == 0)
			newPasswordCopy = in.nextLine();

		while (newPassword.equals(newPasswordCopy) == false) {
			System.out.print("�����������µ�����(����س����˳�):");
			newPassword = in.nextLine();
			if (newPassword.length() == 0)
				return;
			System.out.print("��������һ���µ�������ȷ��:");
			newPasswordCopy = in.nextLine();
			while (newPasswordCopy.length() == 0)
				newPasswordCopy = in.nextLine();
		}

		setPassword(newPassword);
		System.out.println("�ɹ��޸�����");
	}

	public abstract void showMenu();

	public void exitSystem() {
		System.out.println("ϵͳ�˳�, ллʹ�� ! ");
		System.exit(0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String toString() {
		return "Name: " + name + "\tPasward: " + password + "\tRole: " + role;
	}
}