package synalp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import synalp.changes.Text.RenameTextElement;


/**
 * 
 * @author Mariem Mahfoudh
 *
 */

public class RenameTextGui  extends JFrame implements ActionListener
{
	private Interface interf ;
	private JTextField word, newWord ;
	
	public RenameTextGui(Interface interf)
	{
		this.interf=interf;
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		BuildFrame frame= new BuildFrame();
		JFrame f = frame.createFrame("Rename Text Element", 500, 200);
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(233,233,233));	
		
		frame.addLabel(desktopPane, "Rename text element by", 20, 30, 200,30);
		newWord =frame.addTextField(desktopPane, 220, 30, 250, 30) ;
		frame.addButton(desktopPane, "OK", "renameWord", 220, 90, 60, 30, new RenameTextElement(this, interf));
		JPanel  panel = (JPanel)f.getContentPane();
		panel.add( desktopPane,BorderLayout.CENTER); 
		f.setVisible(true);		
	}

	public JTextField getWord() 
	{
		return word;
	}

	public JTextField getNewWord() {
		return newWord;
	}
	
}
