package authentication;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.driver.OracleTypes;
import dbModel.DcheckerTranList;
import secureData.ConnectionManager;
import utils.PaymentStatus;
import utils.Utility;

public class Transaction {
	public int intID;
	public int distributorId;
	public String distName;
	public String depositSlipNo;
	public int smsId;
	public String instrumentType;
	public String instrumentNo; 
	public Date instrumentDate;
	public String issuingBranch;
	public String drawingBranch;
	public String bankCode;
	
	public Date receivingDate;
	public String amount;
	public String remarks;
	public String paySt;
	
	// pass param 0 for Maker and 1 for Checker , 2 for getting all transaction
	public ArrayList<Transaction> getAllTranbyUser(int makerOrChecker){
		ArrayList<Transaction> tranList = new ArrayList<Transaction>();
		// @ sql server pass param -1 for both to get all transaction
		String spSQL = "EXEC  ncs_getAllTranInfoByUser ?";
		Connection con =null;
		try 
		{
			con = ConnectionManager.getConnection();
			PreparedStatement ps = con.prepareStatement(spSQL);
			ps.setEscapeProcessing(true);
			ps.setInt(1,makerOrChecker); 
			
			ResultSet rs = ps.executeQuery();
			tranList = resultSetToArrayList(rs);
			
			rs.close();
			ps.close();
			con.close();
		//	//System.out.println("####################"+retModules.size());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}
		
		
		return tranList;
	}
	
	private ArrayList<Transaction> resultSetToArrayList(ResultSet rs){
		ArrayList<Transaction> tranList = new ArrayList<Transaction>();
		try {
			while(rs.next())
			{
				Transaction transaction  = new Transaction();
				transaction.intID = rs.getInt("intID");
				transaction.smsId = rs.getInt("SMSID");
				transaction.depositSlipNo = rs.getString("DepSlipNo");
				transaction.distName = rs.getString("DistName");
				transaction.instrumentType = rs.getString("DocType");
				transaction.instrumentNo = rs.getString("DocNumber");
				transaction.instrumentDate = rs.getDate("DocDate");
				transaction.bankCode = rs.getString("BankCode");
				transaction.issuingBranch = rs.getString("IssuingBranch");
				
				transaction.drawingBranch = rs.getString("DrawingBranch");
				transaction.receivingDate = rs.getDate("ReceivingDate");
				transaction.amount = rs.getString("Amount");
				
				transaction.remarks = rs.getString("Remarks");
				transaction.paySt = rs.getString("paymement_stat");
				tranList.add(transaction);
			}
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return tranList;
	}
	
	public ArrayList<Transaction> getAllTran(int paymentStatus,int confirmed){
		ArrayList<Transaction> tranList = new ArrayList<Transaction>();
		// @ sql server pass param -1 for both to get all transaction
		String spSQL = "EXEC  ncs_getAllTranInfo ?, ?";
		Connection con =null;
		try 
		{
			con = ConnectionManager.getConnection();
			PreparedStatement ps = con.prepareStatement(spSQL);
			ps.setEscapeProcessing(true);
			ps.setInt(1,paymentStatus); 
			ps.setInt(2,confirmed);
			
			ResultSet rs = ps.executeQuery();
			
			tranList = resultSetToArrayList(rs);
		/*	while(rs.next())
			{
				Transaction transaction  = new Transaction();
				transaction.intID = rs.getInt("intID");
				transaction.smsId = rs.getInt("SMSID");
				transaction.depositSlipNo = rs.getString("DepSlipNo");
				transaction.distName = rs.getString("DistName");
				transaction.instrumentType = rs.getString("DocType");
				transaction.instrumentNo = rs.getString("DocNumber");
				transaction.instrumentDate = rs.getDate("DocDate");
				transaction.bankCode = rs.getString("BankCode");
				transaction.issuingBranch = rs.getString("IssuingBranch");
				
				transaction.drawingBranch = rs.getString("DrawingBranch");
				transaction.receivingDate = rs.getDate("ReceivingDate");
				transaction.amount = rs.getString("Amount");
				transaction.remarks = rs.getString("Remarks");
				
				
				tranList.add(transaction);
			}*/
			rs.close();
			ps.close();
			con.close();
		//	//System.out.println("####################"+retModules.size());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}
		
		
		return tranList;
	}
	
	public Map<String,String> insert(Map<String, String[]> map) {
		Connection con =null;
		ResultSet rsCheck=null , rsCheck2=null , rsInsert=null;
		PreparedStatement ps=null;
		Map<String,String> retVal = new HashMap<String, String>();
		try {
			String userId =null;
			if(map.get("userid")!=null)
				 userId = map.get("userid")[0].trim();
			String sms="";
			if(map.get("sms")!=null)
				sms = map.get("sms")[0].trim();
			String mobile = "";
			if(map.get("mobile")!=null)
				mobile = map.get("mobile")[0].trim();
			String distCode =  map.get("distCode")[0].trim();
			String depSlipNo=  map.get("depSlipNo")[0].trim();
			String instruType= map.get("instruType")[0].trim();
			String instruNo= map.get("instruNo")[0].trim();
			String instruDate= map.get("instruDate")[0].trim();
			String bankCode= map.get("bankCode")[0].trim();
			String issueBr= map.get("issueBr")[0].trim();
			String drawBr= map.get("drawBr")[0].trim();
			String amount= map.get("amount")[0].trim();
			String rcvDate = map.get("receiveDate")[0].trim();
	//		String rcvDate = receiveDate.split(" ")[0].trim();
	//		String rcvTime = receiveDate.split(" ")[1].trim();
		//	System.out.println("######## BANK CODE BEFORE: "+bankCode);
	//		bankCode = new DataManagement().getBankCodeByName(bankCode);
	//		System.out.println("######## BANK CODE AFTER TRANSACTION INSERT: "+bankCode);
			 /*if(!bankCode.isEmpty())
				 return null;*/
			// Call stored Procedure at SQL server to insert or Update Transaction
			//	String spSQL = "EXEC NCS_InsertTransaction ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
			String spSQL = "EXEC NCS_InsertTransaction ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(spSQL);
			ps.setEscapeProcessing(true);
			
			ps.setString(1,""+depSlipNo);
			ps.setString(2,""+distCode);
			ps.setString(3,""+instruType);
			ps.setString(4,""+instruNo);
			ps.setString(5,""+instruDate);
			ps.setString(6,""+bankCode);
			ps.setString(7,""+issueBr);
			ps.setString(8,""+drawBr);
			ps.setString(9,""+rcvDate);
	//		ps.setString(10,""+rcvTime);
			ps.setString(10,""+amount);
			
			// SME USER SET CONFIRMED TO 1 otherwise 0 
			ps.setString(11,""+"0");
			ps.setString(12,""+userId);
			ps.setString(13,""+sms);
			ps.setString(14, mobile);
			
			
			
			
			//System.out.println("########## DOC TYPE: "+instruType);
			
			
			if(instruType.equals("CASH") || instruType.equals("AT"))
			{
				instruType="AT";
				
				/*String spSQL = "EXEC NCS_InsertTransaction ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
				con = ConnectionManager.getConnection();
				
				
				ps = con.prepareStatement(spSQL);
				
				ps.setEscapeProcessing(true);*/
				String remarks = "CASH TRANSACTION";
				ps.setInt(15,-1); //PO DD FLAG 
				ps.setString(16,""+remarks);
				ps.setString(17,""+intID);
				ps.execute();
				ps.getMoreResults();
				ps.getMoreResults();
				 
			//	 //System.out.println("######@@@@@@ IN CASH TRAN ");
			}
			else
			{
					String sqlCheckExist ="select * from [dbo].[tblNCSTxnInformation] where DepSlipNo ='"+depSlipNo+"' AND (ISNULL(po_dd_flag,0)>=3)"; 
					System.out.println("#########SQL CHECK EXIST ######"+sqlCheckExist);
					
					String sqlPODDPlacement = "SELECT intID, ISNULL(po_dd_flag,0) AS po_dd_flag FROM [dbo].[tblNCSTxnInformation] WHERE DepSlipNo='"+depSlipNo+"' ";
					//System.out.println("######### SQL DD PO PLACEMNT ######"+sqlPODDPlacement);
					Connection connection=ConnectionManager.getConnection();
					Statement s = connection.createStatement();
					rsCheck = s.executeQuery(sqlCheckExist);
					
					if(rsCheck.next()){
						rsCheck.close();
						System.out.println("#### PO DD placement 3 ");
						return null;
					}
					
					rsCheck2 = s.executeQuery(sqlPODDPlacement); 
					int poDDFlag = 1;
					String remarks = "1st Placement";
					int intID=0;
					if(rsCheck2.next())
					{
						System.out.println("########PO DD PLACEMENT#####"+sqlPODDPlacement);
						
						poDDFlag = rsCheck2.getInt("po_dd_flag");
						intID = rsCheck2.getInt("intID");
						poDDFlag++;
						if(poDDFlag ==2)
							remarks = "2nd Placement";
						if(poDDFlag ==3)
							remarks = "3rd Placement";
						rsCheck2.close();
					}
					
					ps.setString(15,""+poDDFlag);
					ps.setString(16,""+remarks);
					ps.setString(17,""+intID);
					
					ps.execute();
					ps.getMoreResults();
					if(poDDFlag==1)
						ps.getMoreResults();
			}
			  
		
			rsInsert = ps.getResultSet();
			if(rsInsert.next())
			{
				int smsId =rsInsert.getInt("SMSID");
				String dpSlipNo = rsInsert.getString("DepSlipNo");
				retVal.put("SMSID", ""+smsId);
				retVal.put("DepSlipNo", dpSlipNo);
			}
			rsInsert.close();
			ps.close ();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return retVal;
		
	}

	public boolean sendToCheckOrMake(Map<String, String[]> param, int paymentStatus) {
		ArrayList<Integer> listTran = new ArrayList<Integer>(); 
		try{
			int rowCount = Integer.parseInt(param.get("rowCount")[0]);
			Collection<String> keys = param.keySet();
			
			
			for(int i=0;i<rowCount;i++)
			{
				if(keys.contains("checkBox"+i))
				{
					 changePaymentStatusByIntId(Integer.parseInt(param.get("checkBox"+i)[0]),paymentStatus);
					 listTran.add(Integer.parseInt(param.get("checkBox"+i)[0]));
					
				}
			}
		}catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
		if(paymentStatus==PaymentStatus.CONFIRMED)
			confirmTransactions(listTran);
		
		return true;
	}
	
	/*public boolean sendBackToMaker(Map<String, String[]> param) {
		
		try{
			int rowCount = Integer.parseInt(param.get("rowCount")[0]);
			Collection<String> keys = param.keySet();
			
			for(int i=0;i<rowCount;i++)
			{
				if(keys.contains("checkBox"+i))
				{
					 changePaymentStatusByIntId(Integer.parseInt(param.get("checkBox"+i)[0]),PaymentStatus.MODIFIED);
					
				}
			}
		}catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}*/
	
	private boolean confirmTransactions(ArrayList<Integer> listTran) {
		

		try{
			Connection currentCon = null;
			Statement stmt = null;
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			
		//	//System.out.println("######### CHECKED ITEM : "+param.get("checkBox"+i)[0]);
			String updateTransaction = "UPDATE [dbo].[tblNCSTxnInformation] SET"
					+ " Confirmed=1, mail_sent=0 WHERE intID IN ("+new Utility().lsitToString(listTran)+")";
			//System.out.println("###### UPDATE TRANX: "+updateTransaction);
			
			stmt.executeUpdate(updateTransaction);
			stmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private boolean changePaymentStatusByIntId(int intID, int paymentStatus) {
		
		try{
			Connection currentCon = null;
			Statement stmt = null;
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			
		//	//System.out.println("######### CHECKED ITEM : "+param.get("checkBox"+i)[0]);
			String updateTransaction = "UPDATE [dbo].[tblNCSTxnInformation] SET"
					+ " paymentStatus="+paymentStatus+" WHERE intID="+intID;
	//		//System.out.println("###### UPDATE TRANX: "+updateTransaction);
			
			stmt.executeUpdate(updateTransaction);
			stmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public ArrayList<Transaction> getTransactionByMerchantIdAndDate(
			int merchantId, String rcvFromDate, String rcvToDate,ArrayList<Integer> paymentStatusList) {
		
		
		ArrayList<Transaction> tranList = new ArrayList<Transaction>();
		
		try{
			Connection currentCon = null;
			Statement stmt = null;
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			
			String getTransaction = "SELECT * ,"
					+ " distInfo.DistributorName as DistName ,"
					+ " payst.paymement_stat as paymentStatus"
					+ " FROM [dbo].[tblNCSTxnInformation] AS tranInfo"
					+ " INNER JOIN [dbo].[NCS_Distributor_Info] as distInfo ON distInfo.DistributorCode=tranInfo.CustCode"
					+ " INNER JOIN [dbo].[NCS_Payment_Status_Code] as payst ON payst.pay_stat_id=tranInfo.paymentStatus"
					+ " WHERE"
					+ " CustCode IN "
					+ " (SELECT DistributorCode FROM [dbo].[NCS_Distributor_Info] WHERE [Merchant_Id]="+merchantId+")"
					+ " AND paymentStatus IN ("+new Utility().lsitToString(paymentStatusList)+")"
					+ " AND ReceivingDate between '"+rcvFromDate+"' AND '"+rcvToDate+"'";
			
			System.out.println("#### SQL for GET TRAN :"+getTransaction);
			ResultSet rs = stmt.executeQuery(getTransaction);
			
			tranList = resultSetToArrayList(rs);
			stmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return tranList;
		
		}
		return tranList;
	}
	
	public ArrayList<DcheckerTranList> getDcheckerStatus(int dateInterval , String routingNo){
		
	//	String spSQL = "EXEC  assng.NCS_GETFROMDCHECKER ?, ?";
		Connection con =null;
		ArrayList<DcheckerTranList> dTranList = new ArrayList<DcheckerTranList>(); 
		
		try 
		{
		   con = ConnectionManager.getConnectionOracle();
		   CallableStatement ocs=con.prepareCall("{call NCS_GETFROMDCHECKER(?,?,?)}");
		   ocs.registerOutParameter(1,OracleTypes.CURSOR);
		   ocs.setString(2,routingNo);
		   ocs.setInt(3,dateInterval);
		   ocs.execute();
		   ResultSet rs = (ResultSet)ocs.getObject(1);
		   while(rs.next())
		   {
			   	DcheckerTranList dTran  = new DcheckerTranList();
				dTran.branchRoutingNo = rs.getString(1);
				System.out.println("########## branchRoutingNo: "+dTran.branchRoutingNo);
				dTran.chequeNo = rs.getString(2);
				System.out.println("########## chequeNo: "+dTran.chequeNo);
				dTran.clearingType = rs.getString(3);
				System.out.println("########## CLEARING TYPE: "+dTran.clearingType);
				dTran.amount = rs.getString(4);
				System.out.println("########## amount: "+dTran.amount);
				dTran.cRAccountNo = rs.getString(5);
				System.out.println("########## CRAccountNo: "+dTran.cRAccountNo);
				dTran.presentmentFlag = rs.getString(6);
				System.out.println("########## PRESENTMENT FLAG: "+dTran.presentmentFlag);
				dTran.dcheckerStatus = rs.getString(7);
				System.out.println("########## DCHECKER STATUS: "+dTran.dcheckerStatus);
				if(rs.getString(8)!=null)
					dTran.finalClearingStatus = rs.getString(8);
				else
					dTran.finalClearingStatus ="PROCESSING";
				System.out.println("########## final Clearing Status:"+dTran.finalClearingStatus);
				
				dTranList.add(dTran);
			}
			rs.close();
			ocs.close();
		//	ps.close();
			con.close();
		//	//System.out.println("####################"+retModules.size());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return dTranList;
			
		}
		return dTranList;
	}
	public static void main(String[] args){
		new Transaction().getDcheckerStatus(8,"175264276");
	}
	
}
