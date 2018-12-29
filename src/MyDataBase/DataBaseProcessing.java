package MyDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Client.Doc;
import Client.User;

public class DataBaseProcessing {
	static Connection dataConnection;
	static Statement sqlTools;

	public static void Init() throws ClassNotFoundException, SQLException {
		// ��ʼ��SQL
//		String sqlCommand;
		String url = "jdbc:mysql://localhost:3306/document?" + "user=root&password=" + "123456"
				+ "&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT";
		Class.forName("com.mysql.cj.jdbc.Driver");// ��̬����mysql����
		System.out.println("������������");
		// Class.forName("com.mysql.jdbc.Driver");
		// ������ʾ:���ݿ�����com.mysql.jdbc.Driver�Ѿ���������,Ӧ��ʹ���µ�����com.mysql.cj.jdbc.Driver
		dataConnection = DriverManager.getConnection(url);
		System.out.println("���ݿ���������");
		sqlTools = dataConnection.createStatement();
		System.out.println("���ݿ⹤����������");
//      // ���ذ�
//		// ��ʼ��users //�����������Ϣ�����и���
//		{
//			DataProcessing.users = new Hashtable<String, User>();
//			String name, password, role;
//			sqlCommand = "select * from user_info";
//			ResultSet dataBaseUserInfo = sqlTools.executeQuery(sqlCommand);
//			while (dataBaseUserInfo.next()) {
//				name = dataBaseUserInfo.getString(1).trim();
//				password = dataBaseUserInfo.getString(2).trim();
//				role = dataBaseUserInfo.getString(3).trim();
//				if (name == null || password == null || role == null) {
//					throw new DataException("���ݴ���");
//				}
//				if (role.equalsIgnoreCase("Operator") || role.equalsIgnoreCase("Browser")
//						|| role.equalsIgnoreCase("Administrator"))
//					DataProcessing.users.put(name, new User(name, password, role));
//				else {
//					throw new DataException("���ݴ���");
//				}
//			}
//			dataBaseUserInfo.close();
//		}
//
//		// ��ʼ��docs //�����������Ϣ�����и���
//		{
//			DataProcessing.docs = new Hashtable<String, Doc>();
//			String ID, creator, timestamp, description, filename;
//			sqlCommand = "select * from doc_info";
//			ResultSet dataBaseDocInfo = sqlTools.executeQuery(sqlCommand);
//			while (dataBaseDocInfo.next()) {
//				ID = dataBaseDocInfo.getString(1).trim();
//				creator = dataBaseDocInfo.getString(2).trim();
//				timestamp = dataBaseDocInfo.getString(3).trim();
//				description = dataBaseDocInfo.getString(4).trim();
//				filename = dataBaseDocInfo.getString(5).trim();
//
//				if (ID == null || creator == null || timestamp == null || description == null || filename == null) {
//					throw new DataException("���ݴ���");
//				} else {
//					DataProcessing.docs.put(ID, new Doc(ID, creator, timestamp, description, filename));
//				}
//			}
//			dataBaseDocInfo.close();
//		}

		// ��һ������:�����쳣ʱ,�����Ƿ������Ͽ�?
		// ��ÿ���˳�ϵͳʱ,ִ���Լ���д��systemQuit����,�ڸ÷����йر�
	}

	// �û���Ϣ����

	// ���������û���Ϣ
	public static ResultSet getUserInfo() throws SQLException {
		String sqlCommand = "select * from user_info";
		return sqlTools.executeQuery(sqlCommand);
	}

	// �����û�
	public static ResultSet searchUser(String userName) throws SQLException {
		String sqlCommand = "select * from user_info where username = '" + userName + "'";
		return sqlTools.executeQuery(sqlCommand);
	}

	// �����û�
	public static boolean updateUser(User user) throws SQLException {
		// updateUserFile();
		String sqlCommand = "update user_info set password = " + user.getPassword() + ", role = '" + user.getRole()
				+ "' where username = '" + user.getName() + "'";
		if (sqlTools.executeUpdate(sqlCommand) != -1) {
			return true;
		} else {
			return false;
		}
	}

	// ����û�
	public static boolean insertUser(User user) throws SQLException {
		// updateUserFile();
		String sqlCommand = "insert into user_info values('" + user.getName() + "','" + user.getPassword() + "','"
				+ user.getRole() + "')";
		// executeUpdate���᷵��һ����Ӱ����������������-1��û�гɹ�
		if (sqlTools.executeUpdate(sqlCommand) != -1) {
			return true;
		} else {
			return false;
		}
	}

	// ɾ���û�
	public static boolean deleteUser(String name) throws SQLException {

		// updateUserFile();
		String sqlCommand = "delete from user_info where username='" + name + "'";
		if (sqlTools.executeUpdate(sqlCommand) != -1) {
			return true;
		} else {
			return false;
		}
	}

	// ������Ϣ����

	// ��ȡ���ݿ��еĵ�����Ϣ
	public static ResultSet getDocInfo() throws SQLException {
		String sqlCommand = "select * from doc_info";
		return sqlTools.executeQuery(sqlCommand);
	}

	// ���ҵ�����Ϣ
	public static ResultSet searchDoc(String iD) throws SQLException {
		String sqlCommand = "select * from doc_info where iD = " + iD;
		return sqlTools.executeQuery(sqlCommand);
	}

	// �����µĵ����ļ���Ϣ
	public static boolean insertDoc(Doc doc) throws SQLException {
		// updateDocFile();
		String sqlCommand = "insert into doc_info values('" + doc.getID() + "','" + doc.getCreator() + "','"
				+ doc.getTimestamp() + "','" + doc.getDescription() + "','" + doc.getFilename() + "')";
		if (sqlTools.executeUpdate(sqlCommand) != -1) {
			return true;
		} else {
			return false;
		}

	}

	// ɾ���ĵ�
	public static boolean deleteDocInfo(String Id) throws SQLException {
		String sqlCommand = "delete from doc_info where Id = " + Id;
		if (sqlTools.executeUpdate(sqlCommand) != -1) {
			return true;
		} else {
			return false;
		}
	}

	// �ر����Ӻ�����
	public static void dataBaseQuit() throws SQLException {
		sqlTools.close();
		dataConnection.close();
	}
}
