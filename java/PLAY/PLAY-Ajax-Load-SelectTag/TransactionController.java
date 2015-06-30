package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.bank.Branch;
import models.transaction.Transaction;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.transaction.livetranview;
import views.html.transaction.liveTranViewPage;
import views.html.transaction.transactionSearch;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.google.gson.Gson;

public class TransactionController extends Controller {
public static Result showAllTran(){
    	
    	List<Transaction> allTran = Transaction.findAllTran();
    	return ok(livetranview.render(allTran));
    }
  public static Result showTranByInOut(int flag){
	  
	  
		  List<Transaction> allTran = Transaction.findTranByInOutFlag(flag);
		  return ok(livetranview.render(allTran));
	  
  }
  public static Result showTranDataTable() {
	  
	  return ok(liveTranViewPage.render());
  }
  public static Result searchTransactions(){
	  return ok(transactionSearch.render(new ArrayList<Transaction>()));
	  
  }
  
  public static Result getBranchListByBankId(){
	   Map<String, String[]> params = request().queryString();
	   String bankId = params.get("bankId")[0];
	   
	  // System.out.println("################## BANK ID ###########"+bankId);
	//   System.out.println("##_______________________________________#####");
	   List<Branch> branchList = Branch.getBranchByBankId(bankId);
	//   System.out.println("##_______________________________________#####");
	   Gson gson = new Gson();
	   Map<String,String> branchIdName = new HashMap<String,String>();
	   for(Branch branch:branchList){
		   branchIdName.put(""+branch.id,branch.name);
	   }
	   
	   String toJson = gson.toJson(branchIdName);
	   return ok(""+toJson);
  }
  
  public static Result searchLogs(){
	  Map<String, String[]> data = request().body().asFormUrlEncoded();
	  String bankId = data.get("bank")[0];
	  String branchId = data.get("branch")[0];
	  String origin = data.get("origin")[0];
	  String from_acc = data.get("from_acc")[0];
	  String to_acc = data.get("to_acc")[0];
	  String tranType = data.get("tranType")[0];
	  String currency = data.get("currency")[0];
	  
	  	flash("bank", bankId);
		flash("branch", branchId);
		flash("origin", origin);
		flash("from_acc", from_acc);
		flash("to_acc", to_acc);
		flash("currency", currency);
		flash("tranType", tranType);
		
	  System.out.println("#### bankId:"+bankId+"===branch:"+branchId+"&&&&origin: "+origin+"====fromacc:"+from_acc+"$$$$$$to_acc"+to_acc+"%%%% CURRENCY: "+currency);
	  List<Transaction> tranList = Transaction.searchTransactions(data);
	  
	  
	  return ok(transactionSearch.render(tranList));
  }
  
  public static Result listTranDataTable() {
	    /**
	     * Get needed params
	     */
	    Map<String, String[]> params = request().queryString();
	 
	    Integer iTotalRecords = Transaction.find.findRowCount();
	    String filter = params.get("sSearch")[0];
	    Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
	    Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;
	 
	    /**
	     * Get sorting order and column
	     */
	    String sortBy = "id";
	    String order = params.get("sSortDir_0")[0];
	 
	    switch(Integer.valueOf(params.get("iSortCol_0")[0])) {
	      case 0 : sortBy = "id"; break;
	      case 1 : sortBy = "fromAccountName"; break;
	      case 2 : sortBy = "fromAccountNumber"; break;
	    }
	 
	    /**
	     * Get page to show from database
	     * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
	     */
	    Page<Transaction> tranPage = Transaction.find.where(
	      Expr.or(
	        Expr.ilike("id", "%"+filter+"%"),
	        Expr.or(
	          Expr.ilike("fromAccountName", "%"+filter+"%"),
	          Expr.ilike("fromAccountNumber", "%"+filter+"%")
	        )
	      )
	    )
	    .orderBy(sortBy + " " + order + ", id " + order)
	    .findPagingList(pageSize).setFetchAhead(false)
	    .getPage(page);
	 
	    Integer iTotalDisplayRecords = tranPage.getTotalRowCount();
	 
	    /**
	     * Construct the JSON to return
	     */
	    ObjectNode result = Json.newObject();
	 
	    result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
	    result.put("iTotalRecords", iTotalRecords);
	    result.put("iTotalDisplayRecords", iTotalDisplayRecords);
	 
	    ArrayNode an = result.putArray("aaData");
	 
	    for(Transaction c : tranPage.getList()) {
	      ObjectNode row = Json.newObject();
	      row.put("0", c.id);
	      row.put("1", c.fromAccountName);
	      row.put("2", c.fromAccountNumber);
	      an.add(row);
	    }
	 
	    return ok(result);
	 }
}
