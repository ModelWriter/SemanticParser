package synalp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import synalp.synchronization.RemoveLink;
import synalp.synchronization.SearchLink;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class SearchLinkGui implements ActionListener
{
	private Interface interf;
	private JTextField txtElt;
	private JDesktopPane desktopPane;
	private JComboBox   comboBox;
	
	public SearchLinkGui (Interface interf)
	{
		this.interf=interf;
	}
	
	public void  actionPerformed(ActionEvent e)
	{
		BuildFrame frame= new BuildFrame();
		JFrame f = frame.createFrame("Search link", 500, 200);
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(233,233,233));
		
		frame.addLabel(desktopPane, "Text Element", 20, 30, 130,30);
		txtElt =frame.addTextField(desktopPane, 140, 30, 200, 30) ;
	
		frame.addButton(desktopPane, "Search link", "search  Link ", 350, 30, 120, 30, new SearchLink(this, interf));
		
		
		
		JPanel  panel = (JPanel)f.getContentPane();
		panel.add( desktopPane,BorderLayout.CENTER); 
		f.setVisible(true);		
	}

	public JTextField getTxtElt() {
		return txtElt;
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public JComboBox getComboBox() {
		return comboBox;
	}
	
	
}
