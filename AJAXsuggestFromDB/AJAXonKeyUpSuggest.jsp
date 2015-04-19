
<%@page import="dbModel.MerchantSetup"%>
<%@page import="utils.FetchFromDB"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@ include file="../../mainHeader.jsp" %> 


<div class="container">
            <div class="row">
                <div class="twelve columns">
                    <div class="box_c">
                        <div class="box_c_heading cf">
                            <div class="box_c_ico"><img src="${pageContext.request.contextPath}/img/ico/icSw2/16-List.png" alt="" /></div>
                            <p>Distributor Registration</p>
                        </div>
                        <div class="box_c_content form_a">
						<form id="distributorRegis" action="${pageContext.request.contextPath}/validateServlet" class="cmxform" method="POST">
									<input type="hidden" id="formName" name="formName" value="NCS_DistributorRegistration"/>
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
											 <li><label>Distributor Name:</label><input class="input-text" type="text" name="DistributorName" /></li>
                                        	  <li><label>Distributor Code:</label><input autocomplete="off" class="input-text" type="text" id="DistributorCode" name="DistributorCode" OnkeyUp="suggestUser()" />
                                        	   <div id="notavailable" style="display:none;color:red">
										<b>NOT AVAILABLE</b>
									</div>
									<div id="available" style="display:none;color:green">
										<b>AVAILABLE</b>
									</div> 
                                        	  </li>
                                           
                                            
                                            
                                    	  </ul>
                                    </div>
                                	</div>
									
									
									
									 <div class="formRow">
                                	 <input type="submit" value="submit"  class="button small nice blue"> 
                                  <!--   <button type="submit" class="button small nice blue">Send form</button> -->
                               		 </div>
								
								</form>
						</div>
                    </div>
                </div>
            </div>
        </div>
	<script>
	


 	function suggestUser(){
		var distCode = document.getElementById("DistributorCode").value;
		
		document.getElementById("available").style.display="none";
		document.getElementById("notavailable").style.display="none";
		
		/* if(distCode==""){
			document.getElementById("available").style.display="none";
			document.getElementById("notavailable").style.display="block";
		}
		 */
		$.ajax({
			type:"GET",
			url:"${pageContext.request.contextPath}/AjaxHandler",
			data:"distCode="+distCode,
			success:function(data){
				//alert(data.ret);
				if(data.ret=="TRUE"){
					document.getElementById("available").style.display="none";
					document.getElementById("notavailable").style.display="block";
				}
				else if(data.ret=="FALSE"){
					document.getElementById("notavailable").style.display="none";
					document.getElementById("available").style.display="block";
				}
			}
			
		});
	} 
	
	$("#distributorRegis").validate({ 
        rules: { 
        	MerchantID: { 
        		number:true,
	          required: true,
	          maxlength:18
	        }, 
	        DistributorCode:{
	     	 	  required: true,
		          maxlength:30   
	        },
	        DistributorName: {
     	 	  required: true,
	          maxlength:150   
        	},
         
        	DistributorShortName: {
        		required: true,
	     	 	maxlength:55
	        
        	},
        	DistributorAddress: { 
     	 	  required: true,
     	 	  maxlength:250,
	          
        	},  
        	DistributorLocation:{
        	  required: true,
        	  maxlength:100,
        		
        	},
        	DistributorVendorCode:{
        		required: true,
          	 	maxlength:3,
        	},
        	DistributorTypeID:{
        		maxlength:3
        		
        	},
        	DistributorZoneID:{
        		maxlength:18,
        		number:true
        		
        	},
        	ToEmail:{
        		required: true,
        		maxlength:100
        		
        	},
        	CCEmail:{
        		maxlength:100,
        		required: true
        	},
        	DefaultEmail:{
        		maxlength:100,
        		required: true
        	}
        	
        	
        }, 
       /*  messages: {  
        	Name: {
    	      required: "Please enter Name.",
    	    }, 
    	    TypeID: {
    	      required: "Please enter TypeID."
    	    },
    	    MerchantShortName: {
    	      required: "Please enter Merchant Short Name"
    	    }, 
    	    AccountNo: {
        	  required: "Please enter Account No."
        	},
        } */ 
      });
	
	</script>
<%@ include file="../../mainFooter.jsp" %> 