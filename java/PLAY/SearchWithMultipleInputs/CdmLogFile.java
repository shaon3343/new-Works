package models;
/*
 * Author: SHAON
 * 
 */
import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import FileUtils.Constants;
import FileUtils.TestWithTextFile;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;






import com.avaje.ebean.SqlUpdate;

import play.db.ebean.Model;

@Entity
@Table(name="cdm_log")

public class CdmLogFile extends Model {
	
	@Id
	public Long id;
	
	public String sl_no; 
    public String account_number;
    public String original_account_number;
	public Date create_date;
	public Date create_time;
	public String customer_name; 
	public String amount;
	public String original_amount;
	public String pay_mode;
	public String original_pay_mode;
	public String envelop_number;
	public Long payment_mode;
	public String reference_id;
	public Long payment_desc; 
/*	@Column(name="cdm_id")*/
	public Long cdm_id;
	public Date fetch_date;
	public String fetch_mode;
	

	public String remarks;
	public String modifyRemarks;
	//public String modifyRemarksEdit;
	
	@Column(name="MODIFY_DATE")
	public Date modifyDate;
	
	
	public Integer payment_status;
	
	public String status;

	public Long maker_id;
	public Long checker_id; 

	public Long cdm_area;
	public String particular;
	public String account_type; 
	//public boolean is_select;
	
	public static boolean show=true;
	
	public static Finder<Long,CdmLogFile> finder = new Finder<Long,CdmLogFile>(Long.class,CdmLogFile.class);
	
	public static List<CdmLogFile> getAllLogFiles(Long cdmId){
		
		
		List<CdmLogFile> allLogs = finder.all();
	//	List<CdmLogFile> allLogs = finder.where().eq("cdm_id",cdmId).findList();
		return allLogs;
	}
	
	public static List<CdmLogFile> getAllLogs(){
		List<CdmLogFile> allLogs = finder.all();
	//	List<CdmLogFile> allLogs = finder.where().eq("cdm_id",cdmId).findList();
		return allLogs;
	}
	
	public static List<CdmLogFile> getAllMakersLogFiles(Long cdmId){
		
		//List<CdmLogFile> allLogs = finder.where().eq("payment_status",1).eq("cdm_id", cdmId).findList();
		List<CdmLogFile> allLogs = finder.where().or(Expr.eq("payment_status", 1), Expr.eq("payment_status", 3)).eq("cdm_id", cdmId).orderBy("payment_status desc").findList();
	//	List<CdmLogFile> allLogs = finder.where().eq("cdm_id", cdmId).orderBy("payment_status desc").findList();
		return allLogs;
	}
	public static List<CdmLogFile> getAllCheckersLogFiles(Long cdmId){
		
		//List<CdmLogFile> allLogs = finder.where().eq("payment_status", 2).eq("cdm_id",cdmId).findList();
		//List<CdmLogFile> allLogs = finder.where().eq("payment_status",2).eq("payment_status",6).eq("payment_status",3).eq("cdm_id",cdmId).findList();
		List<CdmLogFile> allLogs = finder.where().or(Expr.eq("payment_status",2), Expr.eq("payment_status",6)).eq("cdm_id",cdmId).findList();
	
		return allLogs;
	} 
	 
	/*public static List<CdmLogFile> getAllModified(Long cdmId){
		
		List<CdmLogFile> allLogs = finder.where().eq("payment_status", 3).eq("cdm_id",cdmId).findList();
		return allLogs;
	}*/
	
	public static Integer getTotalModified(Long area){
		
		int allLogs = finder.where().eq("payment_status", 3).eq("cdm_area",area).findRowCount();
		return allLogs;
	}
	public static Integer getTotalDiscardLogs(Long area){
		
		int allLogs = finder.where().eq("payment_status", 9).eq("cdm_area",area).findRowCount();
		return allLogs;
	}
	public static List<CdmLogFile> getAllModifiedLogByArea(Long area){
		
		List<CdmLogFile> allLogs = finder.where().eq("payment_status", 3).eq("cdm_area",area).findList();
		return allLogs;
	}
	public static List<CdmLogFile> getAllDiscardLogsByArea(Long area){
		
		List<CdmLogFile> allLogs = finder.where().eq("payment_status", 9).eq("cdm_area",area).findList();
		return allLogs;
	}
	public static List<CdmLogFile> discardLogByChecker(Long area){
		
		List<CdmLogFile> allLogs = finder.where().eq("payment_status", 9).eq("cdm_area",area).findList();
		CdmLogFile.updateDiscardLog(allLogs);	
		return allLogs;
	}
	
	public static void updateDiscardLog(List<CdmLogFile> abcd){
		Date dt=new Date();
		for(int i=0; i<abcd.size();i++){
			abcd.get(i).payment_status=10;
			abcd.get(i).modifyDate=dt;
			abcd.get(i).update();
		}
	}
	public static List<CdmLogFile> discardLogBackToMaker(Long area){
		List<CdmLogFile> allLogs = finder.where().eq("payment_status", 9).eq("cdm_area",area).findList();
		CdmLogFile.cancelDiscardLog(allLogs);	
		return allLogs;
	}
	
	public static void cancelDiscardLog(List<CdmLogFile> abcd){
		Date dt=new Date();
		for(int i=0; i<abcd.size();i++){
			abcd.get(i).payment_status=11;
			abcd.get(i).modifyDate=dt;
			abcd.get(i).update();
		}
	}
		
	public static boolean findUniqueCdmLog(CdmLogFile cdmlog){  // if CDM LOG cf does not exist in DB then it returns TRUE:
		List<CdmLogFile> loglist = finder.where().eq("sl_no",cdmlog.sl_no).eq("account_number",cdmlog.account_number).eq("envelop_number", cdmlog.envelop_number).eq("amount", cdmlog.amount).eq("cdm_id", cdmlog.cdm_id).eq("create_time",cdmlog.create_time).findList();
		if(loglist==null || loglist.isEmpty()) 
			return true;
		else
			return false;
	} 
	
	public static void updateLog(Long ID){
		CdmLogFile cdmlg=new CdmLogFile();

		Date curDate =new Date();
		cdmlg.modifyDate=curDate;
		cdmlg.payment_status=9;
		cdmlg.update(ID);
	}
	
	public static void checkerDiscard(Long ID){
		CdmLogFile cdmlg=new CdmLogFile();

		Date curDate =new Date();
		cdmlg.modifyDate=curDate;
		cdmlg.payment_status=10;
		cdmlg.update(ID);
	}
	public static void sendToMaker(Long ID){
		CdmLogFile cdmlg=new CdmLogFile();
		System.out.println("\n+++++====================CANCEL_DISCARDS_LOGS===");
		Date curDate =new Date(); 
		cdmlg.modifyDate=curDate;
		cdmlg.payment_status=11;
		cdmlg.update(ID);
		
	}
	public static CdmLogFile findbyid(Long id){ 
		
		CdmLogFile log = CdmLogFile.finder.byId(id);  
		return log;
	}
	
	public static Long  saveLog(CdmLogFile log){
		 
//		System.out.println("(()JJDKJFLJDLDJFLJD"+log.SL_NO);
		
		log.save();
		
	List<CdmLogFile> logfile=CdmLogFile.findByCdmId(log.cdm_id);
	//	return log.id;
		return logfile.get(0).id;
	}
	//Tofazzal
	public static void CheckerModifyRemarks(CdmLogFile cdml, String mrmk, int pStatus){
		//cdml.payment_status=3;
		cdml.payment_status=pStatus;
		cdml.modifyRemarks=mrmk;
		cdml.update(cdml.id);
	}
/*	public static void MakerModifyRemarks(CdmLogFile cdml, String custmrName, String account, String envelopnumber, String amount, String paymode, String desc, String paymentMode,String cdmId,String refId, Date createTime  ,String mrmk){
		cdml.customer_name=custmrName;
		cdml.envelop_number=envelopnumber;
		cdml.amount=amount;
		cdml.pay_mode=paymode;
		cdml.payment_mode=paymentMode;
		cdml.payment_desc=desc;
		cdml.cdm_id=cdmId;
		cdml.reference_id=refId;
		cdml.create_time=createTime;
		cdml.payment_status=2;
		cdml.modifyRemarks=mrmk;
		cdml.update(cdml.id);
	}*/
	public static void MakerModifyRemarks(CdmLogFile cdml, String mrmk){
		cdml.payment_status=2;
		cdml.modifyRemarks=mrmk;
		cdml.update(cdml.id);
	} 
	
	/*public static void MakerModifyRemarks(CdmLogFile cdml, int pStatus){
		cdml.payment_status=pStatus;
		cdml.update(cdml.id);
	} */
	
	public static void VerifyLog(CdmLogFile cdml , String mrmk){
		Date curDate =new Date();
		cdml.payment_status=4;
		cdml.modifyRemarks=mrmk;
		cdml.modifyDate=curDate;
		cdml.update(cdml.id);
	}
	
	public static CdmLogFile FindBySLNo(String slno){
		
		slno = slno.replaceFirst("^0+(?!$)", "");
		
		return finder.where().eq("SL_NO", slno).findUnique();
	}
	public static boolean findByReferenceId(String ref){
		CdmLogFile logfile = finder.where().eq("reference_id", ref).findUnique();
		if(logfile!=null)
				return true;
		return false;
	}
	public static List<CdmLogFile> findByCdmSL(String SLno){
		List<CdmLogFile> log = finder.where().eq("sl_no",SLno).findList();
		return log;
	}
	
	public static CdmLogFile findByCdmID(Long cdmid){
		try {
			CdmLogFile log = finder.where().eq("cdm_id",cdmid).findUnique();
			return log;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
	public static List<CdmLogFile> findByCdmId(Long cdmid){
		List<CdmLogFile> log = finder.where().eq("cdm_id",cdmid).findList();
		return log;
	}
	
	public static List<CdmLogFile> searchLogFiles( Map<String,String[]> data,String userType ){
		String cdm_id,account_number="",pay_mode,ref_id;
		String account_name="",envelop_number="";
		Integer payment_status;
		String from_date ,to_date , amount_min,amount_max;
		
		Date to=new Date(),from = new Date();
		
		/*from.setHours(0);
		from.setMinutes(0);
		from.setSeconds(0);
		*/
	//	System.out.println("((((((((((((((((((((((((((((((((((((((((((((((((((((((%%%%%%%%%%%%%%%%%% TO DATE"+to.toString());
		
		  List<CdmLogFile> logs = new ArrayList<CdmLogFile>();
		
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME);
		
		
		 ExpressionList<CdmLogFile> query = finder.where();
		 
		 try{
 	 
		//	 TestWithTextFile testing = new TestWithTextFile("TESTING.TXT");
		if(!data.get("account_name")[0].isEmpty())
		{
			account_name = data.get("account_name")[0];
			query = query.eq("CUSTOMER_NAME",account_name);
		}
		if(!data.get("cdmid")[0].isEmpty())
		{
			cdm_id = data.get("cdmid")[0];
			query = query.eq("CDM_ID",cdm_id); 
			
		}
		if(!data.get("account_number")[0].isEmpty())
		{
			account_number = data.get("account_number")[0];
			query = query.eq("ACCOUNT_NUMBER",account_number);
		}
		if(!data.get("envelop_number")[0].isEmpty())
		{
			envelop_number = data.get("envelop_number")[0];
			query = query.eq("envelop_number",envelop_number);
		}
		if(!data.get("pay_mode")[0].isEmpty())
		{
			pay_mode = data.get("pay_mode")[0];
			
			query = query.eq("pay_mode",pay_mode);
		}
		if(!data.get("payment_status")[0].isEmpty())
		{
			payment_status = Integer.parseInt(data.get("payment_status")[0]);
			query = query.eq("payment_status",payment_status);
		}
		if(!data.get("ref_id")[0].isEmpty())
		{
			ref_id = data.get("ref_id")[0];
			query = query.eq("reference_id",ref_id);
		}
		
		if(!data.get("amount_min")[0].isEmpty())
		{
			amount_min = data.get("amount_min")[0];
			
			
			if(!data.get("amount_max")[0].isEmpty())
			{	
				amount_max = data.get("amount_max")[0];
				query = query.between("amount", amount_min, amount_max);
			}
			else
				query = query.between("amount",amount_min,"99999999");
		
		}
		
		else if(!data.get("amount_max")[0].isEmpty())
		{
			
			amount_max = data.get("amount_max")[0];
			if(!data.get("amount_min")[0].isEmpty())
			{
				amount_min = data.get("amount_min")[0];
				query = query.between("amount", amount_min, amount_max);
			
			}
			else
				query = query.between("amount","0",amount_max);
		}
		
		if(!data.get("to_date")[0].isEmpty())
		{
			
			to_date = data.get("to_date")[0];
			to_date=to_date+":59.0";
			from=new Date();
			if(!data.get("from_date")[0].isEmpty())
			{
				from_date = data.get("from_date")[0];
				
				from_date=from_date+":00.0";
				
		//		testing.write("###TEST TO DATE:########"+to_date+"#######$$$$$$$$$$ TEXT FROM DATE: ########"+from_date);
				try {
					from = formatter.parse(from_date);
					
				} catch (ParseException e) {
					System.out.println("EXCEPTION IN DATE FORMATTING "+e);
					e.printStackTrace();
				}
			}
			
			
			try { 
				
				to=formatter.parse(to_date); 
				query = query.between("CREATE_TIME", from,to);
				
			} catch (ParseException e) {
				
				System.out.println("EXCEPTION IN DATE FORMATTING "+e);
				e.printStackTrace();
			}
		
		}
		else if(data.get("to_date")[0].isEmpty() && data.get("from_date")[0].isEmpty())
		{
			String fr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			from = new SimpleDateFormat("yyyy-MM-dd").parse(fr);
			
			query = query.between("CREATE_TIME", from,to);
		}
		if(!data.get("cdm_area")[0].isEmpty())
		{
			Long cdmArea = Long.parseLong(data.get("cdm_area")[0]);
			
			List<Cdmregis> cdmList = Cdmregis.findCdmIdbyAreaId(cdmArea);
			List<Long> array = new ArrayList<Long>();
			for(int i=0;i<cdmList.size();i++)
			{
				array.add(cdmList.get(i).id);
				
				//query = Expr.or(Expr.eq("CDM_ID",cdmList.get(i).id));
				//query = query.raw(" or CDM_ID="+cdmList.get(i).id+" ");
			}
			query = query.in("CDM_ID",array);
			query = query.eq("cdm_area", cdmArea);
		}
		else{
			Long cdmArea = Area.findAreaPrimaryIdByUserId(Long.parseLong( data.get("userid")[0]));
			List<Cdmregis> cdmList = Cdmregis.findCdmIdbyAreaId(cdmArea);
			List<Long> array = new ArrayList<Long>();
			for(int i=0;i<cdmList.size();i++)
			{
				//query = query.or(Expr.eq("CDM_ID",cdmList.get(i).id),Expr.eq("CDM_ID",cdmList.get(i+1).id));
				//query = query.raw(" or CDM_ID="+cdmList.get(i).id+" ");
				array.add(cdmList.get(i).id);
			}
			query = query.in("CDM_ID",array);  
			query = query.eq("cdm_area", cdmArea);
		}
		if(userType.equals("2") && data.get("payment_status")[0].isEmpty())
		{
			//query = query.eq("payment_status",1);
			List<Long> payStatus = new ArrayList<Long>();
			payStatus.add(1L);
			payStatus.add(3L);
			payStatus.add(6L); //TRANSACTION ERROR (AFTER UPLOADING AND PARSING TTUM REPORT .RPT FILE 
			payStatus.add(11L);
			query = query.in("payment_status",payStatus);
			//query = query.or(Expr.eq("payment_status", 1), Expr.eq("payment_status", 3));
		}
		else if(userType.equals("3") && data.get("payment_status")[0].isEmpty())
		{
			//query = query.eq("payment_status",2);
			
			List<Long> payStatus = new ArrayList<Long>();
			payStatus.add(2L);
		//	payStatus.add(6L); //TRANSACTION ERROR (AFTER UPLOADING AND PARSING TTUM REPORT .RPT FILE 
			payStatus.add(9L);
		//	payStatus.add(10L);
			
		//	query = query.or(Expr.eq("payment_status", 2), Expr.eq("payment_status", 6));
			query = query.in("payment_status",payStatus);
			
		}
		
	//	query = query.eq("payment_status", arg1)
		
		/*List<CdmLogFile> logs = finder.where().or(Expr.eq("cdm_id",cdm_id),Expr.eq("customer_name",account_name))
    			.or(Expr.eq("account_number",account_number),Expr.eq("envelop_number",envelop_number))
    			.or(Expr.eq("reference_id",ref_id),Expr.eq("pay_mode",pay_mode)).findList();*/
		System.out.println("-----------------------------------------------------------------------------");
    	logs = query.findList();
   // 	testing.closeFile();
    	System.out.println("-----------------------------------------------------------------------------");
		return logs;
		 }catch(Exception e){
			 e.printStackTrace();
			 return logs;
		 }
	}
	/*@Override
	public String toString() {
		return "CdmLogFile [id=" + id + ", sl_no=" + sl_no
				+ ", account_number=" + account_number + ", create_date="
				+ create_date + ", create_time=" + create_time
				+ ", customer_name=" + customer_name + ", amount=" + amount
				+ ", pay_mode=" + pay_mode + ", envelop_number="
				+ envelop_number + ", payment_mode=" + payment_mode
				+ ", reference_id=" + reference_id + ", payment_desc="
				+ payment_desc + ", cdm_id=" + cdm_id + "]";
	}*/
	public static void setFinacleId(String finacleId , Long id) {
		
		String query = "UPDATE CDM_LOG SET CHECKER_FINACLE_ID='"+finacleId+"' WHERE id='"+id+"'";
		System.out.println("Query - " + query);
		SqlUpdate update = Ebean.createSqlUpdate(query);
		update.execute();
		
	}
	public static List<CdmLogFile> getCheckerApproved(Long userId, List<Long> cdmIdList) {
		
	//	List<CdmLogFile> cdmLogList = finder.where().eq("checker_id", userId).in("cdm_id", cdmIdList).eq("payment_status",4).findList();
		List<CdmLogFile> cdmLogList = finder.where().eq("maker_id", userId).in("cdm_id", cdmIdList).eq("payment_status",4).findList();
		return cdmLogList;
	}
	public static void setDownloadedTTUM(List<CdmLogFile> cdmLogList,boolean isdownloaded) {
		for(int i=0;i<cdmLogList.size();i++){
			CdmLogFile loglist = cdmLogList.get(i);
			if(isdownloaded)
				loglist.payment_status=5;
			else
				loglist.payment_status=4;
			loglist.update();
		}
	}

	public static void setFileNameAndSeq(String fileName, int i,Long id) {
		/*String query = "UPDATE CDM_LOG SET REMARKS='"+fileName+"', MODIFY_REMARKS='"+i+"' WHERE id='"+id+"' ";
		System.out.println("Query - " + query);
		SqlUpdate update = Ebean.createSqlUpdate(query);
		update.execute();*/
		
		CdmLogFile log = finder.byId(id);
		log.payment_status = 5;
		log.remarks=fileName;
		log.modifyRemarks=""+i;
		
		log.update();
	}
	public static List<CdmLogFile> getErrorTran(String[] fileNameAndSeq) {
		
		String fileName = fileNameAndSeq[0].substring(0, fileNameAndSeq[0].lastIndexOf(".TXT")+4);
		fileNameAndSeq[1].replaceAll("\n","");
		fileNameAndSeq[2].replaceAll("\n","");
		List<CdmLogFile> cdmLogFile = finder.where().eq("PAYMENT_STATUS",5).eq("REMARKS",fileName).eq("MODIFY_REMARKS",fileNameAndSeq[1]).findList();
		
		return cdmLogFile;
	}
	public static void updateErrorTran(Long id, String errorString) {
		CdmLogFile log = finder.byId(id);
		log.payment_status=6;
		log.remarks=errorString;
		log.modifyRemarks=errorString;
		log.update();
		
	}
	
/*	public static void declineCDMLog(Long id) {
		CdmLogFile cdm = findbyid(id);
		Date currentDate = Calendar.getInstance().getTime();
		if(cdm.modifyRemarks.isEmpty()){
			return;
		}
		cdm.modifyDate=currentDate;
    	
		cdm.payment_status=7;
		cdm.update();
	}*/
	
	public static void declineCDMLog(CdmLogFile cdm, String remrks) {
		
		Date currentDate = Calendar.getInstance().getTime();
		/*if(cdm.modifyRemarks.isEmpty()){
			return;
		}*/
		
		CdmLogFile cdmLog = CdmLogFile.findbyid(cdm.id);
		cdmLog.modifyRemarks = remrks;;
		cdmLog.modifyDate=currentDate;
    	
		cdmLog.payment_status=7;
		cdmLog.update();
	}
	
	public static List<CdmLogFile> findByCdmArea(Long cdmArea) {
		List<CdmLogFile> logList = finder.where().eq("cdm_area",cdmArea).findList();
		return logList;
	}
	public static boolean findbyCreateDate(Long cdmID,Date time1,Date time2) {
		List<CdmLogFile> logList = finder.where().eq("cdm_id", cdmID).between("create_time",time1,time2).findList();
		if(logList==null)
			return false;
		else if(logList.isEmpty())
			return false;
		return true;
		
	}

}
