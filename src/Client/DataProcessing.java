package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.*;

//˵��:ʵ���ʹ�õ� DataProcessing��
public class DataProcessing {
	final static String cookiePath = "./cookie.dll";
	public static Hashtable<String, User> users;
	public static Hashtable<String, Doc> docs;
	static String[] cookies;

	// ���س�ʼ��
	public static void initLocalCookies() throws IOException, DataException {
		cookies = null;
		String savedName, savedPassword, savedState;
		File cookieFile = new File(cookiePath); // cookie.dll�к����û���,����,��½��ʽ
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
					cookies = new String[4];
					cookies[0] = savedName;
					cookies[1] = savedPassword;
					cookies[3] = savedState;
				}
			}
			cookieReader.close();
		}
	}

	// ������ʼ��UserInfo
	public static void initUserInfo(BufferedReader bufferedReader) throws IOException, DataException {
		users = new Hashtable<String, User>();
		String name, password, role;
		while (!(name = bufferedReader.readLine()).equals("Finished")) {
			password = bufferedReader.readLine();
			role = bufferedReader.readLine();
			if (password == null || role == null) {
				bufferedReader.close();
				throw new DataException("���ݴ���");
			}
			User user = new User(name, password, role);
			if (role.equals("Operator") || role.equals("Browser") || role.equals("Administrator"))
				users.put(name, user);
			else {
				bufferedReader.close();
				throw new DataException("���ݴ���");
			}
		}
	}

	// ������ʼ��DocInfo
	public static void initDocInfo(BufferedReader bufferedReader) throws IOException, DataException {
		docs = new Hashtable<String, Doc>();
		String ID, creator, timestamp, description, filename;
		while ((ID = bufferedReader.readLine()) != null) {
			creator = bufferedReader.readLine();
			timestamp = bufferedReader.readLine();
			description = bufferedReader.readLine();
			filename = bufferedReader.readLine();
			if (creator == null || timestamp == null || description == null || filename == null) {
				bufferedReader.close();
				throw new DataException("�ļ��б��ȡ�쳣��");
			}
			docs.put(ID, new Doc(ID, creator, timestamp, description, filename));
		}
		bufferedReader.close();
	}
//	public static void Init() throws IOException, DataException, ClassNotFoundException, SQLException {
//		// ��ʼ��users 
//		users = new Hashtable<String, User>();
//		String name, password, role;
//		BufferedReader br = new BufferedReader(new FileReader("d:\\Multithreading\\user.txt"));
//		while ((name = br.readLine()) != null) {
//			password = br.readLine();
//			role = br.readLine();
//			if (password == null || role == null) {
//				br.close();
//				throw new DataException("���ݴ���");
//			}
//			User user = new User(name, password, role);
//			if (role.equals("Operator") || role.equals("Browser") || role.equals("Administrator"))
//				users.put(name, user);
//			else {
//				br.close();
//				throw new DataException("���ݴ���");
//			}
//		}
//		br.close();
//		
//		// ��ʼ��doc
//		docs = new Hashtable<String, Doc>();
//		String ID, creator, timestamp, description, filename;
//		
//		BufferedReader fileReader = new BufferedReader(new FileReader("d:\\Multithreading\\fileList.txt"));
//		while ((ID = fileReader.readLine()) != null) {
//			creator = fileReader.readLine();
//			timestamp = fileReader.readLine();
//			description = fileReader.readLine();
//			filename = fileReader.readLine();
//			
//			if (creator == null || timestamp == null || description == null || filename == null) {
//				fileReader.close();
//				throw new DataException("�ļ��б��ȡ�쳣��");
//			}
//			
//			docs.put(ID, new Doc(ID, creator, new Long(timestamp).longValue(), description, filename));
//		}
//		fileReader.close();

//		�ڱ��ؼ������ݿ��Ĳ���
//		DataBaseProcessing.Init();

	// ��ʼ��cookie
//		cookies = null;
//		String savedName, savedPassword, savedState;
//		File cookieFile = new File("D:\\Multithreading\\cookie.dll"); // cookie.dll�к����û���,����,��½��ʽ
//		if (!cookieFile.exists()) {
//			cookieFile.createNewFile();
//		} else if (cookieFile.getTotalSpace() != 0) {
//			BufferedReader cookieReader = new BufferedReader(new FileReader(cookieFile));
//			if ((savedName = cookieReader.readLine()) != null) {
//				savedPassword = cookieReader.readLine();
//				savedState = cookieReader.readLine();
//				if (savedPassword == null || savedState == null || !savedState.matches("Remember|Auto")
//						|| cookieReader.readLine() != null) {
//					cookieReader.close();
//					throw new DataException("���ݴ���!");
//				} else {
//					User user = searchByName(savedName);
//					if (user == null) {
//						cookieReader.close();
//						throw new DataException("���ݱ��۸�!");
//					} else {
//						cookies = new String[4];
//						cookies[0] = savedName;
//						cookies[1] = savedPassword;
//						cookies[2] = user.getRole();
//						cookies[3] = savedState;
//					}
//				}
//			}
//			cookieReader.close();
//		}

//	}

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

	// ������������������Ϣ
	public static void sendRequestionAndInfo(String requestion, String info) throws UnknownHostException, IOException {
		DataRequestion dataRequestion = new DataRequestion();
		PrintWriter printWriter = dataRequestion.sendRequestion(requestion);
		String[] infos = info.split(MyInfo.separator);
		int numberOfInfo;
		if ("DeleteUser".equals(requestion)) {
			numberOfInfo = 1;
		} else {
			numberOfInfo = 3;
		}
		for (int i = 0; i < numberOfInfo; i++) {
			printWriter.println(infos[i]);
		}
		printWriter.flush();
		dataRequestion.close();
	}

	// �����û���Ϣ
	public static void updateUser(User user) throws UnknownHostException, IOException {
		// �ǹ���Ա���ܿ����û���Ϣ,��ʱusers��null,�ú���ֻ��������������
		if (users != null) {
			users.put(user.getName(), user);
		}
		sendRequestionAndInfo("UpdateUser", user.toString());
//		return DataBaseProcessing.updateUser(user);
	}

	// �������û�
	public static void insertUser(User user) throws UnknownHostException, IOException {
		users.put(user.getName(), user);
		sendRequestionAndInfo("InsertUser", user.toString());
//		return DataBaseProcessing.insertUser(user);
	}

	// ɾ���û�
	public static void deleteUser(String name) throws UnknownHostException, IOException {
		users.remove(name);
		sendRequestionAndInfo("DeleteUser", name);
//		return DataBaseProcessing.deleteUser(name);
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
//	public static void updateUserFile() throws IOException {
//		BufferedWriter writer = new BufferedWriter(new FileWriter("d:\\Multithreading\\user.txt"));
//		Enumeration<User> users = DataProcessing.getAllUser();
//		while (users.hasMoreElements()) {
//			User user = users.nextElement();
//			writer.write(user.getName() + "\r\n" + user.getPassword() + "\r\n" + user.getRole() + "\r\n");
//		}
//		writer.close();
//	}

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
	public static void insertDoc(Doc doc) throws UnknownHostException, IOException {
		docs.put(doc.getID(), doc);
		// �ϴ�ʱ�Զ������ݿ���� �ڱ�����Ӽ���
//		sendRequestionAndInfo("InsertDoc", doc.toString());
//		return DataBaseProcessing.insertDoc(doc);
	}

	// �����������ļ�����Ϣд�뵽�ļ�fileList.txt��
//	public static void updateDocFile() throws IOException {
//		BufferedWriter writer = new BufferedWriter(new FileWriter("d:\\Multithreading\\fileList.txt"));
//		Enumeration<Doc> docs = DataProcessing.getAllDocs();
//		while (docs.hasMoreElements()) {
//			Doc doc = docs.nextElement();
//			writer.write(doc.getID() + "\r\n" + doc.getCreator() + "\r\n" + doc.getTimestamp() + "\r\n"
//					+ doc.getDescription() + "\r\n" + doc.getFilename() + "\r\n");
//		}
//		writer.close();
//	}

	// ����cookie��Ϣ
	public static void updateCookies() throws IOException {
		File cookieFile = new File(cookiePath);
		if (cookies == null)
			cookieFile.delete();
		else {
			BufferedWriter writer = new BufferedWriter(new FileWriter(cookieFile));
			writer.write(cookies[0] + "\r\n" + cookies[1] + "\r\n" + cookies[3] + "\r\n");
			writer.close();
		}
	}

	public static int getUserNumber() {
		return users.size();
	}

	// �����ļ���Ŀ
	public static int getDocNumber() {
		return docs.size();
	}

	public static void systemQuit() {
//		try {
//			DataBaseProcessing.dataBaseQuit();
//		} catch (SQLException e) {
//			JOptionPane.showMessageDialog(new JFrame(), e.getMessage() + "���ݿ����ӹر��쳣��", "��ʾ",
//					JOptionPane.INFORMATION_MESSAGE);
//		} finally {
		System.exit(0);
//		}
	}

}
