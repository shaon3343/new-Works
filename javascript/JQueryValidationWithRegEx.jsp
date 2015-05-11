
<%@page import="utils.PrivilegeConstant"%>
<%@page import="dbModel.DataManagement"%>
<%@page import="utils.FetchFromDB"%>
<%@page import="java.util.ArrayList"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ include file="../../mainHeader.jsp" %> 

<% 
	int formId = new DataManagement().getFormIdByUrl("/Menu/formSetup.jsp"); 
	
	if(!DataManagement.checkPrivilege(formId, user.getRoleId(),PrivilegeConstant.WRITE) && (user.getRoleId()!=1))
	{
		
		response.sendRedirect(request.getContextPath());
	}

%>


<div class="container">
            <div class="row">
                <div class="twelve columns">
                    <div class="box_c">
                        <div class="box_c_heading cf">
                            <div class="box_c_ico"><img src="${pageContext.request.contextPath}/img/ico/icSw2/16-List.png" alt="" /></div>
                            <p>Form Management</p>
                        </div>
                        <div class="box_c_content form_a">
						 <!-- <%-- <div style="color:red"><%=((session.getAttribute("errormsg")!=null)?(String)session.getAttribute("errormsg"):"") %></div> --%> -->
						 	<%-- <% session.setAttribute("errormsg",null); %> --%>
								<form id="formSetup" action="${pageContext.request.contextPath}/validateServlet" class="cmxform" method="POST">
									 <input type="hidden" id="formName" name="formName" value="FormSetup"/>
									
									<div class="formRow">
                                 	<div class="elVal">
                                        <ul class="block-grid three-up mobile">
                                        	
                                            
                                              <li><label>Form Name:</label><input class="input-text" type="text" name="Name" /></li>
                                             <li><label>Form Description:</label><input class="input-text" type="text" name="Description" /></li>
                                             <li>
                                             <label >Module:</label>
                                             <select id="Module" name="Module" >
                                             <%
                                             	String tableName = "[dbo].[NCS_MODULE]";
                                             	ArrayList<String> columnHeader = new ArrayList<String>();
                                             	columnHeader.add("MODULEID");
                                             	columnHeader.add("NAME");
                                             	columnHeader=DataManagement.getAll(columnHeader,tableName);
                                             	
                                             %>
                                             <option value=""> Please Select </option>
                                            <%  for(int i=0;i<columnHeader.size();i=i+2){
                                            %>
                                             		<option value="<%=columnHeader.get(i) %>"><%=columnHeader.get(i+1) %></option>
											<%} %>																						
											</select>
                                             </li>                                             
                                    	  </ul>
                                    </div>
                                	</div>
										
									<div class="formRow">
                                 	<div class="elVal">
                                        <ul class="block-grid three-up mobile">
                                       		                                       	                                       
                                             <li><label>Navigation URL:</label><input class="input-text" type="text" name="NavigationURL"  /></li>
                                             <li><label>Menu Label:</label><input class="input-text" type="text" name="MenuLabel"/></li>
                                              <li>
                                             <label >Is Menu Item:</label>
                                             <select id="IsMenuItem" name="IsMenuItem" >
											<option value=""> Please Select </option>
											
											<option value="Y"> YES </option>
											
											<option value="N"> NO </option>
											
											</select>
                                             </li>
                                    	  </ul>
                                    </div>
                                	</div>	                               	                              			
                                                										    
								  <!--   <p>
									<label>
								    Merchant Day End File Directory:</label>
									<input class="input-text" type="text" name="DayEndFileDirectory"  />
									</p> -->
									
									 <div class="formRow">
                                	 <input type="submit" value="Submit"  class="button small nice blue"> 
                                  <!--   <button type="submit" class="button small nice blue">Send form</button> -->
                               		 </div>
								
								</form>
						</div>
                    </div>
                </div>
            </div>
        </div>
	<script>
	$.validator.addMethod(
		    "regex",
		    function(value, element, regexp) {
		        var check = false;
		        var re = new RegExp(regexp);
		        return this.optional(element) || re.test(value);
		    },
		    "( - / . : \ a-z A-Z 0-9 Are allowed)"
		);
	
	$("#formSetup").validate({ 
        rules: { 
        	Name: { 
	          required: true,
	          maxlength:100
	        }, 
	        
	        Description: {
     	 	  required: true,
     	 	  maxlength:300
	       },
         
	       Module: {
	    	   required: true	     	 	        
        	},
        	
        	NavigationURL: { 
        		 required: true,
        		 regex:/^[A-Za-z0-9:./\-]+$/,
        	 	  maxlength:100
        	},
        	MenuLabel: { 
     	 	  required: true,
	          maxlength:100,
	          	          	         
        	},  
        	IsMenuItem:{
        	  required: true,
        	  
        		
        	},  
        	       	
        	/* IsActive:{
        		required: true,
        		
        	} */
        	
        }, 
      
      });
	
	</script>
<%@ include file="../../mainFooter.jsp" %> 