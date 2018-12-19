import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//˵��:ʵ���ʹ�õ� DataProcessing��
public class DataProcessing {
	static Hashtable<String, User> users;
	static Hashtable<String, Doc> docs;
	static String[] cookies;

	// ˵��: ʵ����������� ʹ�ô˴�Init()����
	public static void Init() throws IOException, DataException {
		// ��ʼ��users
		users = new Hashtable<String, User>();
		String name, password, role;
		BufferedReader br = new BufferedReader(new FileReader("d:\\Multithreading\\user.txt"));
		while ((name = br.readLine()) != null) {
			password = br.readLine();
			role = br.readLine();
			if (password == null || role == null) {
				br.close();
				throw new DataException("���ݴ���");
			}
			User user = new User(name, password, role);
			if (role.equals("Operator") || role.equals("Browser") || role.equals("Administrator"))
				users.put(name, user);
			else {
				br.close();
				throw new DataException("���ݴ���");
			}
		}
		br.close();

		// ��ʼ��cookie
		cookies = null;
		String savedName, savedPassword, savedState;
		File cookieFile = new File("D:\\Multithreading\\cookie.dll"); // cookie.dll�к����û���,����,��½��ʽ
		if (!cookieFile.exists()) {
			cookieFile.createNewFile();
		} else if (cookieFile.getTotalSpace() != 0) {
			BufferedReader cookieReader = new BufferedReader(new FileReader(cookieFile));
			if ((savedName = cookieReader.readLine()) != null) {
				savedPassword = cookieReader.readLine();
				savedState = cookieReader.readLine();
				if (savedPassword == null || savedState == null || !savedState.matches("Remember|Auto")
						|| cookieReader.readLine() != null) {
					cookieReader.close();
					throw new DataException("���ݴ���!");
				} else {
					User user = searchByName(savedName);
					if (user == null) {
						cookieReader.close();
						throw new DataException("���ݱ��۸�!");
					} else {
						cookies = new String[4];
						cookies[0] = savedName;
						cookies[1] = savedPassword;
						cookies[2] = user.getRole();
						cookies[3] = savedState;
					}
				}
			}
			cookieReader.close();
		}

		// ��ʼ��doc
		docs = new Hashtable<String, Doc>();
		String ID, creator, timestamp, description, filename;

		BufferedReader fileReader = new BufferedReader(new FileReader("d:\\Multithreading\\fileList.txt"));
		while ((ID = fileReader.readLine()) != null) {
			creator = fileReader.readLine();
			timestamp = fileReader.readLine();
			description = fileReader.readLine();
			filename = fileReader.readLine();

			if (creator == null || timestamp == null || description == null || filename == null) {
				fileReader.close();
				throw new DataException("�ļ��б��ȡ�쳣��");
			}

			docs.put(ID, new Doc(ID, creator, new Long(timestamp).longValue(), description, filename));
		}
		fileReader.close();
	}

	// �������û������ܺ���
	public static User searchUser(String name, String password) {
		if (users.containsKey(name)) {
			User temp = users.get(name);
			if ((temp.getPassword()).equals(password))
				return temp;
		}
		return null;
	}

	public static Enumeration<User> getAllUser() {
		Enumeration<User> e = users.elements();
		return e;
	}

	// �����û���Ϣ
	public static boolean updateUser(User user) throws IOException {
		String name = user.getName();
		if (users.containsKey(name)) {
			users.put(name, user);
			updateUserFile();
			return true;
		} else
			return false;
	}

	// �������û�
	public static boolean insertUser(User user) throws IOException {
		String name = user.getName();
		if (users.containsKey(name))
			return false;
		else {
			users.put(name, user);
			updateUserFile();
			return true;
		}
	}

	// ɾ���û�
	public static boolean deleteUser(String name) throws IOException {
		if (users.containsKey(name)) {
			users.remove(name);
			updateUserFile();
			return true;
		} else
			return false;
	}

	// ͨ�����ֲ����û�
	public static User searchByName(String name) {
		if (users.containsKey(name)) {
			User temp = users.get(name);
			return temp;
		} else
			return null;
	}

	// ���û���Ϣд�뵽�ļ�user.txt�У�ʵ���û���Ϣ���ñ���
	public static void updateUserFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("d:\\Multithreading\\user.txt"));
		Enumeration<User> users = DataProcessing.getAllUser();
		while (users.hasMoreElements()) {
			User user = users.nextElement();
			writer.write(user.getName() + "\r\n" + user.getPassword() + "\r\n" + user.getRole() + "\r\n");
		}
		writer.close();
	}

	// �����ǵ��������ܺ���
	// �ҵ�������ΪID�ĵ����ļ���Ϣ
	public static Doc searchDoc(String ID) {
		if (docs.containsKey(ID)) {
			Doc temp = docs.get(ID);
			return temp;
		}
		return null;
	}

	// ��ȡ���еĵ����ļ���Ϣ
	public static Enumeration<Doc> getAllDocs() {
		Enumeration<Doc> enumDoc = docs.elements();
		return enumDoc;
	}

	// �����µĵ����ļ���Ϣ
	public static boolean insertDoc(Doc doc) throws IOException {
		String ID = doc.getID();
		if (docs.containsKey(ID)) {
			return false;
		} else {
			docs.put(ID, doc);
			updateDocFile();
			return true;
		}
	}

	// �����������ļ�����Ϣд�뵽�ļ�fileList.txt��
	public static void updateDocFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("d:\\Multithreading\\fileList.txt"));
		Enumeration<Doc> docs = DataProcessing.getAllDocs();
		while (docs.hasMoreElements()) {
			Doc doc = docs.nextElement();
			writer.write(doc.getID() + "\r\n" + doc.getCreator() + "\r\n" + doc.getTimestamp() + "\r\n"
					+ doc.getDescription() + "\r\n" + doc.getFilename() + "\r\n");
		}
		writer.close();
	}

	// ����cookie��Ϣ
	public static void updateCookies() throws IOException {
		File cookieFile = new File("D:\\Multithreading\\cookie.dll");
		if (cookies == null)
			cookieFile.delete();
		else {
			BufferedWriter writer = new BufferedWriter(new FileWriter(cookieFile));
			writer.write(cookies[0] + "\r\n" + cookies[1] + "\r\n" + cookies[3] + "\r\n");
			writer.close();
		}
	}

	// �����ļ���Ŀ
	public static int getUserNumber() {
		return users.size();
	}

	public static int getDocNumber() {
		return docs.size();
	}
}
