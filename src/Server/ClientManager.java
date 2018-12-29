package Server;

import MyDataBase.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import Client.Doc;
import Client.User;

public class ClientManager extends ServerSocket {
	private static final int SERVER_PORT = 3000;
	private static String fileServecePath = "d:\\Multithreading\\Files\\";

	public ClientManager() throws IOException {
		super(SERVER_PORT);
		try {
			while (true) {
				Socket socket = accept();
				new CreateServerThread(socket);
			}
		} catch (IOException ioException) {
			System.out.println("�ȴ�����ʱ����IO�쳣");
		} finally {
			close();
		}
	}

	// �߳���
	class CreateServerThread extends Thread {
		private Socket client;
		private BufferedReader bufferedReader;
		private PrintWriter printWriter;

		public CreateServerThread(Socket socket) {
			client = socket;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException ioException) {
				System.out.println("�������ȡ�쳣");
				try {
					client.close();
				} catch (IOException e) {
					System.out.println("Socket�ر��쳣");
				}
				return;
			}
			try {
				printWriter = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException ioException) {
				System.out.println("ִ�е�½ָ��ʱ,�������ȡ�쳣");
				return;
			}
			System.out.println("�����ӽ���");
			start();
		}

		public void run() {
			// ��ȡ�ӿͻ��˶�����ַ���
			String command;
			try {
				command = bufferedReader.readLine();
			} catch (IOException ioException) {
				System.out.println("ָ���ȡ�쳣");
				return;
			}
			try {
				if (command.equals("Login")) {
					String name = bufferedReader.readLine();
					String password = bufferedReader.readLine();
					String role = null;
					ResultSet resultSet;
					resultSet = DataBaseProcessing.searchUser(name);
					if (resultSet.next()) {
						role = resultSet.getString(3).trim();
						if (password.equals(resultSet.getString(2).trim())) {
							printWriter.println(role);
							printWriter.flush();
							if (role.equals("Administrator")) {
								transmitUserInfo(client);
								printWriter.println("Finished");
								printWriter.flush();
							}
							transmitDocInfo(client);
							System.out.println(role + "�û�:" + name + "��½ϵͳ");
						} else {
							System.out.println(role + "�û�:" + name + "�������,��½ʧ��");
						}
					} else {
						System.out.println("δ֪�û�:" + name + "����ϵͳ");
					}
				} else if (command.equals("UpdateUser")) {
					String name = bufferedReader.readLine();
					String password = bufferedReader.readLine();
					String role = bufferedReader.readLine();
					DataBaseProcessing.updateUser(new User(name, password, role));
				} else if (command.equals("InsertUser")) {
					String name = bufferedReader.readLine();
					String password = bufferedReader.readLine();
					String role = bufferedReader.readLine();
					DataBaseProcessing.insertUser(new User(name, password, role));
				} else if (command.equals("DeleteUser")) {
					String name = bufferedReader.readLine();
					DataBaseProcessing.deleteUser(name);
				} else {
					try {
						if (command.equals("DownloadFile")) {
							// ����֮ǰ�ȶ��ĵ�ID
							String iD = bufferedReader.readLine();
							transmitOutDoc(client, iD);
						} else if (command.equals("UploadFile")) {
							// �����ĵ�֮ǰ�ȶ��ĵ���ϸ��Ϣ
							String[] infos = new String[5];
							for (int i = 0; i < 5; i++) {
								infos[i] = bufferedReader.readLine();
								// ��ȷ����鶼�յ�(271MB,68MB,��С���ļ�)
								System.out.println(infos[i]);
							}
							transmitInDoc(client, new Doc(infos[0], infos[1], infos[2], infos[3], infos[4]));
						} else {
							System.out.println("�ж�һ��δ֪����");
						}
					} catch (IOException ioException) {
						System.out.println("����������쳣");
					} catch (SQLException sqlException) {
						System.out.println("User_doc���ݿ�����쳣");
					}
				}
			} catch (SQLException e) {
				System.out.println("User_info���ݿ�����쳣");
			} catch (IOException e) {
				System.out.println("ָ�������Ϣ��ȡʧ��");
			}
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("Socket�ر��쳣");
			}
			System.out.println("Socket�����ѶϿ�");
		}
	}

	private void transmitOutDoc(Socket socket, String iD) throws SQLException, IOException {
		final int BytesEachTime = 10000;// һ�ζ�����������
		String filename;
		ResultSet docResult = DataBaseProcessing.searchDoc(iD);
		if (docResult.next()) {
			filename = docResult.getString(5).trim();
		} else {
			System.out.println("�ĵ�" + iD + "ƥ���쳣");
			return;
		}
		File filetoDownload = new File(
				fileServecePath + iD + "." + filename.substring(filename.lastIndexOf(".") + 1, filename.length()));
		DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filetoDownload));
		DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
		// �ȴ����ļ���С

		long fileSize = filetoDownload.length();
		// TODO Test
		System.out.println(fileSize);

		dataOutputStream.writeLong(fileSize);
		// �ٴ����ļ�
		// �����ļ�
		int length = 0;
		long curLength = 0;
		float currentProgress = 0;
		byte[] sendBytes = new byte[BytesEachTime];
		while ((length = dataInputStream.read(sendBytes, 0, sendBytes.length)) > 0) {
			dataOutputStream.write(sendBytes, 0, length);
			curLength = curLength + length;
			currentProgress = (float) ((double) curLength / (double) fileSize * 100.0);
			System.out.println("�ĵ�" + iD + "�Ѵ���" + currentProgress + "%");
		}
		dataOutputStream.flush();
		System.out.println("�ļ��������!");
		try {
			dataInputStream.close();
		} catch (IOException ioException) {
			System.out.println("�ļ����ر��쳣");
		}
		// socket ���ڹ��캯���йر�
	}

	private void transmitInDoc(Socket socket, Doc doc) throws SQLException, IOException {
		final int BytesEachTime = 10000;// һ�ζ�����������
		String filename = doc.getFilename();
		File filetoSave = new File(fileServecePath + doc.getID() + "."
				+ filename.substring(filename.lastIndexOf(".") + 1, filename.length()));
		// �����ĵ���Ϣ
		DataBaseProcessing.insertDoc(doc);
		try {
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(filetoSave));
			// �ȶ��ļ���С
			// TODO bug:�ļ���СΪ284778602B(271MB)ʱ������ʾΪ5785721462337830912,��68MBʱ����
			// �ڶ��β���(271MB)ʱ��ʾ���� �����β���(δ��¼) ���Ĵβ����쳣(��ʾΪ-7676423963002534235)
			// ����β�������(��ʱʹ��Ĭ��������Ϣ) �����β�������(��ʱʹ��Ĭ��������Ϣ)
			// ���ߴβ�������(������Ϣ:123) �ڰ˴β�������(������Ϣ:271(��2-4�β��Ե�������ͬ))
			// �ھŴβ�������(ͬ��һ������) bug û�и���,�����ļ���С(543MB)--->����
			// ����1.85GB-->���� ��������2.12GB(����int����)(ǰ�������,�����쳣)ԭ��(int�ͱ���curLength���)
			// ��һ�ε������ں�������û�и���,(�ɻ�?) ע:�������������������µ�bug��client��Main��������ʾ
			// �Դ˴��޸�Ϊģ��,�������������ļ�����Ĵ���
			long fileSize = dataInputStream.readLong();
			// �����ļ�
			int length = 0;// ��ǰһ�ζ�������� (С�ڵ���BytesEachTime,���������������һ��)
			long curLength = 0;// ���ϴ���
			float currentProgress = 0;
			byte[] sendBytes = new byte[BytesEachTime];
			while ((length = dataInputStream.read(sendBytes, 0, sendBytes.length)) > 0) {
				dataOutputStream.write(sendBytes, 0, length);
				curLength = curLength + length;
				// Fixed bug: currentProgress = (curLength * 100 / fileSize);
				// ԭ��:����һ�����׵������(���Դ�С71336566B)
				currentProgress = (float) ((double) curLength / (double) fileSize * 100.0);
				System.out.println("��ǰ���� = " + curLength);
				System.out.println("�ļ����� = " + fileSize);
				System.out.println("���� = " + currentProgress);
				System.out.println("�ĵ�" + doc.getID() + "�Ѵ���" + currentProgress + "%");
			}

			System.out.println("�ļ��������!");
			try {
				dataOutputStream.close();
			} catch (IOException ioException) {
				System.out.println("�ļ����ر��쳣");
			}
		} catch (IOException ioException) {
			System.out.println("�ĵ�" + doc.getID() + "����ʧ��!");
			// ɾ�������ĵ�
			if (filetoSave.exists()) {
				filetoSave.delete();
			}
			// ɾ�������ĵ���Ϣ
			try {
				DataBaseProcessing.deleteDocInfo(doc.getID());
			} catch (SQLException sqlException) {
				System.out.println("�����ĵ���Ϣɾ��ʧ��,�����ֶ�ɾ��,����ϵͳ�쳣");
			}
			// �׳��쳣�Ա����ļ�����ʧ��
			throw new IOException();
		}
		// socket ���ڹ��캯���йر�
	}

	private boolean transmitUserInfo(Socket socket) {
		ResultSet resultSet;
		PrintWriter printWriter;
		String name, password, role;
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e1) {
			System.out.println("��ȡ���ӵ������ʱ����");
			return false;
		}
		try {
			resultSet = DataBaseProcessing.getUserInfo();
			while (resultSet.next()) {
				name = resultSet.getString(1).trim();
				password = resultSet.getString(2).trim();
				role = resultSet.getString(3).trim();
				assert (name == null || password == null || role == null);
				printWriter.println(name);
				printWriter.println(password);
				printWriter.println(role);
				printWriter.flush();
			}
		} catch (SQLException sqlException) {
			System.out.println("���ݿ��������");
			return false;
		}
		return true;
	}

	private boolean transmitDocInfo(Socket socket) {
		ResultSet resultSet;
		PrintWriter printWriter;
		String ID, creator, timestamp, description, filename;
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e1) {
			System.out.println("��ȡ���ӵ������ʱ����");
			return false;
		}
		try {
			resultSet = DataBaseProcessing.getDocInfo();
			while (resultSet.next()) {
				ID = resultSet.getString(1).trim();
				creator = resultSet.getString(2).trim();
				timestamp = resultSet.getString(3).trim();
				description = resultSet.getString(4).trim();
				filename = resultSet.getString(5).trim();
				assert (ID == null || creator == null || timestamp == null || description == null || filename == null);
				printWriter.println(ID);
				printWriter.println(creator);
				printWriter.println(timestamp);
				printWriter.println(description);
				printWriter.println(filename);
				printWriter.flush();
			}
		} catch (SQLException sqlException) {
			System.out.println("���ݿ��������");
			return false;
		}
		return true;
	}
}
