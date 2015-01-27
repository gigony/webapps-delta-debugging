//
// matchesadmin.js - javascript associated with the A/Bmatchesadmin.html XHTML page
// Author: Wayne Motycka
// Date: Mar 25, 2008
// CS464 Spring 08
// HW 4
//
// global row counters for the tables
//
// insert the row into the appropriate round table
//
function insRow()
{
    if(chkVals()) {
        var rndnum = document.getElementById('SemiRound').value;
        var rndId = "rnd"+rndnum;     // the round to change/create
	//
	// this next part creates a new round if one did not already exist
	//
        if(document.getElementById(rndId) == null)    //create a new round
	{
            var lbl = document.createElement('h3');
            var txtl = document.createTextNode("Round "+rndnum);
            lbl.appendChild(txtl);
            lbl.style.textAlign="center";
            var f = document.getElementById('tbls');  // get the form for tables
            var t = document.createElement('table');
            t.id=rndId;
            t.setAttribute("cellspacing", "20px");
            f.appendChild(lbl);            //put the heading for the round on the form
            f.appendChild(t);              // put new round on form
            addhdr(t);                    // put a table header on new round
        }
        var lrnd = document.getElementById(rndId);  // lrnd is the round being changed
	//
        // walk through the table looking for a matching team pairing
	//
        var matchrow=-1;
        for(var k=1; k < lrnd.getElementsByTagName("tr").length;k++)
        {
            celz=lrnd.rows[k].cells;
            if((celz[0].innerHTML == document.getElementById('TeamNum1').value) &&
	                (celz[2].innerHTML == document.getElementById('TeamNum2').value))
            {
               // k is the row that has the same pair of teams in the table...
               matchrow=k;
            }
        }
        var x;
        var a;
        var b;
        var c;
        var d;
        var e;
        var z = document.getElementById(rndId);
        if(matchrow > 0)
        {
            x = z.rows[matchrow];
            a = x.cells[0];
            b = x.cells[1];
            c = x.cells[2];
            d = x.cells[3];
            e = x.cells[4];
        }
        else
        { // need a new row
            rowcnt=z.getElementsByTagName("tr").length;
            x = z.insertRow(rowcnt);
            a=x.insertCell(0);
            b=x.insertCell(1);
            c=x.insertCell(2);
            d=x.insertCell(3);
            e=x.insertCell(4);
        }
	// set the table values from the form
        a.innerHTML=document.getElementById('TeamNum1').value;
        b.innerHTML=document.getElementById('TeamScore1').value;
        c.innerHTML=document.getElementById('TeamNum2').value;
        d.innerHTML=document.getElementById('TeamScore2').value;
        e.innerHTML="<input type=\"checkBox\" name=\"erase\" onclick=\"chkDel(this)\"/>";
	// clear the form
        document.getElementById('SemiRound').value = "";
        document.getElementById('TeamNum1').value = "";
        document.getElementById('TeamScore1').value = "";
        document.getElementById('TeamNum2').value="";;
        document.getElementById('TeamScore2').value = "";
        document.getElementById('SemiRound').focus();
    }
}
//
// add the headers to the tables executed at page load
//
function addhdr(tbl)
{
    if(!tbl)
    {
        for(var k=0; k<document.getElementById('tbls').getElementsByTagName('table').length; k++)
        {
            stbl=document.getElementById('tbls').getElementsByTagName('table');
            addhdr(stbl[k]); //recurse on this function to add the headers to the tables on pageload
        }
    }
    else
    {
        var nav = navigator.appName;
        if(nav == "Microsoft Internet Explorer")
        {
            var at = document.getElementById(tbl.id);
	    at.setAttribute("width", 800);
        }
	var y = document.getElementById(tbl.id);
        var arow=document.getElementById(tbl.id).insertRow(0);
        var hd1a=arow.insertCell(0);
        var hd2a=arow.insertCell(1);
        var hd3a=arow.insertCell(2);
        var hd4a=arow.insertCell(3);
        var hd5a=arow.insertCell(4);
        hd1a.innerHTML="<b>Team Number</b>";
        hd2a.innerHTML="<b>Team Score</b>";
        hd3a.innerHTML="<b>Team Number</b>";
        hd4a.innerHTML="<b>Team Score</b>";
        hd5a.innerHTML="<b>Delete</b>";
        var p = document.getElementById("SemiRound");
        p.focus();
    }
}
    //
// delete a row from the table
//
function chkDel(what)
{
    var targ=what.parentNode.parentNode;
    var oarg=targ.parentNode;
    oarg.removeChild(targ);
    var p = document.getElementById("SemiRound");
    p.focus();
}
//
// check the values in the input fields
//
function chkVals()
{
    var ret=false;
    var p = document.getElementById("SemiRound");
    var tn = document.getElementById("TeamNum1");
    var tw = document.getElementById("TeamScore1");
    var tn2 = document.getElementById("TeamNum2");
    var tw2 = document.getElementById("TeamScore2");
    var pos = p.value.search(/[1-9][0-9]{0,1}/);
    var pos1 = tn.value.search(/[1-9][0-9]*/);
    var pos2 = tw.value.search(/[1-9][0-9]*/);
    var pos3 = tn2.value.search(/[1-9][0-9]*/);
    var pos4 = tw2.value.search(/[1-9][0-9]*/);
    if (pos != 0)
    {
	alert("Invalid semi round number");
        p.focus();
	p.select();
        ret=false;
    }
    else if (pos1 != 0)
    {
	alert("Team number must be numeric, non-zero");
        tn.focus();
	tn.select();
        ret=false;
    }
    else if (pos2 != 0 && tw.value!="")
    {
	alert("Team scores must contain only numeric values");
        tw.focus();
	tw.select();
        ret=false;
    }
    else if (pos3 != 0)
    {
	alert("Team number must be numeric, non-zero");
        tn2.focus();
	tn2.select();
        ret=false;
    }
    else if (pos4 != 0 && tw2.value!="")
    {
	alert("Team scores must contain only numeric values");
        tw2.focus();
	tw2.select();
        ret=false;
    }
    else
    {
	ret=true;
    }
    return ret;
}
