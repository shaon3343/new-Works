  /*
  GENERATE TABLE FROM JSON : 
	{key:
		{keyInt:valueInt,keyString:valString,keyObjArray:
				[{key:val,key:val ...},{key:val,key:val ...}, ...]}, 
		{keyInt:valueInt,keyString:valString,keyObjArray:
				[{key:val,key:val ...},{key:val,key:val ...}, ...]},
		...
	}
  
  */
  
  var getTable = function(context){

     var tbl     = document.createElement("table");
     tbl.setAttribute('class','table table-bordered table-striped table-condensed');
     
     var thead = document.createElement("thead");
     var tr = document.createElement("tr");
     var th1 = document.createElement("th");
     var th2 = document.createElement("th");
     var th3 = document.createElement("th");
     
     var thText1 = document.createTextNode("Transaction Type");
     var thText2 = document.createTextNode("Transaction Count");
     var thText3 = document.createTextNode("Transaction Amount");
     
     th1.appendChild(thText1);
     th2.appendChild(thText2);
     th3.appendChild(thText3);
     
     tr.appendChild(th1);
     tr.appendChild(th2);
     tr.appendChild(th3);
     
     thead.appendChild(tr);
     
     tbl.appendChild(thead);	
     
     var tblBody = document.createElement("tbody");
     tblBody.setAttribute('id', 'accordion');
     var collapseCount = 0;
     $.each(context[0],function(k,v){
	    
    	 var innerTR = document.createElement("tr");
	     
	     var innerTD = document.createElement("td");
	     innerTD.innerHTML = k; // to be set from CONTEXT
	     var innerA = document.createElement('a');
	     innerA.setAttribute('data-toggle', 'collapse');
	     innerA.setAttribute('data-parent', '#accordion');
	     innerA.setAttribute('href', '#collapse'+collapseCount);
	     innerA.setAttribute('style', 'padding-right: 10px');
	     
	     var imgA = document.createElement('img');
	     imgA.setAttribute('class', 'btn');
	     imgA.setAttribute('src', '/assets/images/details_open.png');
	     
	     innerA.appendChild(imgA);
	     innerTD.appendChild(innerA);
	     
	     var tblInner     = document.createElement("table");
	     tblInner.setAttribute('id', 'collapse'+collapseCount);
	     tblInner.setAttribute('class', 'table table-bordered table-striped table-condensed panel-collapse collapse');
	    
	     var theadInner = document.createElement("thead");
	     theadInner.setAttribute('style', 'color: #1FB5AD');
	     var trInner = document.createElement("tr");
	     
	     var thInner1 = document.createElement("th");
	     thInner1.innerHTML = "From Bank";
	     var thInner2 = document.createElement("th");
	     thInner2.innerHTML = "To Bank";
	     var thInner3 = document.createElement("th");
	     thInner3.innerHTML = "To Account Name";
	     var thInner4 = document.createElement("th");
	     thInner4.innerHTML = "To Account Number";
	     var thInner5 = document.createElement("th");
	     thInner5.innerHTML = "From Account Name";
	     var thInner6 = document.createElement("th");
	     thInner6.innerHTML = "From Account Number";
	     var thInner7 = document.createElement("th");
	     thInner7.innerHTML = "Amount";
	     var thInner8 = document.createElement("th");
	     thInner8.innerHTML = "Status";
	     
	     trInner.appendChild(thInner1);
	     trInner.appendChild(thInner2);
	     trInner.appendChild(thInner3);
	     trInner.appendChild(thInner4);
	     trInner.appendChild(thInner5);
	     trInner.appendChild(thInner6);
	     trInner.appendChild(thInner7);
	     trInner.appendChild(thInner8);
	     
	     theadInner.appendChild(trInner);
	     tblInner.appendChild(theadInner);
	     
	     var tblBodyInner = document.createElement("tbody");
	     collapseCount++;
	  //   console.log("DUMMY TRAN "+v.dummyTran);
	     for(var i=0;i<v.dummyTran.length;i++){
	    	 var trForTblInner = document.createElement("tr");
		     var tdForTblInner1 = document.createElement("td");
		     tdForTblInner1.innerHTML = v.dummyTran[i].fromBank;
		     var tdForTblInner2 = document.createElement("td");
		     tdForTblInner2.innerHTML =  v.dummyTran[i].toBank;
		     var tdForTblInner3 = document.createElement("td");
		     tdForTblInner3.innerHTML = v.dummyTran[i].toAccountName;;
		     var tdForTblInner4 = document.createElement("td");
		     tdForTblInner4.innerHTML = v.dummyTran[i].toAccountNumber;
		     var tdForTblInner5 = document.createElement("td");
		     tdForTblInner5.innerHTML =  v.dummyTran[i].fromAccountName;
		     var tdForTblInner6 = document.createElement("td");
		     tdForTblInner6.innerHTML =  v.dummyTran[i].fromAccountNumber;
		     var tdForTblInner7 = document.createElement("td");
		     tdForTblInner7.innerHTML =  v.dummyTran[i].amount;
		     var tdForTblInner8 = document.createElement("td");
		     tdForTblInner8.innerHTML =  v.dummyTran[i].status;
		     
		     trForTblInner.appendChild(tdForTblInner1);
		     trForTblInner.appendChild(tdForTblInner2);
		     trForTblInner.appendChild(tdForTblInner3);
		     trForTblInner.appendChild(tdForTblInner4);
		     trForTblInner.appendChild(tdForTblInner5);
		     trForTblInner.appendChild(tdForTblInner6);
		     trForTblInner.appendChild(tdForTblInner7);
		     trForTblInner.appendChild(tdForTblInner8);
		     
		     tblBodyInner.appendChild(trForTblInner);
	     }
	     
	     
	     tblInner.appendChild(tblBodyInner);
	     
	     innerTD.appendChild(tblInner);
	     
	     
	     
	     innerTR.appendChild(innerTD);
	     
	     var innerTD1 = document.createElement("td");
	     innerTD1.innerHTML = v.totAmount;
	     var innerTD2 = document.createElement("td");
	     innerTD2.innerHTML = v.totRowCount;
	     
	     innerTR.appendChild(innerTD1);
	     innerTR.appendChild(innerTD2);
	     
	     
	     tblBody.appendChild(innerTR);
     
     })
     
     
     tbl.appendChild(tblBody);
    
     
     
      
    return tbl;              
  };