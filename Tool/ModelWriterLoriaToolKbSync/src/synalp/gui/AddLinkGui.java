package synalp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import synalp.synchronization.AddLink;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class AddLinkGui implements ActionListener
{

	private Interface interf ;
	private JTextField word, concept;
	private JFrame f;
	
	public AddLinkGui(Interface interf)
	{
		this.interf=interf;
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		BuildFrame frame= new BuildFrame();
		f = frame.createFrame("Text Element", 500, 200);
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(233,233,233));
			
		frame.addLabel(desktopPane, "Text Element", 20, 30, 130,30);
		word =frame.addTextField(desktopPane, 150, 30, 200, 30) ;
		frame.addLabel(desktopPane, "Model Element", 20, 70, 130,30);
		concept =frame.addTextField(desktopPane, 150, 70, 200, 30) ;
		frame.addButton(desktopPane, "Create a link", "Create a Link ", 150, 120, 150, 30, new AddLink(this, interf));
		JPanel  panel = (JPanel)f.getContentPane();
		panel.add( desktopPane,BorderLayout.CENTER); 
		f.setVisible(true);		
	}

	public JTextField getWord() 
	{
		return word;
	}

	public JTextField getConcept() {
		return concept;
	}

	public JFrame getFrame() {
		return f;
	}

}
