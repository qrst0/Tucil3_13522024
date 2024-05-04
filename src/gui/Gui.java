package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

import algorithm.*;
import pair.Pair;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.NumberFormatter;

class Constants {
	public static String fileSeparator = File.separator;
	public static Font customFont = new Font(null);
	public static Color boxColor = new Color(237,209,211);
	public static void initConstants() {
		try {
		InputStream myStream = new BufferedInputStream(new FileInputStream("." + fileSeparator + "assets" + fileSeparator + "Futura Bold.otf"));
		customFont = Font.createFont(Font.TRUETYPE_FONT, myStream).deriveFont(20f);
		}catch(Exception e) {
			
		}
	}
}

class ExtendedTextField extends JTextField{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int index;
	public boolean listenNow;
	public char lastString = ' ';
	public ExtendedTextField(int index) {
		super();
		this.index = index;
		listenNow = true;
		// TODO Auto-generated constructor stub
	}
}

final class InputBox {
	public JPanel panel;
	Stack<ExtendedTextField> fields;
	int n;
	
	public InputBox(String inputString, String endString, int diff) {
		this.n = inputString.length();
		fields = new Stack<ExtendedTextField>();
		for(int i = 0; i < inputString.length(); ++i) {
			ExtendedTextField jte = new ExtendedTextField(i);
			jte.setPreferredSize(new Dimension(50, 50));
			String textString = "" + inputString.charAt(i);
			jte.setText(textString);
			jte.setBackground(Constants.boxColor);
			//fields[i].setBackground(new Color(211,211,211));
			jte.setBorder(BorderFactory.createLineBorder(new Color(51,51,51), 3));
			if(i == diff) {
				jte.setBorder(BorderFactory.createLineBorder(new Color(237, 130, 0), 5));
			}
			jte.setHorizontalAlignment(JTextField.CENTER);
			jte.setFont(Constants.customFont);
			jte.setEditable(false);
			if(inputString.charAt(i) == endString.charAt(i)) {
				//System.out.println("HERE");
				jte.setBackground(new Color(110, 237, 133));
			}
			//jte.setOpaque(true);
			fields.add(jte);
		}
		GridLayout gridLayout = new GridLayout(1, n);
		gridLayout.setHgap(5);
		panel = new JPanel(gridLayout);
		for(int i = 0; i < n; ++i) {
			panel.add(fields.get(i));
		}
		//panel.setPreferredSize(new Dimension(300, 50));
		panel.setOpaque(false);
	}
	
	public InputBox(int n) {
		this.n = n;
		fields = new Stack<ExtendedTextField>();
		for(int i = 0; i < n; ++i) {
			ExtendedTextField jte = new ExtendedTextField(i);
			jte.setPreferredSize(new Dimension(50, 50));
			//jte.setOpaque(false);
			jte.setBackground(Constants.boxColor);
			//fields[i].setBackground(new Color(211,211,211));
			jte.setBorder(BorderFactory.createLineBorder(new Color(51,51,51), 3));
			jte.setHorizontalAlignment(JTextField.CENTER);
			jte.setFont(Constants.customFont);
			jte.setText(" ");
			jte.setCaretPosition(1);
			jte.getDocument().addDocumentListener(new DocumentListener() {
				  public void changedUpdate(DocumentEvent e) {
				    //warn();
				  }
				  public void removeUpdate(DocumentEvent e) {
					  if(!jte.listenNow) return;
					  //System.out.println(jte.index);
					  Runnable doHighlight = new Runnable() {
					        @Override
					        public void run() {
					        	if(!jte.listenNow) return;
					        	jte.lastString = ' ';
					        	jte.listenNow = false;
					        	jte.setText(" ");
					        	jte.listenNow = true;
					        	fields.get(((jte.index - 1) + fields.size()) % fields.size()).requestFocus();
					        }
					    };       
					  SwingUtilities.invokeLater(doHighlight);
				  }
				  public void insertUpdate(DocumentEvent e) {
					  if(!jte.listenNow) return;
					  Runnable doHighlight = new Runnable() {
					        @Override
					        public void run() {
					        	if(!jte.listenNow) return;
					        	String nowString = jte.getText();
					        	char lString = jte.lastString;
					        	char nowChar;
					        	if(lString != ' ') {
					        		if(lString == nowString.charAt(0)) {
					        			if(nowString.length() == 1) {
					        				return;
					        			}
					        			nowChar = nowString.charAt(1);
					        		}else {
					        			nowChar = nowString.charAt(0);
					        		}
					        	}else {
					        		if(nowString.length() == 1) {
					        			nowChar = nowString.charAt(nowString.length() - 1);
					        		} else {
					        			if(lString == nowString.charAt(0)) {
						        			nowChar = nowString.charAt(1);
						        		}else {
						        			nowChar = nowString.charAt(0);
						        		}

					        		}
					        	}
					        	String takeString = "" + Character.toUpperCase(nowChar);
					        	jte.listenNow = false;
					        	jte.setText(takeString);
					        	jte.listenNow = true;
					        	jte.lastString = takeString.charAt(0);
								fields.get(((jte.index + 1) + fields.size()) % fields.size()).requestFocus();
					        }
					    };       
					  SwingUtilities.invokeLater(doHighlight);
				  }
			});
			fields.add(jte);
		}
		GridLayout gridLayout = new GridLayout(1, n);
		gridLayout.setHgap(5);
		panel = new JPanel(gridLayout);
		for(int i = 0; i < n; ++i) {
			panel.add(fields.get(i));
		}
		//panel.setPreferredSize(new Dimension(300, 50));
		panel.setOpaque(false);
	}
	public void change(int n) {
		if(n == this.n) return;
		if(n > this.n) {
			for(; this.n < n; ++this.n) {
				ExtendedTextField newJTextField = new ExtendedTextField(this.n);
				newJTextField.setPreferredSize(new Dimension(50, 50));
				//fields[i].setBackground(new Color(211,211,211));
				newJTextField.setBorder(BorderFactory.createLineBorder(new Color(51,51,51), 3));
				newJTextField.setHorizontalAlignment(JTextField.CENTER);
				newJTextField.setFont(Constants.customFont);
				newJTextField.setBackground(Constants.boxColor);
				newJTextField.setText(" ");
				newJTextField.setCaretPosition(1);
				newJTextField.getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
					    //warn();
					  }
					  public void removeUpdate(DocumentEvent e) {
						  if(!newJTextField.listenNow) return;
						  //System.out.println(jte.index);
						  Runnable doHighlight = new Runnable() {
						        @Override
						        public void run() {
						        	newJTextField.lastString = ' ';
						        	newJTextField.listenNow = false;
						        	newJTextField.setText(" ");
						        	newJTextField.listenNow = true;
						        	fields.get(((newJTextField.index - 1) + fields.size()) % fields.size()).requestFocus();
						        }
						    };       
						  SwingUtilities.invokeLater(doHighlight);
					  }
					  public void insertUpdate(DocumentEvent e) {
						  if(!newJTextField.listenNow) return;
						  Runnable doHighlight = new Runnable() {
						        @Override
						        public void run() {
						        	String nowString = newJTextField.getText();
						        	char lString = newJTextField.lastString;
						        	char nowChar;
						        	if(lString != ' ') {
						        		if(lString == nowString.charAt(0)) {
						        			if(nowString.length() == 1) {
						        				return;
						        			}
						        			nowChar = nowString.charAt(1);
						        		}else {
						        			nowChar = nowString.charAt(0);
						        		}
						        	}else {
						        		if(nowString.length() == 1) {
						        			nowChar = nowString.charAt(nowString.length() - 1);
						        		} else {
						        			if(lString == nowString.charAt(0)) {
							        			nowChar = nowString.charAt(1);
							        		}else {
							        			nowChar = nowString.charAt(0);
							        		}

						        		}
						        	}
						        	String takeString = "" + Character.toUpperCase(nowChar);
						        	newJTextField.listenNow = false;
						        	newJTextField.setText(takeString);
						        	newJTextField.listenNow = true;
						        	newJTextField.lastString = takeString.charAt(0);
									fields.get(((newJTextField.index + 1) + fields.size()) % fields.size()).requestFocus();
						        }
						    };       
						  SwingUtilities.invokeLater(doHighlight);
					  }
				});
				fields.add(newJTextField);
				panel.add(newJTextField);
			}
		}else {
			for(; this.n > n; --this.n) {
				panel.remove(fields.peek());
				fields.pop();
			}
		}
		panel.revalidate();
		panel.repaint();
	}
	
	public String getString() {
		String resultString = "";
		for(int i = 0; i < fields.size(); ++i) {
			resultString += fields.get(i).getText();
		}
		return resultString;
	}
	
}

class MyFormatter extends NumberFormatter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object stringToValue(String text) {
		if(text.equals("")) {
			return null;
		}else {
			try {
				return super.stringToValue(text);
			}catch(Exception e) {
				//System.out.println(text);
				int x = 0, mexi = 0;
				String s = this.getMaximum().toString();
				int cur = Integer.parseInt(s);
				for(int i = 0; i < text.length() - 1 && x <= cur; ++i) {
					mexi = x;
					x = x * 10 + Character.getNumericValue(text.charAt(i));
				}
				//System.out.print(text);
				//System.out.print(" ");
				//System.out.print(x);
				//System.out.println();
				if(x > cur) {
					x = mexi;
				}
				if(x == 0) {
					return null;
				}
				return x;
			}
		}
	}
}

class ResultDialog {
	public ResultDialog(JFrame parent, List<String> result, int time, int visitedNodes) {
		
		try {
		
		JLabel solLabel = new JLabel("Solusi:");
		solLabel.setFont(Constants.customFont.deriveFont(18f));
		solLabel.setHorizontalAlignment(JLabel.CENTER);
		
		UIManager.put("TextField.inactiveBackground", new ColorUIResource(Color.white));
		JDialog dialRanDialog = new JDialog(parent, "Solution", true);
		JPanel mainPanel = new JPanel();
		dialRanDialog.setSize(new Dimension(500, 550));
		dialRanDialog.setMinimumSize(new Dimension(500, 550));
		dialRanDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GridBagLayout gridLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(10, 3, 10, 3);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		mainPanel.setLayout(gridLayout);
		mainPanel.add(solLabel, gridBagConstraints);
		gridBagConstraints.gridy++;
		
		gridBagConstraints.insets = new Insets(3, 3, 10, 3);
		
		if(visitedNodes != -1) {
		
		for(int i = 0; i < result.size(); ++i) {
			int diff = -1;
			if(i != 0) {
				for(int j = 0; j < result.get(i).length(); ++j) {
					if(result.get(i).charAt(j) != result.get(i - 1).charAt(j)) {
						diff = j; 
						break;
					}
				}
			}
			InputBox inputBox = new InputBox(result.get(i).toUpperCase(), result.getLast().toUpperCase(), diff);
			mainPanel.add(inputBox.panel, gridBagConstraints);
			gridBagConstraints.gridy++;
		}
		
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JLabel longSeqLabel = new JLabel("Panjang sekuens: " + Integer.toString(result.size()));
		longSeqLabel.setFont(Constants.customFont.deriveFont(14f));
		longSeqLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(longSeqLabel, gridBagConstraints);
		
		gridBagConstraints.gridy++;
		
		JLabel timeLabel = new JLabel("Waktu berlalu: " + Integer.toString(time) + " ms");
		timeLabel.setFont(Constants.customFont.deriveFont(14f));
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(timeLabel, gridBagConstraints);
		
		gridBagConstraints.gridy++;
		
		gridBagConstraints.insets = new Insets(3, 3, 10, 3);
		
		JLabel nodeLabel = new JLabel("Simpul dikunjungi: " + Integer.toString(visitedNodes));
		nodeLabel.setFont(Constants.customFont.deriveFont(14f));
		nodeLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(nodeLabel, gridBagConstraints);
		
		
		} else {
		JLabel nosolLabel = new JLabel("Tidak ada solusi! :(");
		nosolLabel.setFont(Constants.customFont.deriveFont(20f));
		nosolLabel.setForeground(new Color(255, 87, 98));
		nosolLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(nosolLabel, gridBagConstraints);
		}
		JScrollPane scrPane = new JScrollPane(mainPanel);
		mainPanel.setBackground(new Color(110,211,237));
		dialRanDialog.add(scrPane);
		dialRanDialog.setVisible(true);
		
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}


public class Gui {
	protected Algorithm algorithm;
	
	public static void printUIManagerKeys()
	{
	    UIDefaults defaults = UIManager.getDefaults();
	    Enumeration<Object> keysEnumeration = defaults.keys();
	    ArrayList<Object> keysList = Collections.list(keysEnumeration);
	    for (Object key : keysList)
	    {
	        System.out.println(key);
	    }
	}
	
	public void start() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			
			/*
			UIManager.put("ComboBox.background", new ColorUIResource(Color.yellow));
	        UIManager.put("JTextField.background", new ColorUIResource(Color.yellow));
	        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(Color.magenta));
	        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.blue));
	        */
		}catch(Exception e) {
			
		}
		algorithm = new Algorithm();
		Constants.initConstants();
		ImageIcon img = new ImageIcon("." + Constants.fileSeparator + "assets" + Constants.fileSeparator + "ladder2.png");
		JFrame jFrame = new JFrame();
		jFrame.setIconImage(img.getImage());
		JPanel mainPanel = new JPanel();
		jFrame.setSize(500, 500);
		jFrame.setMinimumSize(new Dimension(600, 600));
		//ArrayList<String> result = algorithm.UCS("kris", "unau");
		//for(int i = 0; i < result.size(); ++i) {
			//System.out.println(result.get(i));
		//}
		mainPanel.setLayout(new GridBagLayout());
		JLabel titleLabel = new JLabel();
		titleLabel.setBackground(Color.red);
		titleLabel.setVisible(true);
		titleLabel.setOpaque(true);
		titleLabel.setPreferredSize(new Dimension(50, 50));
		
		InputBox inputBox = new InputBox(4);
		
		NumberFormat format = NumberFormat.getInstance();
		
		MyFormatter myf = new MyFormatter();
	    myf.setFormat(format);
	    myf.setValueClass(Integer.class);
	    myf.setMinimum(1);
	    myf.setMaximum(8);
	    myf.setAllowsInvalid(false);
	    myf.setCommitsOnValidEdit(true);
		
		JFormattedTextField jFormattedTextField = new JFormattedTextField(myf);
		jFormattedTextField.setPreferredSize(new Dimension(150, 25));
		jFormattedTextField.setHorizontalAlignment(JFormattedTextField.CENTER);
		jFormattedTextField.setFont(Constants.customFont.deriveFont(15f));
		jFormattedTextField.setText("4");
		jFormattedTextField.setBackground(Constants.boxColor);
		jFormattedTextField.setBorder(new LineBorder(new Color(51,51,51), 3));
		
		JLabel startJLabel = new JLabel();
		startJLabel.setFont(Constants.customFont.deriveFont(14f));
		startJLabel.setPreferredSize(new Dimension(150, 25));
		startJLabel.setText("MULAI: ");
		
		
		JLabel endJLabel = new JLabel();
		endJLabel.setFont(Constants.customFont.deriveFont(14f));
		endJLabel.setPreferredSize(new Dimension(150, 25));
		endJLabel.setText("AKHIR: ");
		
		JLabel panjangKataJLabel = new JLabel();
		panjangKataJLabel.setFont(Constants.customFont.deriveFont(14f));
		panjangKataJLabel.setPreferredSize(new Dimension(150, 25));
		panjangKataJLabel.setText("PANJANG KATA: ");
		
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints.weightx = 1.0;
		//gridBagConstraints.gridheight = 500;
		//gridBagConstraints.gridwidth = 250;
		mainPanel.add(panjangKataJLabel, gridBagConstraints);
		gridBagConstraints.insets = new Insets(3, 3, 10, 3);
		
		gridBagConstraints.gridy = 1;
		
		mainPanel.add(jFormattedTextField, gridBagConstraints);
		
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		//gridBagConstraints.gridheight = 500;
		//gridBagConstraints.gridwidth = 250;
		mainPanel.add(startJLabel, gridBagConstraints);
		JLabel labelTransparent = new JLabel();
		labelTransparent.setPreferredSize(new Dimension(100, 10));
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridx = 0;
		mainPanel.add(inputBox.panel, gridBagConstraints);
		
		InputBox inputBox1 = new InputBox(4);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		//gridBagConstraints.gridheight = 500;
		//gridBagConstraints.gridwidth = 250;
		mainPanel.add(labelTransparent, gridBagConstraints);
		gridBagConstraints.gridy = 5;
		mainPanel.add(endJLabel, gridBagConstraints);
		gridBagConstraints.gridy = 6;
		mainPanel.add(inputBox1.panel, gridBagConstraints);
		
		
		JRadioButton ucsRadioButton = null;
		JRadioButton astaRadioButton = null;
		JRadioButton gbfsJRadioButton = null;
		
		try {

			
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//.put("RadioButton.light", new ColorUIResource(Color.blue));
		
		//printUIManagerKeys();
		
		ucsRadioButton = new JRadioButton("UCS");
		ucsRadioButton.setFont(Constants.customFont.deriveFont(12f));
		ucsRadioButton.setFocusable(false);
		ucsRadioButton.setOpaque(false);
		ucsRadioButton.setSelected(true);
		
		astaRadioButton = new JRadioButton("A*");
		astaRadioButton.setFont(Constants.customFont.deriveFont(12f));
		astaRadioButton.setFocusable(false);
		astaRadioButton.setOpaque(false);
		
		gbfsJRadioButton = new JRadioButton("GBFS");
		gbfsJRadioButton.setFont(Constants.customFont.deriveFont(12f));
		gbfsJRadioButton.setFocusable(false);
		gbfsJRadioButton.setOpaque(false);
		
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		
		}catch(Exception e) {
			
		}
		
		ButtonGroup algoChooseButtonGroup = new ButtonGroup();
		
		algoChooseButtonGroup.add(ucsRadioButton);
		algoChooseButtonGroup.add(astaRadioButton);
		algoChooseButtonGroup.add(gbfsJRadioButton);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonPanel.setOpaque(false);
		
		buttonPanel.add(ucsRadioButton);
		buttonPanel.add(astaRadioButton);
		buttonPanel.add(gbfsJRadioButton);
		
		gridBagConstraints.gridy = 7;
		gridBagConstraints.insets = new Insets(40, 3, 3, 3);
		mainPanel.add(buttonPanel, gridBagConstraints);
		
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		
		JButton butRandGo = new JButton("GO");
	    butRandGo.setPreferredSize(new Dimension(100, 30));
	    butRandGo.setOpaque(true);
	    butRandGo.setContentAreaFilled(true);
	    butRandGo.setBackground(Constants.boxColor);
	    butRandGo.setBorder(BorderFactory.createLineBorder(new Color(51,51,51), 3));
	    butRandGo.setFont(Constants.customFont.deriveFont(20f));
	    //butRandGo.setForeground(Color.black);
	    butRandGo.setFocusPainted(false);
	    butRandGo.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if(model.isRollover()) {
					butRandGo.setBackground(new Color(252, 149, 156));
				}else {
					butRandGo.setBackground(Constants.boxColor);
				}
			}
		});
	    butRandGo.addActionListener(new Go(ucsRadioButton, astaRadioButton, gbfsJRadioButton, algorithm, inputBox, inputBox1, jFrame));
	    
	    gridBagConstraints.gridy = 8;
	    mainPanel.add(butRandGo, gridBagConstraints);
		
		
		mainPanel.setBackground(new Color(110,211,237));
		jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JScrollPane scrPane = new JScrollPane(mainPanel);
		jFrame.add(scrPane);
		
		
		
		
		jFormattedTextField.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    //warn();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  updateInputBox();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  updateInputBox();
			  }

			  public void updateInputBox() {
				 String cur = jFormattedTextField.getText();
				 int u = 0;
				 if(!cur.equals("")) {
					 u = Integer.parseInt(cur);
					 inputBox.change(u);
					 inputBox1.change(u);
				 }
			  }
		});
		
		jFrame.setVisible(true);
	}
}

class Go implements ActionListener {
	JRadioButton ucsButton;
	JRadioButton astarRadioButton;
	JRadioButton gbfsRadioButton;
	Algorithm algorithm;
	InputBox inputBox;
	InputBox inputBox1;
	JFrame jFrame;
	public Go(JRadioButton ucsButton, JRadioButton astarRadioButton, JRadioButton gbfsRadioButton, Algorithm algorithm, InputBox inputBox, InputBox inputBox1, JFrame jFrame){
		super();
		this.ucsButton = ucsButton;
		this.astarRadioButton = astarRadioButton;
		this.gbfsRadioButton = gbfsRadioButton;
		this.algorithm = algorithm;
		this.inputBox = inputBox;
		this.inputBox1 = inputBox1;
		this.jFrame = jFrame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String startWordString = inputBox.getString().toLowerCase();
		String endWordString = inputBox1.getString().toLowerCase();
		
		//System.out.println(startWordString);
		//System.out.println(endWordString);
		//System.out.println(algorithm.exist(startWordString));
		
		if(!algorithm.exist(startWordString)) {
			try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JOptionPane.showMessageDialog(jFrame,
				    "Kata awal tidak ada!",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch (Exception ee) {
				// TODO: handle exception
			}
			return;
		}
		
		if(!algorithm.exist(endWordString)) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JOptionPane.showMessageDialog(jFrame,
					    "Kata akhir tidak ada!",
					    "ERROR",
					    JOptionPane.ERROR_MESSAGE);
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				}catch (Exception ee) {
					// TODO: handle exception
				}
				return;
		}
		
		Pair<ArrayList<String>, Pair<Integer, Integer>> resultCalc;
		if(ucsButton.isSelected()) {
			resultCalc = algorithm.UCS(startWordString, endWordString);
		}else if(astarRadioButton.isSelected()) {
			resultCalc = algorithm.Astar(startWordString, endWordString);
		}else {
			resultCalc = algorithm.GBFS(startWordString, endWordString);
		}
		List<String> resultList = resultCalc.first.reversed();
		new ResultDialog(jFrame, resultList, resultCalc.second.first, resultCalc.second.second);
	}
}
