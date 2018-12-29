package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class DataRequestion {

	private Socket socket;
	private PrintWriter printWriter;// ���ڷ�����Ϣ

	// �������������������(����)
	public DataRequestion() throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", 3000);
		printWriter = new PrintWriter(socket.getOutputStream(), true);
	}

	// ��������˷�������
	public PrintWriter sendRequestion(String requestion) throws IOException {
		printWriter.println(requestion);
		printWriter.flush();
		return printWriter;
	}

	// ��������˷��������ĵ�����(���ĵ�ID)
	public InputStream downloadRequestion(String iD) throws IOException {
		printWriter.println("DownloadFile");
		printWriter.println(iD);
		printWriter.flush();
		return socket.getInputStream();
	}

	// ��������˷����ϴ��ĵ�����(���ĵ���ϸ��Ϣ)
	public OutputStream uploadRequestion(Doc fileInfo) throws IOException {
		printWriter.println("UploadFile");
		String[] infos = fileInfo.toString().split(MyInfo.separator);
		for (int i = 0; i < 5; i++) {
			printWriter.println(infos[i]);
		}
		printWriter.flush();
		return socket.getOutputStream();
	}

	// ���͵�½����,�����������������Ϣ,��������ݷ����ĵ���Ϣ���û���Ϣ
	public String loginRequestion(String userName, String userPassword)
			throws SocketException, IOException, TimeoutException, DataException {
		BufferedReader respondReader;
		String respond = null;
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
		printWriter.println("Login");
		printWriter.println(userName);
		printWriter.println(userPassword);
		printWriter.flush();
		respondReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		try {
			socket.setSoTimeout(60000); // 1min��ʱ�趨
		} catch (Exception timeOutException) {
			throw new TimeoutException();
		}
		respond = respondReader.readLine();
		// �ڴ���Ҫ��������ٴν�����Ϣ�Գ�ʼ��hashtable
		if (respond != null) {
			if (respond.equals("Administrator")) {
				DataProcessing.initUserInfo(respondReader);
				printWriter.flush();
			}
			DataProcessing.initDocInfo(respondReader);
		} else {
			respond = "Wrong";
		}
		respondReader.close();
		return respond;
	}

	// �ر�����
	public void close() throws IOException {
		socket.close();
	}
}
