package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

public class FrameUploadFile extends FrameFunction {

	private static final long serialVersionUID = 1L;
	private JLabel labelID;
	private JLabel labelFileID;
	private JLabel labelFilePath;
	private JLabel labelFileDescription;
	private JTextField filePathField;
	private JTextArea fileDescriptionArea;
	private JPanel functionPanel;
	private JProgressBar fileDownProgressBar;
	private JButton sureButton;
	private JButton skimButton;
	private File toUploadFile;
	private Doc toUploadFileInfo;

	FrameUploadFile(User user) {
		super(user);

		labelID = new JLabel("" + DataProcessing.getDocNumber());
		labelFileID = new JLabel("��  ��ID:    ");
		labelFilePath = new JLabel("�ĵ�·��:");
		labelFileDescription = new JLabel("�ĵ�����:");

		filePathField = new JTextField(25);
		fileDescriptionArea = new JTextArea(30, 20);
		fileDescriptionArea.setLineWrap(true);

		sureButton = new JButton("ȷ��");
		skimButton = new JButton("���");

		setTitle("�ϴ��ĵ�");
		setSize(350, 300);
		setLocationRelativeTo(null);
		fileDownProgressBar = new JProgressBar();
		functionPanel = new JPanel();
		fileDownProgressBar.setPreferredSize(new Dimension(350, 48));
		this.getContentPane().add(BorderLayout.NORTH, fileDownProgressBar);
		this.getContentPane().add(BorderLayout.CENTER, functionPanel);

		// �ļ������������»س�,��ʾѡ���ϴ�
		fileDescriptionArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				if (keyEvent.getKeyChar() == '\n') {
					// �����˻س��� ȥ���ı��еĻس� ������ϴ�
					fileDescriptionArea.setText(fileDescriptionArea.getText().replaceAll("\n", ""));
					sureButton.doClick();
				}
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
			}

			@Override
			public void keyPressed(KeyEvent keyEvent) {
			}
		});

		// Ϊָ���� Container ���� GroupLayout
		GroupLayout layout = new GroupLayout(functionPanel);
		functionPanel.setLayout(layout);

		// ����GroupLayout��ˮƽ�����飬��Խ�ȼ����ParallelGroup�����ȼ�����Խ�ߡ�
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.addGap(5);// ��Ӽ��
		hGroup.addGroup(layout.createParallelGroup().addComponent(labelFileID).addComponent(labelFilePath)
				.addComponent(labelFileDescription));
		hGroup.addGap(5);
		hGroup.addGroup(layout.createParallelGroup().addComponent(labelID).addComponent(filePathField)
				.addComponent(fileDescriptionArea).addComponent(sureButton));
		hGroup.addGap(5);
		hGroup.addGroup(layout.createParallelGroup().addComponent(skimButton));
		layout.setHorizontalGroup(hGroup);

		// ����GroupLayout�Ĵ�ֱ�����飬��Խ�ȼ����ParallelGroup�����ȼ�����Խ�ߡ�
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGap(10);
		vGroup.addGroup(layout.createParallelGroup().addComponent(labelFileID).addComponent(labelID));
		vGroup.addGap(10);
		vGroup.addGroup(layout.createParallelGroup().addComponent(labelFilePath).addComponent(filePathField)
				.addComponent(skimButton));
		vGroup.addGap(10);
		vGroup.addGroup(
				layout.createParallelGroup().addComponent(labelFileDescription).addComponent(fileDescriptionArea));
		vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(sureButton));
		vGroup.addGap(10);
		// ���ô�ֱ��
		layout.setVerticalGroup(vGroup);

//		pack();
		skimButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int fileChooserResult = fileChooser.showDialog(FrameUploadFile.this, "ѡ���ļ�");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
					toUploadFile = fileChooser.getSelectedFile();
					if (!toUploadFile.exists()) {
						JOptionPane.showMessageDialog(FrameUploadFile.this, "δѡ��", "��ʾ",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						filePathField.setText(toUploadFile.getAbsolutePath());
					}
				} else if (fileChooserResult == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(FrameUploadFile.this, "��ȡ��ѡ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(FrameUploadFile.this, "�ļ�ѡ������쳣", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		// ��Ӽ�����
		sureButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (toUploadFile == null) {
					JOptionPane.showMessageDialog(FrameUploadFile.this, "δѡ��Ҫ�ϴ����ļ�!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (MyInfo.separator.matches(fileDescriptionArea.getText())) {
					JOptionPane.showMessageDialog(FrameUploadFile.this, "�ļ������в��ܳ���" + MyInfo.separator, "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
				}
				toUploadFileInfo = new Doc("" + DataProcessing.getDocNumber(), user.getName(),
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()), "(�ϴ���δд��д����)",
						toUploadFile.getAbsolutePath().substring(toUploadFile.getAbsolutePath().lastIndexOf('\\') + 1));
				if (fileDescriptionArea.getText().length() != 0)
					toUploadFileInfo.setDescription(fileDescriptionArea.getText());
				new Thread() {
					@Override
					public void run() {
						try {
							// ��������
							DataRequestion dataRequestion;
							try {
								dataRequestion = new DataRequestion();
							} catch (UnknownHostException addressError) {
								JOptionPane.showMessageDialog(FrameUploadFile.this, "��������ַ����!", "��ʾ",
										JOptionPane.INFORMATION_MESSAGE);
								return;
							} catch (IOException connectError) {
								JOptionPane.showMessageDialog(FrameUploadFile.this, "�����������쳣!", "��ʾ",
										JOptionPane.INFORMATION_MESSAGE);
								return;
							}
							// ��������
							DataOutputStream fileOutput = new DataOutputStream(
									dataRequestion.uploadRequestion(toUploadFileInfo));
							DataInputStream fileInput = new DataInputStream(new FileInputStream(toUploadFile));
							// �ȴ����С
							long fileSize = toUploadFile.length();
							fileOutput.writeLong(fileSize);

							fileOutput.flush();
							// �����ļ�
							int length = 0;
							long curLength = 0;
							float currentProgress = 0;
							final int BytesEachTime = 10000;
							byte[] sendBytes = new byte[BytesEachTime];
							sureButton.setEnabled(false);
							skimButton.setEnabled(false);
							fileDownProgressBar.setValue(0);
							fileDownProgressBar.setMaximum(100);
							while ((length = fileInput.read(sendBytes, 0, sendBytes.length)) > 0) {
								fileOutput.write(sendBytes, 0, length);
								curLength = curLength + length;
								currentProgress = (float) ((double) curLength / (double) fileSize * 100.0);
								fileDownProgressBar.setValue((int) currentProgress);
								fileDownProgressBar.setString("�ϴ������" + currentProgress + "%");
								fileDownProgressBar.setStringPainted(true);
							}
							// TODO ���ܷ��������صĴ�
							fileDownProgressBar.setValue(100);
							fileDownProgressBar.setString("�ϴ����");
							fileDownProgressBar.setStringPainted(true);
							fileOutput.flush();
							// ������Ϣ����
							DataProcessing.insertDoc(toUploadFileInfo);
							JOptionPane.showMessageDialog(FrameUploadFile.this, "�ϴ��ɹ�!", "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
							FrameUploadFile frameUploadFile = new FrameUploadFile(user);
							frameUploadFile.initial();
							// �ر��ļ���
							fileInput.close();
							try {
								dataRequestion.close();
							} catch (IOException closeError) {
								JOptionPane.showMessageDialog(FrameUploadFile.this, "δ�ɹ��Ͽ��������������!�����ر�ϵͳ", "��ʾ",
										JOptionPane.INFORMATION_MESSAGE);
								DataProcessing.systemQuit();
							}
						} catch (FileNotFoundException fileNotFoundException) {
							JOptionPane.showMessageDialog(FrameUploadFile.this, "·������!", "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(FrameUploadFile.this, "�ϴ�ʧ��!", "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
				}.start();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				FrameOperator frameOperator = new FrameOperator(user);
				frameOperator.init();
			};
		});
	}

	@Override
	public void initial() {
		setVisible(true);
	}
}
