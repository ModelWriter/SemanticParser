package synalp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import synalp.synchronization.CreateLink;
import synalp.synchronization.RDFTriple;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class LinkView  extends JFrame implements ActionListener
{
	Interface interf;
	int type;
	 List <List <String>> links;
	RDFTriple matching;
	
	public LinkView(Interface interf, int type) throws IOException
	{
		this.interf= interf;
		this.type= type;		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try 
		{
			/******Create link between KB and definition for one time*********/	
			if( type ==0)
			{
				CreateLink cl= new CreateLink(interf.getTextFile(), interf.getOntoFile());	
				links=  cl.getMatching();
				matching= new RDFTriple(interf.getTextFile() ,links);
			}
			
			/***** show the result on the Jtextarea *****/		
			BuildFrame frame=  new BuildFrame();
			
			if(type ==0  || type ==1)
			{				
				interf.getScrollLink().getViewport().setView(frame.readFileCheck(interf.getJLink(),RDFTriple.T2MFile));	
			}
			
			else if(type ==2)
			{			
				interf.getScrollLink().getViewport().setView(frame.readFileCheck(interf.getJLink(), RDFTriple.M2TFile));
			}
			
			else if(type ==3)
			{
				interf.getScrollLink().getViewport().setView(frame.readFileCheck(interf.getJLink(), CreateLink.txtLinkFile));
			}
			
			interf.getDesktopPane().validate();	
		} 
		catch (IOException e1) 
		{		
			e1.printStackTrace();
		} 
	}
}
