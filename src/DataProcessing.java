import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//˵��:ʵ���ʹ�õ� DataProcessing��
public class DataProcessing {
	static Hashtable<String, User> users;
	static Hashtable<String, Doc> docs;

	// ˵��: ʵ����������� ʹ�ô˴�Init()����
	public static void Init() throws IOException, DataException {
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

			if (role.equals("Operator"))
				users.put(name, new Operator(name, password, role));
			else if (role.equals("Browser"))
				users.put(name, new Browser(name, password, role));
			else if (role.equals("Administrator"))
				users.put(name, new Administrator(name, password, role));
			else {
				br.close();
				throw new DataException("���ݴ���");
			}
		}
		br.close();
		// ʵ�������˴���д���룬���ļ�file.txt����ȡ�Ѿ��ϴ����ĵ�
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
	public static boolean updateUser(String name, String password, String role) throws IOException {
		User user;

		if (users.containsKey(name)) {
			// ��Administrator��Operator��Browser������ɺ󣬴˴����벻�ٱ���
			if (role.equalsIgnoreCase("administrator"))
				user = new Administrator(name, password, "Administrator");
			else if (role.equalsIgnoreCase("operator"))
				user = new Operator(name, password, "Operator");
			else
				user = new Browser(name, password, "Browser");
			users.put(name, user);
			updateUserFile();
			return true;
		} else
			return false;
	}

	// �������û�
	public static boolean insertUser(String name, String password, String role) throws IOException {
		User user;

		if (users.containsKey(name))
			return false;
		else {
			// ��Administrator��Operator��Browser������ɺ󣬴˴����벻�ٱ���
			if (role.equalsIgnoreCase("administrator"))
				user = new Administrator(name, password, "administrator");
			else if (role.equalsIgnoreCase("operator"))
				user = new Operator(name, password, "operator");
			else
				user = new Browser(name, password, "Browser");
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
	public static boolean searchByName(String name) {
		if (users.containsKey(name))
			return true;
		return false;
	}

	// ʵ������ʵ�ִ˹���
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

	// ʵ�����������û��������еĺ���ʵ�ַ����������Щ�����Ĺ���
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
	public static boolean insertDoc(String ID, String creator, long timestamp, String description, String filename)
			throws IOException {
		if (docs.containsKey(ID)) {
			return false;
		} else {
			docs.put(ID, new Doc(ID, creator, timestamp, description, filename));
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

	// �����ļ���Ŀ
	public static int getDocNumber() {
		return docs.size();
	}
}
