package user;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import secureData.UserBean;
import secureData.UserDao;
import utils.Constants;
import menuManagement.Form;
import menuManagement.Module;
import menuManagement.RolePrivilege;
import FileWriteProcess.FileWriteProcess;
import authentication.ModuleManagement;
import authentication.Transaction;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dbModel.DataManagement;
import dbModel.DistributorEmailSetUp;
import dbModel.DistributorSetUp;
import dbModel.MerchantSetup;
import dbModel.NCS_EmailColDef;
import dbModel.NCS_EmailColumnMapping;
import dbModel.NCS_FormSetup;
import dbModel.NCS_ModuleSetup;
import dbModel.NCS_RoleSetup;
import dbModel.NCS_savePageLinks;

/**
 * Servlet implementation class AjaxHandler
 */
@WebServlet("/AjaxHandler")
public class AjaxHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String formName = "";
		if(request.getParameter("formName")!=null)
			 formName = request.getParameter("formName");
		
		String roleId = request.getParameter("roleId");
	//	String distributorId = request.getParameter("distId");
		String moduleId = request.getParameter("moduleId");
		int merchantId=-1;
		if(request.getParameter("merchantId")!=null)
		{	
			if(!request.getParameter("merchantId").isEmpty())
				merchantId = Integer.parseInt(request.getParameter("merchantId"));
		}
		String rcvFromDate = request.getParameter("recvFromDate");
		String rcvToDate = request.getParameter("recvToDate");
		int payStat = -1;
		if(request.getParameter("payStat")!=null)
			payStat=Integer.parseInt(request.getParameter("payStat"));
	
		
		
//		String moduleId = request.getParameter("moduleId");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String toJSON="";
		
		
		// AJAX SUGGESTION :
		if(request.getParameter("distCode")!=null)
		{
			String distCode = request.getParameter("distCode");
			distCode = distCode.trim();
			response.setContentType("application/json");
			Map<String,String> ret = new HashMap<String,String>();
			
			if(distCode.equals("") || distCode.isEmpty())
				ret.put("ret","TRUE");
			else if(new DistributorSetUp().getDistByDistCode(distCode))
				ret.put("ret","TRUE");
			else
				ret.put("ret","FALSE");
			toJSON =  gson.toJson(ret);
		
			out.print(toJSON);
			out.flush();
		}
		else if(formName.equals("editEmailCol")){
		//	System.out.println("#### MERCHANT ID : "+merchantId);
			ArrayList<NCS_EmailColDef> listEmailCol = new NCS_EmailColDef().listAllEmailCol(merchantId);
			toJSON = gson.toJson(listEmailCol);
			response.setContentType("application/json");
			
			out.print(toJSON);
			out.flush();
		}
		else if(rcvFromDate!=null && rcvToDate!=null){ 
		
			/* for Javascript Data Table Start */
			
			 Integer iTotalRecords = 0;
			    String filter = request.getParameter("sSearch");
			    Integer pageSize = Integer.valueOf(request.getParameter("iDisplayLength"));
			    Integer page = Integer.valueOf(request.getParameter("iDisplayStart")) / pageSize;
			 
			    /**
			     * Get sorting order and column
			     */
			    String sortBy = "SMSID";
			    String order = request.getParameter("sSortDir_0");
			 
			    switch(Integer.valueOf(request.getParameter("iSortCol_0"))) {
			      case 0 : sortBy = "SMSID"; break;
			      case 1 : sortBy = "DepSlipNo"; break;
			      case 2 : sortBy = "Amount"; break;
			    }
			
			/* for Javascript Data Table END */
	System.out.println("########## MERCHANT ID : "+merchantId+"recieve FROM DATE: "+rcvFromDate
								+"RECEIVE TO DATE: "+rcvToDate);
			ArrayList<Integer> paymentStatusList = new ArrayList<Integer>(); 
			if(payStat==0){
				paymentStatusList.add(0);
				paymentStatusList.add(2);
			}else if(payStat == 1){
				paymentStatusList.add(1);
			}else if(payStat==-1){
				paymentStatusList.add(0);
				paymentStatusList.add(1);
				paymentStatusList.add(2);
				paymentStatusList.add(3);
				paymentStatusList.add(4);
				
			}
			
			ArrayList<Transaction> listTran = new Transaction().getTransactionByMerchantIdAndDate
									(merchantId,rcvFromDate,rcvToDate,paymentStatusList);
		
			
		/*	result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
		    result.put("iTotalRecords", iTotalRecords);
		    result.put("iTotalDisplayRecords", iTotalDisplayRecords);
		 
		    ArrayNode an = result.putArray("aaData");
		 
		    for(Contact c : contactsPage.getList()) {
		      ObjectNode row = Json.newObject();
		      row.put("0", c.name);
		      row.put("1", c.title);
		      row.put("2", c.email);
		      an.add(row);
		    }*/
			  /* For Javascript Data Table */
		    Integer iTotalDisplayRecords = listTran.size();
		  
			iTotalRecords=listTran.size();
			
			
		    JsonObject innerObject = new JsonObject();
		    
		    innerObject.addProperty("sEcho",request.getParameter("sEcho"));
		    innerObject.addProperty("iTotalRecords", iTotalRecords);
		    innerObject.addProperty("iTotalDisplayRecords", iTotalDisplayRecords);
		    
		    JsonObject array = new JsonObject();
		    array.addProperty("list",gson.toJson(listTran));
		    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
		    JsonObject object = new JsonObject();
		    int i=0;
		    for(Transaction tran:listTran){
		    	
		    	object.addProperty("0", tran.smsId);
		    	object.addProperty("1", tran.depositSlipNo);
		    	object.addProperty("2", tran.distName);
		    	object.addProperty("3", tran.instrumentType);
		    	object.addProperty("4", tran.instrumentNo);
		    	object.addProperty("5", sdf.format(tran.instrumentDate));
		    	object.addProperty("6", tran.paySt);
		    	object.addProperty("7", sdf.format(tran.receivingDate));
		    	object.addProperty("8", tran.amount);
		    	object.addProperty("9", tran.remarks);
		    	array.addProperty(""+i,gson.toJson(object));

		    	i++;
		    }
		    String arrayToJson = gson.toJson(array);
		    innerObject.addProperty("aaData", arrayToJson);
		 //   toJSON = gson.toJson(listTran);
		    
		    
		//    innerObject.addProperty("aaData", toJSON);
			
			
			
			/*JsonObject jsonObject = new JsonObject();
			jsonObject.add("publisher", innerObject);*/
			
			/* For Javascript data table*/
		    arrayToJson = gson.toJson(innerObject);
			toJSON = gson.toJson(listTran);
			response.setContentType("application/json");
			
			out.print(innerObject);
			out.flush();
			
			
		}
		
		if(moduleId==null && roleId!=null){
			ArrayList<Module> moduleList = new ModuleManagement().getAllModules();
		//	moduleList = new ModuleManagement().selectModuleByParentId(moduleList,0);
			toJSON = gson.toJson(moduleList);
			response.setContentType("application/json");
			
			out.print(toJSON);
			out.flush();
		}
		else if(moduleId!=null){  // SETUP ROLE ON FORMS :
			
			ArrayList<RolePrivilege> form = new ArrayList<RolePrivilege>(); 
			
			form = new ModuleManagement().getAllFormsbyModuleId(Integer.parseInt(moduleId),Integer.parseInt(roleId));
		
			toJSON = gson.toJson(form);
			response.setContentType("application/json");
			
			out.print(toJSON);
			out.flush();
		}
		else{
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		
		try {
			HttpSession session = request.getSession();
			UserBean user = (UserBean) session.getAttribute("currentSessionUser");
			
			String formName = request.getParameter("formName");
			 
			 Map<String, String[]> parameters = request.getParameterMap();
			 
			 
			

			 // Saving Form Data By SWITCH : 
			 switch(formName) {
	            case "PrivilegeSetup":
	            	try{
		            	int formLoop = Integer.parseInt(parameters.get("frmLoop")[0]);
		            	Collection<String> keys = parameters.keySet();
		            	int roleId = Integer.parseInt(parameters.get("roleId")[0]);
		            	//System.out.println("############## formLoop: "+formLoop);
		            	
		            	for(int i=1;i<formLoop;i++){
		            		////System.out.println("############## key : "+key+"#######$$$$$$$$$ VALUE: "+parameters.get(key)[0]);
		            		
		            		
		            		int formId = Integer.parseInt(parameters.get("frmName"+i)[0]);
		            		
		            		int isSelect = 0;
		            		int isUpdate = 0;
		            		int isDelete = 0;
		            		int isInsert = 0;
		            
		            		if(keys.contains("isSelect"+i)){
		            			isSelect=1;
		            		}
		            			            		
		            		if(keys.contains("isUpdate"+i)){
		            			isUpdate = 1;
		            		}
		            		
		            		
		            		if(keys.contains("isInsert"+i)){
		            			
		            			isInsert = 1;
		            		}
		            		
		            		
		            		if(keys.contains("isDelete"+i)){
		            			isDelete = 1;
		            		}
		            		
		            	//	//System.out.println("##### formId="+formId+"###RoleId="+roleId+"## isSelect="+isSelect+"##isInsert="+isInsert+"###isUpdate="+isUpdate+"###isDelete="+isDelete);
		            		new ModuleManagement().insertRolePrivilege(formId,roleId,isSelect,isUpdate,isDelete,isInsert);
		            	}
		            	session.setAttribute("successmsg",Constants.SUCCESSFULLY_SAVED);
						response.sendRedirect(request.getContextPath()+"/commonSuccessError.jsp");
				 }catch(Exception e){
					e.printStackTrace();
					session.setAttribute("errormsg",Constants.VALUE_EXIST);
	            	response.sendRedirect(request.getContextPath()+"/commonSuccessError.jsp");
				 }
	            	break;
	                
	            default:
	            	response.sendRedirect(request.getContextPath());
	        }
			
			 		
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendRedirect(request.getContextPath());
		}
		
	}

}
