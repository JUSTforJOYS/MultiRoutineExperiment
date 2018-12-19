import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FrameDownloadFile extends FrameFunction {

	private static final long serialVersionUID = 1L;
	private static String fileServecePath = "d:\\Multithreading\\Files\\";
	private File placeFileToSave;
	private JProgressBar fileDownProgressBar;
	private DefaultTableModel tableModel;
	private JButton buttonDownload;
	private JTable table;

	FrameDownloadFile(User user) {
		super(user);

//		private FramChangeUserInfo framChangeUserInfo;

		setTitle("�����ĵ�");
		setSize(640, 400);
		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();
		int screenHeight = dimension.height;
		int screenWidth = dimension.width;
		int frm_Height = this.getHeight();
		int frm_width = this.getWidth();
		this.setLocation((screenWidth - frm_width) / 2, (screenHeight - frm_Height) / 2);

		// �û��б�Ƭ
		buttonDownload = new JButton("����");
		fileDownProgressBar = new JProgressBar();
		fileDownProgressBar.setPreferredSize(new Dimension(400, 48));
		this.getContentPane().add(BorderLayout.NORTH, fileDownProgressBar);
		this.getContentPane().add(BorderLayout.SOUTH, buttonDownload);

		Enumeration<Doc> docs = DataProcessing.getAllDocs();
		Object[][] cellData = new Object[DataProcessing.getDocNumber()][5];
		{
			int i = 0;
			while (docs.hasMoreElements()) {
				Doc tempDoc = docs.nextElement();
				cellData[i][0] = tempDoc.getID();
				cellData[i][1] = tempDoc.getCreator();
				cellData[i][2] = tempDoc.getFilename();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cellData[i][3] = simpleDateFormat.format(tempDoc.getTimestamp());
				cellData[i][4] = tempDoc.getDescription();
				i++;
			}
		}
		String[] columnNames = { "ID", "������", "ԭʼ�ļ���", "����ʱ��", "����" };

		tableModel = new DefaultTableModel(cellData, columnNames) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable();
		table.setModel(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		this.getContentPane().add(BorderLayout.CENTER, scrollPane);

		buttonDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int currentRow = table.getSelectedRow();
				if (currentRow == -1) {
					JOptionPane.showMessageDialog(FrameDownloadFile.this, "����ѡ��һ��", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				Object objectID = tableModel.getValueAt(currentRow, 0);
				String ID = (String) objectID;
				Doc fileInfo = DataProcessing.searchDoc(ID);
				File toDownloadFile = new File(fileServecePath + ID + "." + fileInfo.getFileType());
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int fileChooserResult = fileChooser.showSaveDialog(FrameDownloadFile.this);
				if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
					placeFileToSave = fileChooser.getSelectedFile();
					if (placeFileToSave.getAbsolutePath().endsWith("\\")) {
						placeFileToSave = new File(placeFileToSave.getAbsolutePath() + fileInfo.getFilename());
					} else {
						placeFileToSave = new File(placeFileToSave.getAbsolutePath() + "\\" + fileInfo.getFilename());
					}
					new Thread() {
						@Override
						public void run() {
							try {
								placeFileToSave.createNewFile();
								FileInputStream fileInput = new FileInputStream(toDownloadFile);
								FileOutputStream fileOutput = new FileOutputStream(placeFileToSave);
								FileChannel readFileChannel = fileInput.getChannel();
								FileChannel writeFileChannel = fileOutput.getChannel();
								int currentProgress = 0;
								long fileSize = readFileChannel.size();
								buttonDownload.setEnabled(false);
								fileDownProgressBar.setValue(0);
								fileDownProgressBar.setMaximum(100);
								for (long fileContent = 0L, transferSize; fileContent <= fileSize; fileContent += 100L) {
									if (fileSize - fileContent >= 100L)
										transferSize = 100L;
									else {
										transferSize = fileSize - fileContent;
									}
									writeFileChannel.transferFrom(readFileChannel, fileContent, transferSize);
									currentProgress = (int) ((double) fileContent * 100.0 / (double) fileSize);
									fileDownProgressBar.setValue(currentProgress);
									fileDownProgressBar.setString("���������" + currentProgress + "%");
									fileDownProgressBar.setStringPainted(true);
								}
								fileDownProgressBar.setValue(100);
								fileDownProgressBar.setString("���������");
								fileDownProgressBar.setStringPainted(true);
								fileInput.close();
								fileOutput.close();
								buttonDownload.setEnabled(true);
							} catch (FileNotFoundException fileNotFoundException) {
								JOptionPane.showMessageDialog(FrameDownloadFile.this, "�ļ���ʧ��", "��ʾ",
										JOptionPane.INFORMATION_MESSAGE);
							} catch (IOException ioException) {
								JOptionPane.showMessageDialog(FrameDownloadFile.this, "����ʧ��", "��ʾ",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}.start();

				} else if (fileChooserResult == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(FrameDownloadFile.this, "��ȡ������", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(FrameDownloadFile.this, "�ļ�ѡ������쳣", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				FrameUser frameUser;
				if (user.getRole().equalsIgnoreCase("Administrator")) {
					frameUser = new FrameAdministrator(user);
				} else if (user.getRole().equalsIgnoreCase("Operator")) {
					frameUser = new FrameOperator(user);
				} else {
					frameUser = new FrameBrowser(user);
				}
				frameUser.init();
			};

		});
	}

	@Override
	public void initial() {
		setVisible(true);
	}
}
