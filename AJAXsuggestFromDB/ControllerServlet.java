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
		else{
			
		}
	}


}
