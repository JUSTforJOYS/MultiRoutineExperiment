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
	private static String fileServecePath = "d:\\Multithreading\\Files\\";
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

		sureButton = new JButton("ȷ��");
		skimButton = new JButton("���");

		setTitle("�ϴ��ĵ�");
		setSize(350, 300);
		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();
		int screenHeight = dimension.height;
		int screenWidth = dimension.width;
		int frm_Height = this.getHeight();
		int frm_width = this.getWidth();
		this.setLocation((screenWidth - frm_width) / 2, (screenHeight - frm_Height) / 2);

		fileDownProgressBar = new JProgressBar();
		functionPanel = new JPanel();
		fileDownProgressBar.setPreferredSize(new Dimension(350, 48));
		this.getContentPane().add(BorderLayout.NORTH, fileDownProgressBar);
		this.getContentPane().add(BorderLayout.CENTER, functionPanel);

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
		// ��Ӽ�����
		sureButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (toUploadFile == null) {
					JOptionPane.showMessageDialog(FrameUploadFile.this, "δѡ��Ҫ�ϴ����ļ�!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				toUploadFileInfo = new Doc("" + DataProcessing.getDocNumber(), user.getName(), new Date().getTime(),
						"(�ϴ���δд��д����)",
						toUploadFile.getAbsolutePath().substring(toUploadFile.getAbsolutePath().lastIndexOf('\\') + 1));
				if (fileDescriptionArea.getText() != null)
					toUploadFileInfo.setDescription(fileDescriptionArea.getText());
				new Thread() {
					@Override
					public void run() {
						try {
							FileInputStream fileInput;
							fileInput = new FileInputStream(filePathField.getText());

							FileOutputStream fileOutput;
							fileOutput = new FileOutputStream(fileServecePath + toUploadFileInfo.getID() + toUploadFile
									.getAbsolutePath().substring(toUploadFile.getAbsolutePath().lastIndexOf('.')));
							FileChannel readFileChannel = fileInput.getChannel();
							FileChannel writeFileChannel = fileOutput.getChannel();

							int currentProgress = 0;
							long fileSize = readFileChannel.size();
							if (fileSize == 0L) {
								JOptionPane.showMessageDialog(FrameUploadFile.this, "�����ϴ����ļ�!", "��ʾ",
										JOptionPane.INFORMATION_MESSAGE);
								fileInput.close();
								fileOutput.close();
								return;
							}
							sureButton.setEnabled(false);
							skimButton.setEnabled(false);
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
								fileDownProgressBar.setString("�ϴ������" + currentProgress + "%");
								fileDownProgressBar.setStringPainted(true);
							}
							fileDownProgressBar.setValue(100);
							fileDownProgressBar.setString("�ϴ������");
							fileDownProgressBar.setStringPainted(true);
							DataProcessing.insertDoc(toUploadFileInfo);
							JOptionPane.showMessageDialog(FrameUploadFile.this, "�ϴ��ɹ�!", "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
							FrameUploadFile frameUploadFile = new FrameUploadFile(user);
							frameUploadFile.initial();
							fileInput.close();
							fileOutput.close();
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
