package Server;

import MyDataBase.*;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		try {
			DataBaseProcessing.Init();
			try {
				new ClientManager();
			} catch (IOException e) {
				System.out.println("ServerSocket(����/�ر�)�쳣");
			}
		} catch (SQLException e) {
			System.out.println("���ݿ������쳣");
		} catch (ClassNotFoundException e) {
			System.out.println("��������ʧ��");
		} finally {
			try {
				DataBaseProcessing.dataBaseQuit();
			} catch (SQLException e) {
				System.out.println("���ݿ�ر��쳣");
			}
		}

	}
}
