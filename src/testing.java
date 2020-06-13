import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.JList;
import java.awt.Choice;
import javax.swing.JCheckBoxMenuItem;
import java.awt.Label;
import javax.swing.JSeparator;
import java.awt.TextArea;

/**
 * Date: 4/26/2020 JFileSplitter: Toona
 * 
 * @author Lorenzo
 * @version 1.2
 * 
 */
public class testing {

	static JFrame PBF = null;
	static JProgressBar progressBar;
	static JPanel PBP = null;

	private DefaultTableModel model;
	private JFrame frame;
	private JTable table;
	private JButton MergeButton;
	private JButton SplitButton;
	private JButton trashcan;
	private JTextField textFieldCut;
	private JTextField Password;


	/**
	 * @param args default main's values
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testing window = new testing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public testing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("deprecation")
	public void initialize() {
		frame = new JFrame("Toona"); // Nome Programma nella Barra
		frame.getContentPane().setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 12));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/Toona.png")));
		frame.setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 745, 268);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(268, 13, 414, 169);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 12));
		table.setCellSelectionEnabled(true);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "File", "Job" }) {
			Class[] columnTypes = new Class[] { String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(1).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setMaxWidth(40);
		scrollPane.setViewportView(table);
		
		Choice SplitType = new Choice();
		SplitType.setBounds(10, 13, 83, 29);
		frame.getContentPane().add(SplitType);
		SplitType.add("Default");
		SplitType.add("Zip");
		SplitType.add("Crypt");
		
		textFieldCut = new JTextField();
		textFieldCut.setEnabled(false);
		textFieldCut.setText("0");
		textFieldCut.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		textFieldCut.setBounds(154, 98, 37, 23);
		frame.getContentPane().add(textFieldCut);
		textFieldCut.setColumns(10);
		
		//RadioButtons
		JRadioButton NumbPart = new JRadioButton("Number of Part");
		NumbPart.setSelected(true);
		NumbPart.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 12));
		NumbPart.setBounds(10, 50, 135, 23);
		frame.getContentPane().add(NumbPart);
		
		JRadioButton CustomSize = new JRadioButton("Custom Size Cut");
		CustomSize.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 12));
		CustomSize.setBounds(143, 50, 125, 23);
		frame.getContentPane().add(CustomSize);
		
		ButtonGroup group = new ButtonGroup();
		group.add(NumbPart);
		group.add(CustomSize);
		
		//Spinner
		JSpinner parter = new JSpinner();
		parter.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		parter.setBounds(47, 99, 37, 20);
		frame.getContentPane().add(parter);
		System.out.println("Stampa : " + CustomSize.isSelected());
		

		SplitButton = new JButton("Split");
		SplitButton.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 17));
		SplitButton.setBackground(new Color(204, 204, 204));
		SplitButton.setBounds(10, 182, 104, 26);
		frame.getContentPane().add(SplitButton);

		Choice choice = new Choice();
		choice.setEnabled(false);
		choice.setBounds(197, 98, 55, 26);
		frame.getContentPane().add(choice);
		choice.add("B");
		choice.add("KB");
		choice.add("MB");
		choice.add("GB");
			
		Password = new JPasswordField();
		Password.setEnabled(false);
		Password.setBounds(122, 136, 136, 24);
		frame.getContentPane().add(Password);
		Password.setColumns(10);

		JLabel cutter_label = new JLabel("Cut");
		cutter_label.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		cutter_label.setBounds(117, 103, 27, 14);
		frame.getContentPane().add(cutter_label);

		JLabel parter_label = new JLabel("Part");
		parter_label.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		parter_label.setBounds(10, 97, 27, 23);
		frame.getContentPane().add(parter_label);
		
		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 136, 83, 23);
		frame.getContentPane().add(lblNewLabel);
		
		Label Setting_Before_Split = new Label("Settings before Split");
		Setting_Before_Split.setFont(new Font("Microsoft Sans Serif", Font.ITALIC, 12));
		Setting_Before_Split.setBounds(99, 13, 153, 20);
		frame.getContentPane().add(Setting_Before_Split);
	
		
		//Coordinamento
	    SplitType.addItemListener(new ItemListener(){
	        public void itemStateChanged(ItemEvent ie)
	        {
	        	String TipoSplit = SplitType.getSelectedItem().toString();
	        	if (TipoSplit == "Default" || TipoSplit == "Zip")
	        	{	
	        		Password.setEnabled(false);
	        	}
	        	else {
	        		Password.setEnabled(true);
	        	}
	        }
	    });
		
		CustomSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(CustomSize.isEnabled())
					choice.setEnabled(true);
					textFieldCut.setEnabled(true);
					parter.setEnabled(false);
					
				}			
			});
		
		NumbPart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(NumbPart.isEnabled())
					parter.setEnabled(true);
					choice.setEnabled(false);
					textFieldCut.setEnabled(false);
				
				}
			});
		
		//Declaration of Generic queue File queue
		GenQueue<File_Input> FileQ = new GenQueue<File_Input>();
		
		//SPLIT
		SplitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean Z = false, C = false;
				
				Boolean CutType = NumbPart.isSelected(); //Same size, Custom Size
				String CutSize = choice.getItem(choice.getSelectedIndex()); //Bytes, Megabytes,... 
				String TipoSplit = SplitType.getSelectedItem().toString(); //Default, Crypt, Split
				
				System.out.println("Split Type: " + TipoSplit);
				if (TipoSplit == "Zip") {
					Z = true;
				} else if (TipoSplit == "Crypt") {
					C = true;
					} else {
						Z = false;
						C = false;
					}

				int parter_value = (Integer) parter.getValue();
				int cutter_value = Integer.parseInt(textFieldCut.getText());

				String[] file_name = FileChooser();
				String file_job = "S";
				for (int i = 0; i < file_name.length; i++) {
					Object[] file_split = { file_name[i], file_job };
					model = (DefaultTableModel) table.getModel();
					model.addRow(file_split);
				}

				for (int i = 0; i < file_name.length; i++) {
					FileQ.enqueue(new File_Input(file_name[i], file_job, Z, C, parter_value, cutter_value, CutSize, CutType));
				}

				System.out.println("List's Dimension: " + FileQ.size());
			}
		});

		// MERGE
		MergeButton = new JButton("Merge");
		MergeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] file_name = FileChooser();
				String file_job = "M";
				for (int i = 0; i < file_name.length; i++) {
					Object[] file_merge = { file_name[i], file_job };
					model = (DefaultTableModel) table.getModel();
					model.addRow(file_merge);
				}

				for (int i = 0; i < file_name.length; i++) {
					FileQ.enqueue(new File_Input(file_name[i], file_job, false, false, 0, 0, null, null));
				}
			}
		});
		MergeButton.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 17));
		MergeButton.setBackground(new Color(204, 204, 204));
		MergeButton.setBounds(154, 182, 104, 26);
		frame.getContentPane().add(MergeButton);
		// END MERGE


		trashcan = new JButton("");
		trashcan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getHeight() != 0) {
					model.removeRow(0);
					FileQ.dequeue();
					System.out.println("Dimesione attuale :" + FileQ.size());
				}
			}
		});
		trashcan.setIcon(new ImageIcon(testing.class.getResource("/Images/Trash-White-Full-icon.png")));
		trashcan.setBackground(new Color(192, 192, 192));
		trashcan.setForeground(new Color(0, 0, 0));
		trashcan.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 9));
		trashcan.setBounds(692, 13, 27, 23);
		frame.getContentPane().add(trashcan);

		JButton refresh = new JButton("");
		refresh.setBackground(UIManager.getColor("InternalFrame.activeTitleGradient"));
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				while (FileQ.hasItems()) {
					model.removeRow(0);
					File_Input emp = FileQ.dequeue();
					System.out.println(emp.getFileName());
				}
			}
		});
		refresh.setIcon(new ImageIcon(testing.class.getResource("/Images/refresh.png")));
		refresh.setBounds(692, 156, 27, 26);
		frame.getContentPane().add(refresh);

		
		//START
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 17));
		btnNewButton.setBackground(UIManager.getColor("InternalFrame.activeTitleGradient"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				while (FileQ.hasItems()) {
					File_Input emp = FileQ.dequeue();
					if (emp.JobName() == "S") {
							if (emp.FileZip()){
								try {
									Zip Zip = new Zip();
									Zip.zipFiles(emp.getFileName(), emp.FilePart(), emp.FileCut(), emp.CutSize(), emp.CutType());

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else if (emp.FileCrypt()) {
								System.out.println("Entro in crypt mode");
								try {
									Crypt Crypt = new Crypt();
									Crypt.cryptFiles(emp.getFileName(), emp.FilePart(), emp.FileCut(), Password.getText(), emp.CutSize(), emp.CutType());
									

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CryptoException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								try {
									Split Split = new Split();
									Split.splitFile(emp.getFileName(), emp.FilePart(), emp.FileCut(), emp.CutSize(), emp.CutType());

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}						
							}
					} else if (emp.JobName() == "M") {
						try {
							System.out.println("Sto chiamando Merge");
							Merge Merge = new Merge();
							Merge.Merge(emp.getFileName());

						} catch (IOException e) {
							e.printStackTrace();
						} catch (GeneralSecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				model.removeRow(0);
				} // end of while(FileQ.hasItems())
			}
		});

		btnNewButton.setBounds(427, 185, 94, 33);
		frame.getContentPane().add(btnNewButton);
		
	}

	
	// FILECHOOSER
	public String[] FileChooser() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Multiple file selection:");
		jfc.setMultiSelectionEnabled(true);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		File[] files = null;

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			files = jfc.getSelectedFiles();
		}
		String[] files_names = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			files_names[i] = files[i].getAbsolutePath();
		}
		return files_names;
	}
	
	public Boolean SelectionCheck(JRadioButton B) {
		if (B.isSelected())
			return true;
		else
			return false;
	}
}
