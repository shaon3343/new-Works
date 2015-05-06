package FileUtils;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;







import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;






import models.Area;
import models.CdmLogFile;
import models.Cdmregis;
import models.PaymentDescription;
import models.PaymentType;

public class CreateSpreadsheet {
	public static ArrayList<ArrayList<String>> decodeExcelFile(String fileName,String cdmId,String log_stored_loc)
	{
		
		
		String password="123456";
		ArrayList<ArrayList<String>> contents = new ArrayList<ArrayList<String>>();
		
		
		
		/*String inputPathFileName = "\\\\10.100.17.120\\JANALAExcel\\encryptedExcel.rar";
		String outputPath = "\\\\10.100.17.120\\ShareFolder\\";*/
		
//		String inputPathFileName = logLoc+"\\encryptedExcel.rar";
		String inputPathFileName = log_stored_loc;
		String outputPath = log_stored_loc;
		
	//	String excelFilePath = UnrarFile.doUnRar(inputPathFileName, outputPath);
		/*
		 * 
		 * 
		 * to be Uncommented :
		 */
		
		System.out.println("#############___________############inputPathFileName__________"+inputPathFileName+fileName);
		
		String excelFilePath = UnzipFile.unZipIt(inputPathFileName+fileName, outputPath,cdmId);
		
		if(excelFilePath==null || excelFilePath.equals(""))
			return null;
		excelFilePath=log_stored_loc+excelFilePath;
		
	//	String excelFilePath = log_stored_loc+"33A1009141010transactions.xls";
		
		try{
		
		FileInputStream fileInput;
	
		//	fileInput = new FileInputStream(outputPath+"\\37A0406141414transactions.xls");
		fileInput = new FileInputStream(excelFilePath);
		
		BufferedInputStream bufferInput = new BufferedInputStream(fileInput);  
		
		POIFSFileSystem poiFileSystem;
	
		poiFileSystem = new POIFSFileSystem(bufferInput);
		Biff8EncryptionKey.setCurrentUserPassword(password); 
		
		HSSFWorkbook wb = new HSSFWorkbook(poiFileSystem, true);
		
		
			 
			    Sheet sheet1 = wb.getSheetAt(0);
			    
			    DataFormatter formatter = new DataFormatter(Locale.getDefault());
			    
			    for (Row row : sheet1) {
			    	ArrayList<String> str = new ArrayList<String>();
			        for (Cell cell : row) {
			          /*  CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());*/
			           /* System.out.print(cellRef.formatAsString());
			            System.out.print(" - ");*/
			        	
			        	
			            switch (cell.getCellType()) {
			                case Cell.CELL_TYPE_STRING:
			                	str.add(cell.getRichStringCellValue().getString());
			                 //   System.out.println(cell.getRichStringCellValue().getString());
			                    break;
			                case Cell.CELL_TYPE_NUMERIC:
			                    if (DateUtil.isCellDateFormatted(cell)) {
			                    	str.add(formatter.formatCellValue(cell));
			                    	//str.add(cell.getDateCellValue().toString());
			                    //    System.out.println(formatter.formatCellValue(cell));
			                    } else {
			                    	str.add(String.valueOf((long)cell.getNumericCellValue()));
			                  //      System.out.println(cell.getNumericCellValue());
			                    }
			                    break;
			                case Cell.CELL_TYPE_BOOLEAN:
			                //    System.out.println(cell.getBooleanCellValue());
			                    break;
			                case Cell.CELL_TYPE_FORMULA:
			                //    System.out.println(cell.getCellFormula());
			                    break;
			                default:
			                  //  System.out.println("DEFAULT");
			            }
			        }
			       
			        contents.add(str);
			    }
			
		return contents;
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		
			return contents;
			
		} catch (IOException e) {
			e.printStackTrace();
			
			return contents;
			
		} 
			
}
	
	public static String doSpreadsheet(List<CdmLogFile> loglist) throws IOException{
		
		Workbook wb = new HSSFWorkbook();
	   
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet("CDM LOG FILES");

	    // Create a row and put some cells in it. Rows are 0 based.
	    Row row = sheet.createRow((short)0);
	    
	    
	    CellStyle style = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
	    
        
        ArrayList<String> CDM_LOG_HEADER = GetHeadersFooters.getCdmLogHeaders();
        
        
        for(int i=0;i<CDM_LOG_HEADER.size();i++)
        {
        	Cell cell =row.createCell(i);
    	    cell.setCellStyle(style);
    	    cell.setCellValue(createHelper.createRichTextString(CDM_LOG_HEADER.get(i)));
        }
	  
	    
	    /* END HEADER */
	    int i=0;
	    Double amount =0.0;
	    for(i=0;i<loglist.size();i++){
		    	row = sheet.createRow((short)i+1);
			
		    	row.createCell(0).setCellValue(
			         createHelper.createRichTextString(loglist.get(i).sl_no));		    
			    row.createCell(1).setCellValue(
				         createHelper.createRichTextString(loglist.get(i).customer_name));
	
			    row.createCell(2).setCellValue(
				         createHelper.createRichTextString(loglist.get(i).account_number));
			                      
			    row.createCell(3).setCellValue(
				         createHelper.createRichTextString(loglist.get(i).envelop_number));
			    
			    row.createCell(4).setCellValue(
				         createHelper.createRichTextString(loglist.get(i).pay_mode));
			   
			    String paymentType = PaymentType.getPaymentType(loglist.get(i).payment_mode);
			    row.createCell(5).setCellValue(
				         createHelper.createRichTextString(paymentType));
			    
			    PaymentDescription pDesc = PaymentDescription.findById(loglist.get(i).payment_desc);
			    String payDesc = "";
			    if(pDesc!=null){
			    	payDesc=pDesc.longDesc;
			    }
			    
			    
			    row.createCell(6).setCellValue(
				         createHelper.createRichTextString(payDesc));
			    
			    String cdmArea = Area.findAreaNameByPrimaryId(loglist.get(i).cdm_area);
			    row.createCell(7).setCellValue(
				         createHelper.createRichTextString(cdmArea));
			    
			    String CdmID = Cdmregis.findCdmIdbyPrimaryid(loglist.get(i).cdm_id);
			    
			    	
			    row.createCell(8).setCellValue(
				         createHelper.createRichTextString(CdmID));
			    row.createCell(9).setCellValue(Double.parseDouble(loglist.get(i).amount));
			    amount =amount +Double.parseDouble(loglist.get(i).amount);
			    
	    }
	    row = sheet.createRow((short)i+1);
	    row.createCell(8).setCellValue(
		         createHelper.createRichTextString("TOTAL AMOUNT: "));
	    row.createCell(9).setCellValue(amount);
	    
	    String fileName=Constants.CDM_PDF_EXCEL_FILE_NAME+new Date().getTime();
	    String filePath=Constants.EXCEL_FILE_PATH;
	    FileOutputStream fileOut = new FileOutputStream(filePath+fileName+".xls");
	    wb.write(fileOut);
	    fileOut.close();
	    
		return fileName;
	}
	
	public static String createSpreadsheetGeneric(ArrayList<ArrayList<String>> headerPlusContent,String spreadsheetName) throws IOException{
		
		Workbook wb = new HSSFWorkbook();
	   
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet(spreadsheetName);

	    // Create a row and put some cells in it. Rows are 0 based.
	    Row row = sheet.createRow((short)0);
	    
	    
	    CellStyle style = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
	    
        
        ArrayList<String> header = headerPlusContent.get(0); // first row of this is Header and rest of it are contents 
        
        
        for(int i=0;i<header.size();i++)
        {
        	Cell cell =row.createCell(i);
    	    cell.setCellStyle(style);
    	    cell.setCellValue(createHelper.createRichTextString(header.get(i)));
        }
	  
	    /* END HEADER */
	    ArrayList<String> content =new ArrayList<String>();
	    
	    for(int i=1;i<headerPlusContent.size();i++){
	    	
	    	content = headerPlusContent.get(i);
	    	
	    	
		    row = sheet.createRow((short)i);
			for(int j=0;j<content.size();j++){
				row.createCell(j).setCellValue(createHelper.createRichTextString(content.get(j)));
		    	
		    }
			    
	    }
	    
	   
	    
	    String fileName=spreadsheetName;
	    String filePath=Constants.EXCEL_FILE_PATH;
	    FileOutputStream fileOut = new FileOutputStream(filePath+fileName+".xls");
	    wb.write(fileOut);
	    fileOut.close();
	    
	    
	    
		return fileName;
	}
}
