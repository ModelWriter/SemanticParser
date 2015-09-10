package synalp.commons.input.texts;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Class to read Excel file
 * @author mariem mahfoudh
 *
 */

public class ExcelFile
{

    private String xlsFile;
    
    private   BufferedWriter plainTxtFile ;
    
    
    public  ExcelFile(String xlsFile)
    {
    	this.xlsFile = xlsFile;
    }

    public void setInputFile(String outputFile ) throws IOException
    {
        
        File inputWorkbook = new File(xlsFile);
        
        System.out.println(xlsFile + " gg");
        
        Workbook workbook ;
        
        try
        {
            // Get  Excel workbook
			workbook = Workbook.getWorkbook(inputWorkbook);			
			
           // Get the first sheet
           Sheet sheet =workbook.getSheet(0);            
           
           plainTxtFile = new BufferedWriter(new FileWriter(outputFile));
           
           // get all the rows and just             
            for (int i=1; i< sheet.getRows() ; i++) 
            {
             	Cell cell = sheet.getCell(54,i);
             	//if(!cell.getContents().isEmpty())
             	{
             		plainTxtFile.write(cell.getContents()); 
             		//System.out.println( cell.getContents());
             		plainTxtFile.newLine();  
             	}
            }      
            
            plainTxtFile.close();
        } 
        
        catch (BiffException e)
        {
            e.printStackTrace();
        }       
    }    
}