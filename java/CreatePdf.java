package FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Area;
import models.CdmLogFile;
import models.Cdmregis;
import models.PaymentDescription;
import models.PaymentType;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class CreatePdf {
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
	private static Font fontnorm = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.NORMAL);
	public static String generatePDF(List<CdmLogFile> logs) throws FileNotFoundException, DocumentException{
	
		final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
		
        Document document =new Document(PageSize.A4.rotate());  // landscape mode 
 //       Document document = new Document(PageSize.A4_LANDSCAPE,2,2,2,2);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
	    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
	    
	    String filePath=Constants.PDF_FILE_PATH;
		String fileName=Constants.CDM_LOG_PDF_EXCEL_FILE_NAME+new Date().getTime();
        
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath+fileName+".pdf"));
	    /*HeaderFooter event = new HeaderFooter();
	    writer.setPageEvent(event);*/
       
        document.open();
        Font fontnorm = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.NORMAL);
        Font fontnormbold = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.BOLD);
        Font fontbold = new Font(Font.FontFamily.TIMES_ROMAN,13,Font.BOLD);
        Font fontsmallbold = new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
        
     //.setPageCount(arg0)
        Paragraph pr = new Paragraph("Search Result CDM LOG FILES",fontbold);
        pr.setAlignment(Element.ALIGN_CENTER);
        
        document.add(pr);
        
        pr=new Paragraph("BRAC Bank Limited",fontbold);
        pr.setAlignment(Element.ALIGN_CENTER);
        
        document.add(pr);
        
        String today = dateFormat.format(new Date());
        pr=new Paragraph("Search Date: "+today,fontsmallbold);
        pr.setAlignment(Element.ALIGN_RIGHT);
        document.add(pr);
        document.add( Chunk.NEWLINE );
        
       /* document.add(new Paragraph("Search Criteria:",fontnormbold));
        
        if(!df.cardNumber.isEmpty())
        	 document.add(new Paragraph("Card Number: "+df.cardNumber,fontnorm));
        if(df.trnxnFromDate!=null || df.trnxnToDate!=null)
        	 document.add(new Paragraph("Trxn Date: "
        			 + ((df.trnxnFromDate!=null) ? dateFormat.format(df.trnxnFromDate) : "")
        			 + " - "
        			 +((df.trnxnToDate!=null) ? dateFormat.format(df.trnxnToDate) : "")
        	, fontnorm));
        
        if(df.settleFromDate!=null || df.settleToDate!=null)
        	 document.add(new Paragraph("Settlement Date: "
        			 + ((df.settleFromDate!=null) ? dateFormat.format(df.settleFromDate) : "")
        			 + " - "
        			 +((df.settleToDate!=null) ? dateFormat.format(df.settleToDate) : "")
        	, fontnorm));
        
        //if(df.settleFromDate!=null)
        //	 document.add(new Paragraph("Settlement Date: "+dateFormat.format(df.settleFromDate) + " - "+dateFormat.format(df.settleToDate), fontnorm));
        if(df.trnxnMode==null)
        	 document.add(new Paragraph("Trxn Mode: "+"ALL",fontnorm));
        else
        	 document.add(new Paragraph("Trxn Mode: "+TrnxnMode.get(df.trnxnMode).trnxnModeName,fontnorm));
        document.add(new Paragraph("Trxn Type: "+TrnxnType.get(df.trnxnType).trnxnTypeName,fontnorm));
        
   	 	document.add(new Paragraph("Report: "+"PDF",fontnorm));*/
        
       
        document.add( createTable(logs));
        
        document.close();
		
		
		return fileName;
	}
	
	 public static PdfPTable createTable(List<CdmLogFile> dataList) {
		 
			

		 int tableColumnCount = 10;
			//PdfPTable table = new PdfPTable(tableColumnCount);
			
			PdfPTable table = new PdfPTable(new float[] { 0.30f, 1.4f, 1, 0.6f, 0.6f, 0.8f, 0.7f, 0.8f, 0.5f,0.5f});
	        table.setWidthPercentage(100f);
	        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			// t.setBorderColor(BaseColor.GRAY);
			// t.setPadding(4);
			// t.setSpacing(4);
			// t.setBorderWidth(1);

			java.util.ArrayList<String> tableHeader = GetHeadersFooters.getCdmLogHeaders();
			
			SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
		    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
			
			for (int i = 0; i < tableColumnCount; i++) {
				PdfPCell c1 = new PdfPCell(new Phrase(tableHeader.get(i),smallBold));
				
			//	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c1);
			}

			table.setHeaderRows(1);
			

			for (int i = 0; i < dataList.size(); i++) {
				
				String sl = ""+(i+1);
				table.addCell(new Phrase(sl,fontnorm));
				table.addCell(new Phrase((dataList.get(i).customer_name==null?"":dataList.get(i).customer_name),fontnorm));
				table.addCell(new Phrase((dataList.get(i).account_number==null?"":dataList.get(i).account_number),fontnorm));
				table.addCell(new Phrase("" + (dataList.get(i).envelop_number==null?"":dataList.get(i).envelop_number),fontnorm));
				table.addCell(new Phrase("" + (dataList.get(i).pay_mode==null?"":dataList.get(i).pay_mode),fontnorm));
				
				String paymentType = PaymentType.getPaymentType(dataList.get(i).payment_mode);
				
				table.addCell(new Phrase((paymentType),fontnorm));
				
				PaymentDescription pDesc = PaymentDescription.findById(dataList.get(i).payment_desc);
				String payDesc = "";
				if(pDesc!=null)
					payDesc = pDesc.longDesc;
				
				String cdmArea = Area.findAreaNameByPrimaryId(dataList.get(i).cdm_area);
				table.addCell(new Phrase(payDesc,fontnorm));
				
				table.addCell(new Phrase(cdmArea,fontnorm));
				
				String CdmID = Cdmregis.findCdmIdbyPrimaryid(dataList.get(i).cdm_id);
				
				table.addCell(new Phrase(CdmID,fontnorm));
				table.addCell(new Phrase(dataList.get(i).amount==null?"":dataList.get(i).amount,fontnorm));
				
				
			}
	        return table;
	    }
	
}
