
<%@page import="dbModel.NCS_RoleSetup"%>
<%@page import="dbModel.MerchantSetup"%>
<%@page import="dbModel.DataManagement"%>
<%@page import="utils.FetchFromDB"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ include file="../../mainHeader.jsp" %> 

<%
	int roleId = user.getRoleId();
	System.out.println("#### ROLE ID : "+roleId);
	String userType = new NCS_RoleSetup().getRoleNamebyId(roleId).toLowerCase();

%>
<div class="container">
            <div class="row">
                <div class="twelve columns">
                    <div class="box_c">
                        <div class="box_c_heading cf">
                            <div class="box_c_ico"><img src="${pageContext.request.contextPath}/img/ico/icSw2/16-List.png" alt="" /></div>
                            <p>Search Transaction </p>
                        </div>
                        <div class="box_c_content form_a">
						
								<form id="tranInsert" action ="${pageContext.request.contextPath}/validateServlet" class="cmxform" method="POST" >
							<%if(userType.equals("maker")){ %>
								<input type="hidden" id="formName" name="formName" value="editTran_maker"/>
							<%}else if(userType.equals("checker")){ %>
								<input type="hidden" id="formName" name="formName" value="editTran_checker"/>	
							<%} %>
								<!--  <%-- action = ${pageContext.request.contextPath}/validateServlet --%> -->
								<!-- 
									 <input type="hidden" id="formName" name="formName" value="PrivilegeSetup"/> -->
								
								<div class="formRow">
                                 	<div class="elVal">
                                        <ul class="block-grid three-up mobile">
                                         <li><label>Merchant Name:</label>
                                     	<select id="MerchantID" name="MerchantID" >
											<option value=""> Please Select </option>
										 	<% 
											ArrayList<String> allMerchantNameId = MerchantSetup.getAllMerchantNameId();
											for(int i=0;i<allMerchantNameId.size();i=i+2)
											 {
											%>
												<option  value="<%=allMerchantNameId.get(i) %>"><%=allMerchantNameId.get(i+1) %> </option>
											<%
											 }
											%>
											
										</select>
											</li> 
                                         	<li><label>Receiving Date FROM:</label><input class="input-text" type="text" name="receiveFrom" id="receiveFrom" /></li>
                                         	<li><label>Receiving Date TO:</label><input class="input-text" type="text" name="receiveTo" id="receiveTo" /></li>
                                    	  </ul>
                                    	  
                                    </div>
                                	</div>	
                                	<div class="formRow">
                                	 <input type="button" value="Search"  class="button small nice blue" onclick="populateTranByMerchantId()"> 
                                 
                               		 </div>
                                	
                                   <div class="formRow">
                                   
                                   <table cellpadding="0" cellspacing="0" border="0" class="display mobile_dt1 dt_act" id="dt1">
                                <thead>
                                    <tr>
                                    <%if(userType.equals("maker") || userType.equals("checker")){%>
                                       <th class="essential">Select</th>
                                       <%} %>
                                 		<th class="essential">SMS ID</th>
										 <th class="essential">DEPOSIT SLIP NO</th>
										<th class="essential">DISTRIBUTOR Name</th>
										<th class="center">INSTRUMENT TYPE</th>
										<th class="center">INSTRUMENT NO</th> 
										<th >INSTRUMENT DATE</th>
										<!-- <th >ISSUING BRANCH</th>
										<th >DRAWING BRANCH</th> -->
										<th> Payment Status </th>
										<th >RECEIVING DATE</th>
										<th >AMOUNT</th>
										<th >REMARKS</th>
                                        
                                    </tr>
                                </thead>
                                <tbody id="formTable">
                                   <!--  <tr>
                                        <td class="chb_col"><input type="checkbox" name="row_sel" /></td>
                                        <td>Albania</td>
                                        <td>Tirana</td>
                                        <td>2,986,952</td>
                                        <td>28,748</td>
                                    </tr> -->
								</tbody>
                            </table>
                                   
                                   </div>
                                           										    
								 
									 
									 <div class="formRow">
									 <%if(userType.equals("maker")){%>
                                	  	<input type="submit" value="Send To Checker"  class="button small nice blue">
                                	  	<input type="hidden" value="0" id="makerChecker" name="makerChecker"/>
                                 	 <%}else if(userType.equals("checker")){ %>
                                 	 	<input type="submit" name="confirmed" value="Confirm"  class="button small nice blue">
                                	 	<input type="submit" name="sendBackToMaker" value="Send Back To Maker"  class="button small nice blue">
                                	 	<input type="hidden" value="1" id="makerChecker" name="makerChecker"/> 
                                 	 <%} %>
                               		 </div>
								
								</form>
						</div>
                    </div>
                </div>
            </div>
        </div>
	<script>
	function populateTranByMerchantId(){
		var merchantId = $("#MerchantID option:selected").val();
		var rcvFromDate = document.getElementById("receiveFrom").value;
		var rcvToDate = document.getElementById("receiveTo").value;
		if(document.getElementById("makerChecker")!=null)
		{
			var paystat=document.getElementById("makerChecker").value;
		}else
			paystat=-1;
		
		  if(rcvFromDate!='' || rcvToDate!=''){
		     $.ajax({
	                type : "GET",
	                url : "${pageContext.request.contextPath}/AjaxHandler",
	                data : "merchantId="+ merchantId+"&recvFromDate="+rcvFromDate+"&recvToDate="+rcvToDate+"&payStat="+paystat,
	            	success: function(data){
						
						$("#formTable").empty();
						var j=0;
						 for (i in data ) {
							$("#formTable").append('<tr>'+
							<%if(userType.equals("maker") || userType.equals("checker")){%>
									
                    		'<td class="center"><input type="checkbox" name="checkBox'+j+'" id="checkBox'+j+'" class="chk" value="'+data[i].smsId+'"/></td>'+
                    		<%}%>
                    		
                    		'<td>'+data[i].smsId+'</td>'+
                        	'<td>'+data[i].depositSlipNo+'</td>'+
                        	'<td>'+data[i].distName+'</td>'+
                        	'<td>'+data[i].instrumentType+'</td>'+
                        	'<td>'+data[i].instrumentNo+'</td>'+
                        	'<td>'+data[i].instrumentDate+'</td>'+
                        	
                        	/* '<td>'+data[i].issuingBranch+'</td>'+
                        	'<td>'+data[i].drawingBranch+'</td>'+ */
                        	'<td>'+data[i].paySt+'</td>'+
                        	
                        	'<td>'+data[i].receivingDate+'</td>'+
                        	'<td>'+data[i].amount+'</td>'+
                        	'<td>'+data[i].remarks+'</td>'+
                        	'</tr>');	 
		                    //    alert(data[i].name);
		                    j++;
		                }
						 if(j==0)
								alert("NO DATA FOUND!");
						 $("#formTable").append( '<input type="hidden" id="rowCount" name="rowCount" value="'+j+'"/>' );	
					}
	            });
		  }else{
			  $("#moduleListdiv").css("display","none");
			  $("#formTable").empty();
		  }
	}
	
	$("#receiveFrom").appendDtpicker({
		/* "dateOnly": true, */
		"dateFormat": "YYYY-MM-DD hh:mm",
		"minuteInterval": 15,
		"autodateOnStart": true
	});
	
	$("#receiveTo").appendDtpicker({
		"dateFormat": "YYYY-MM-DD hh:mm",
		"minuteInterval": 15,
		"autodateOnStart": true
	});
	
	
	</script>
<%@ include file="../../mainFooter.jsp" %> 