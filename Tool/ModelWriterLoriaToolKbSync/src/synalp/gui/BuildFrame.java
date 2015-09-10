package synalp.gui;

import java.awt.event.ActionListener;
import java.io.FileInputStream;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class BuildFrame
{
	
	public JButton addButton(JDesktopPane desktopPane, String nameButton, String toolTipText, int x, int y, int width, int height, Object nameClass )
	{
	    JButton bo = new JButton(nameButton);
	    bo.setToolTipText(toolTipText);
	    bo.addActionListener( (ActionListener) nameClass);	       
	    bo.setBounds(x, y , width, height);
	    desktopPane.add(bo);
	    return bo;
	 }
	
	public JTextArea addTextArea(JDesktopPane desktopPane,int x, int y, int width, int height )
	{
		JTextArea textArea = new JTextArea();       
		textArea.setBounds(x, y , width, height);
		desktopPane.add(textArea);
	    return textArea;
	}
	
	public JTextField  addTextField(JDesktopPane desktopPane,int x, int y, int width, int height )
	{
		JTextField textField = new JTextField();       
		textField.setBounds(x, y , width, height);
		desktopPane.add(textField);
	    return textField;
	}
	
	public JLabel addLabel(JDesktopPane desktopPane, String labelContenu, int x, int y, int width, int height)
	{
		JLabel label = new JLabel(labelContenu);	       
		label.setBounds(x, y , width, height);
		desktopPane.add(label);
        return label;
	 }
	
	public JScrollPane addScrollPane(JDesktopPane desktopPane, int x, int y, int width, int height)
	{
		JScrollPane scrollPane = new JScrollPane();
 	   	scrollPane.setBounds(x, y , width, height);
 	   	desktopPane.add(scrollPane);
        return scrollPane;
	 }

	
	public JFrame createFrame(String name, int width, int lenght)
	{
		JFrame f = new JFrame ();
		f.setTitle(name);
		f.setSize(width,lenght);
		return f;
	}
	
	public JTextArea readFileCheck(JTextArea textArea, String fichier)
	{
		try
		{
			FileInputStream fis = new FileInputStream(fichier);
			int n;
			while((n = fis.available())>0)
			{
				byte[] b = new byte[n];
		        int result = fis.read(b);
		        if(result == -1)
		        	break;
		         String s = new String(b);
		         textArea.setText(s);
			}            
		}
		catch(Exception err)
		{
		    System.out.println("LoadText: " + err.getCause());
		}

		return textArea;		
	}	
}
