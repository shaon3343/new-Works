package user;

import java.io.IOException;
import java.io.PrintWriter;
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
			merchantId = Integer.parseInt(request.getParameter("merchantId"));
		String rcvFromDate = request.getParameter("recvFromDate");
		String rcvToDate = request.getParameter("recvToDate");
		int payStat = -1;
		if(request.getParameter("payStat")!=null)
			payStat=Integer.parseInt(request.getParameter("payStat"));
	
		
		
//		String moduleId = request.getParameter("moduleId");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String toJSON="";
	
		
		else if(rcvFromDate!=null && rcvToDate!=null){ 
		
	//		//System.out.println("########## MERCHANT ID : "+merchantId+"recieve FROM DATE: "+rcvFromDate
		//						+"RECEIVE TO DATE: "+rcvToDate);
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
			toJSON = gson.toJson(listTran);
			response.setContentType("application/json");
			
			out.print(toJSON);
			out.flush();
			
			
		}
		
		
		else{
			
		}
	}


}
