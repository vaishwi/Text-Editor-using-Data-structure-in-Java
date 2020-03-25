import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.*;  
import java.util.Date;  
import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;  
import javax.swing.event.*;  
  

class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
{
	public MyHighlightPainter(Color color) {
		super(color);
	}
	
	
	}

public class Notepad {
	
	
	public void removeHighlights(JTextComponent textComp) {
		 
		Highlighter hilite = textComp.getHighlighter();
		Highlighter.Highlight[] hilites = hilite.getHighlights();
		
		for(int i=0 ; i<hilites.length ; i++) {
			
			if(hilites[i].getPainter() instanceof MyHighlightPainter) {
				hilite.removeHighlight(hilites[i]);
			}
		}
 	}
	
	Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.cyan);
	
	public void highligh(JTextComponent textComp ,String pattern) {
		removeHighlights(textComp);
		
		try {
			
			Highlighter hilite = textComp.getHighlighter();
			
			Document doc = textComp.getDocument();
			String text = doc.getText(0, doc.getLength());
			int pos=0;
			int cnt=0;
			
			while((pos=text.toUpperCase().indexOf(pattern.toUpperCase() , pos)) >=0) {
				
				System.out.print("YES");
				hilite.addHighlight(pos, pos+pattern.length() , myHighlightPainter );
				pos+=pattern.length();
				cnt++;
			}
			if(cnt==0){
				
				JFrame frameF = new JFrame();
				JButton btnClose = new JButton("OK");
				frameF.setBounds(100, 100, 450, 300);
				frameF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frameF.getContentPane().setLayout(null);
				frameF.setVisible(true);
				
				//frameF.add(btnClose);
				
				
				btnClose.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						frameF.setVisible(false);
					}
				});
				btnClose.setBounds(181, 143, 89, 23);
				frameF.getContentPane().add(btnClose);
				
				JTextPane txtpnTextNotFound = new JTextPane();
				txtpnTextNotFound.setForeground(Color.RED);
				txtpnTextNotFound.setBackground(Color.WHITE);
				txtpnTextNotFound.setFont(new Font("Tahoma", Font.BOLD, 14));
				txtpnTextNotFound.setText("! Text Not Found");
				txtpnTextNotFound.setBounds(165, 76, 124, 20);
				frameF.getContentPane().add(txtpnTextNotFound);
				
				
			}
			
		}catch(Exception e) {
			
		}
	}

	Stack undoStack = new Stack();
	private JFrame frame;
	String filename ="";
	String fileName="";
	String finalText="";
	public JTextArea textArea = new JTextArea();
	String selectedText;
	String saveText="";
	static int cnt=0 , cntAction=0;
	
	JColorChooser bcolorChooser=null;  
	JColorChooser fcolorChooser=null;  
	JDialog backgroundDialog=null;  
	JDialog foregroundDialog=null;  
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Notepad window = new Notepad();
					window.frame.setVisible(true);
					window.frame.setTitle("SEAS_NOTEPAD");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Notepad() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setBounds(100, 100, 450, 300);
		frame.setBounds(0, 0, 450, 300);
		//frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menubarFile = new JMenu("File");
		JMenuItem newFile = new JMenuItem("New");
		JMenuItem openFile = new JMenuItem("Open");
		JMenuItem saveFile = new JMenuItem("Save");
		JMenuItem exitNotepad = new JMenuItem("Exit");
		JMenu mnEdit = new JMenu("Edit");
		JMenuItem cutText = new JMenuItem("Cut");
		JMenuItem copyText = new JMenuItem("Copy");
		JMenuItem pasteText = new JMenuItem("Paste");
		JMenuItem selectAllText = new JMenuItem("Select All");
		JMenuItem findText = new JMenuItem("Find");
	
		
		menuBar.add(menubarFile);
		menubarFile.add(newFile);
		menubarFile.add(openFile);
		menubarFile.add(saveFile);
		
		JMenuItem menuSaveAs = new JMenuItem("Save As");
		menubarFile.add(menuSaveAs);
		menubarFile.add(exitNotepad);
		menuBar.add(mnEdit);
		
		JMenuItem menuUndo = new JMenuItem("Undo");
		mnEdit.add(menuUndo);
		mnEdit.add(cutText);
		mnEdit.add(copyText);
		mnEdit.add(pasteText);
		
		JMenuItem menuDelete = new JMenuItem("Delete");
		mnEdit.add(menuDelete);
		mnEdit.add(selectAllText);
		mnEdit.add(findText);
		
		JMenu menuFormat = new JMenu("Format");
		menuBar.add(menuFormat);
		
		JMenuItem menuWordWrap = new JMenuItem("Word Wrap");
		menuWordWrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					
				//JCheckBoxMenuItem temp=(JCheckBoxMenuItem)ev.getSource();  
				textArea.setLineWrap(true);
			}
		});
		menuFormat.add(menuWordWrap);
		
		JMenuItem menuFont = new JMenuItem("Font");
		menuFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fontDialog==null)  
				    fontDialog=new FontChooser(textArea.getFont());  
				  
				if(fontDialog.showDialog(frame,"Choose a font"))  
				    textArea.setFont(fontDialog.createFont());  
			}
		});
		menuFormat.add(menuFont);
		
		JMenuItem menuForegraound = new JMenuItem("Foreground");
		menuForegraound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showForegroundColorDialog(); 
			}
		});
		menuFormat.add(menuForegraound);
		
		JMenuItem menuBackground = new JMenuItem("Background");
		menuBackground.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBackgroundColorDialog();
			}
		});
		menuFormat.add(menuBackground);
		
		JMenu menuView = new JMenu("View");
		menuBar.add(menuView);
		
		JMenuItem menuStatusBar = new JMenuItem("Status Bar");
		menuStatusBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBoxMenuItem temp=(JCheckBoxMenuItem)ev.getSource();  
				statusBar.setVisible(temp.isSelected());  
			}
		});
		menuView.add(menuStatusBar);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				removeHighlights(textArea);
			}
		});
		
		//JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		
		//textArea.add(scroll);
		mnEdit.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				System.out.println("undo");
				if(undoStack.isEmpty()) {
					menuUndo.setEnabled(false);
				}
				else
					menuUndo.setEnabled(true);
				
				if(textArea.getSelectedText() == null) {
					copyText.setEnabled(false);
					cutText.setEnabled(false);
					menuDelete.setEnabled(false);
					
				}
				else {
					copyText.setEnabled(true);
					cutText.setEnabled(true);
					menuDelete.setEnabled(true);
					
				}
				if(selectedText == null)
					pasteText.setEnabled(false);
				else
					pasteText.setEnabled(true);
			}
		});
		
		
		findText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFrame frameFind = new JFrame();
				frameFind.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frameFind.setBounds(100, 100, 450, 300);
				
				frameFind.getContentPane().setLayout(null);
				JButton btnFind  = new JButton("Find");
				JTextField txtFind = new JTextField();
				frameFind.setVisible(true);
				frameFind.getContentPane().add(txtFind);
				frameFind.getContentPane().add(btnFind);
				txtFind.setBounds(72, 87, 284, 20);
				btnFind.setBounds(72, 143, 89, 23);
				txtFind.setColumns(10);
				frameFind.setTitle("Find Text");
				
				JButton btnClose = new JButton("Close");
				btnClose.setBounds(267, 143, 89, 23);
				frameFind.getContentPane().add(btnClose);
				
				btnFind.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					
						highligh(textArea ,txtFind.getText());
					}
					
				});
				
				
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					
						frameFind.setVisible(false);
					}
					
				});
				

			}
		});
		
		
		
		
		String text="Untitled-SEAS_NOTEPAD";
		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				frame.setTitle(text);
			}
		});
		
		
		
		//String filename ="";
		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				FileDialog fileDialog = new FileDialog(frame, "Open File", FileDialog.LOAD);
				fileDialog.setVisible(true);
				
				if(fileDialog.getFile() !=null) {
					filename = fileDialog.getDirectory() + fileDialog.getFile();
					frame.setTitle(fileDialog.getFile());	
				}
				
				try{
					
					BufferedReader reader = new BufferedReader(new FileReader(filename));
					StringBuilder sb = new StringBuilder();
					
					String line = null;
					
					while((line = reader.readLine()) !=null) {
						
						sb.append(line +"\n");
						
					}
					textArea.setText(sb.toString());
					reader.close();
					
				}catch(IOException e1) {
					System.out.println("File not found");
				}
				
			}
		});
		
		
		
		saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cnt=saveFile(cnt);
			}
		});
		
		menuSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cnt = saveFile(0);
			}
		});
		
		
		
		
		exitNotepad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		
		
		
		
		menuUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!undoStack.isEmpty()) {
					String text = (String) undoStack.pop();
					textArea.setText(text);
				} 
			}
		});
		
		
		
		cutText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoPart();
				selectedText = textArea.getSelectedText();
				finalText = cut();
				textArea.setText(finalText);
				
			}
		});
		
		
		
		copyText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textArea.getSelectedText() != null)
					selectedText = textArea.getSelectedText();
			}
		});
		
		
		
		pasteText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undoPart();
				String text = textArea.getText();
				int caretPosition = textArea.getCaretPosition();
				
				String start = text.substring(0, caretPosition);
				String last = text.substring(caretPosition);
				
				
				String myText = start+selectedText+last;
				
				textArea.setText(myText);
				
			}
		});
		
		
		menuDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undoPart();
				String text = textArea.getText();
				int selectStart = textArea.getSelectionStart();
				
				String start = text.substring(0, selectStart);
				String last = text.substring(selectStart+textArea.getSelectedText().length());
				
				String myText = start+last;
				
				textArea.setText(myText);
			}
		});
		
		
		selectAllText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				highligh(textArea ,textArea.getText());
			}
		});
		
		
		
		
		
	}
	

	public LinkedList getTextList(String text) {
		
		
		LinkedList list = new LinkedList();
		String[] splited = text.split("\\s+");
		
		for(int i=0 ; i<splited.length ; i++) {
			
			list.add(splited[i]);
		}
		
		return list;
		
	}
	
	public String cut() {
		
		String finalText="";
		String text = textArea.getText();
		LinkedList list = getTextList(text);
		
		finalText = text.substring(0, textArea.getSelectionStart())+text.substring(textArea.getSelectionEnd());
		LinkedList ft  = getTextList(finalText);
		return finalText;
	}
	/*private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}*/
	
	
	public void undoPart() {
		undoStack.push(textArea.getText());
	}
	
	public int saveFile(int cnt) {
		FileDialog fileDialog = null;
		if( cnt == 0) {
			fileDialog = new FileDialog(frame, "Save File", FileDialog.SAVE);
			fileDialog.setVisible(true);
			cnt++;			// check for file already save or nor?
			
			if(fileDialog.getFile() != null) {
				fileName = fileDialog.getDirectory() + fileDialog.getFile();
				frame.setTitle(fileDialog.getFile());
			}
			try {
				FileWriter fileWriter = new FileWriter(fileName);
				saveText = textArea.getText();
				fileWriter.write(saveText);
				//frame.setTitle(fileDialog.getFile());
				fileWriter.close();
			} catch (Exception e) {
				System.out.println("File Not Found");
			}
		}
		else {
			try {
				FileWriter fileWriter = new FileWriter(fileName);
				saveText = textArea.getText();
				fileWriter.write(saveText);
				//frame.setTitle(fileDialog.getFile());
				fileWriter.close();
			} catch (Exception e) {
				System.out.println("File Not Found");
			}
		}
		  
		return cnt;
	}
	
	void showForegroundColorDialog()  
	{  
	if(fcolorChooser==null)  
	    fcolorChooser=new JColorChooser();  
	if(foregroundDialog==null)  
	    foregroundDialog=JColorChooser.createDialog  
	        (this.frame,  
	        "Set Text color...",  
	        false,  
	        fcolorChooser,  
	        new ActionListener()  
	        {public void actionPerformed(ActionEvent ev){  
	            textArea.setForeground(fcolorChooser.getColor());}},  
	        null);        
	  
	foregroundDialog.setVisible(true);  
	}  
	
	void showBackgroundColorDialog()  
	{  
	if(bcolorChooser==null)  
	    bcolorChooser=new JColorChooser();  
	if(backgroundDialog==null)  
	    backgroundDialog=JColorChooser.createDialog  
	        (this.frame,  
	        "Set Pad color...",  
	        false,  
	        bcolorChooser,  
	        new ActionListener()  
	        {public void actionPerformed(ActionEvent ev){  
	            textArea.setBackground(bcolorChooser.getColor());}},  
	        null);        
	  
	backgroundDialog.setVisible(true);  
	}
}