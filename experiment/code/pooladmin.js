//
// pooladmin.js - javascript associated with the pool[AB]matchadm.xml
// pages for administration of the match rounds of the tournament
// Author: Wayne Motycka
// Date: Mar 25, 2008
// CS464 Spring 08
// HW 4
//
// global row counters for the tables
var rowcnt;
//
// insert the row into the appropriate round table
//
function insRow()
{
    if(chkVals()) {
        var x;
        var y;
        var z;
        var c;
        var d;
        var g;
        var ret=-1;

        var rnd = document.getElementById('roundNum').value;
	var rndtbl = "tbl"+rnd;
        var whichtbl = document.getElementById(rndtbl);
	if(whichtbl == null)  // adding a new round case
        {
	    alert("adding a new round table: "+rndtbl);
	    whichform= document.getElementById("tbls");
	    whichtbl = document.createElement("table");
	    whichtbl.id=rndtbl;
            alert("whichtbl= "+whichtbl);
	    alert("new table id is "+whichtbl.id);
	    tbody=document.createElement("tbody");
	    whichtbl.appendChild(tbody);
	    whichform.appendChild(whichtbl);
	    addhdr(whichtbl, rnd);
        }
        var firstteam = document.getElementById('TeamNum1').value;
        var secondteam = document.getElementById('TeamNum2').value;
        if((ret = arehere(whichtbl, firstteam, secondteam)) == -1)
        {
	    //
	    //create a new table row, no match exists for this pair of teams
	    //
            rowcnt=document.getElementById(rndtbl).getElementsByTagName('tr').length;
            x=document.getElementById(rndtbl).insertRow(rowcnt);
        }
        else
        {
	    //
	    // this matchup pair already exists in the specified round
	    //
            var x=document.getElementById(rndtbl).rows[ret];
        }
        // if teams already on page, change those cells, else add new cells
        if(ret > -1)
        {
            y=x.cells[0];
            z=x.cells[1];
            c=x.cells[2];
            d=x.cells[3];
            g=x.cells[4];
        }
        else
        {
            y=x.insertCell(0);
            z=x.insertCell(1);
            c=x.insertCell(2);
            d=x.insertCell(3);
            g=x.insertCell(4);
        }
	// set the table values from the form
        y.innerHTML=document.getElementById('TeamNum1').value;
	var alt = document.getElementById('roundNum').value % 2;
    	var nav = navigator.appName;
	if(nav == "Microsoft Internet Explorer")
	{
	    if(alt == 1)
	    {
                y.setAttribute("className", "teamplay1");
                c.setAttribute("className", "teamplay1");
	    }
	    else
	    {
	        y.setAttribute("className", "teamplay2");
                c.setAttribute("className", "teamplay2");
	    }
        }
        else // firefox case
        {
	    if(alt == 1)
	    {
               y.style.fontSize="10pt";
	       y.style.color="blue";
	       y.style.fontFamily="Verdana, Arial, Helvetica, sans-serif";
	       y.style.backgroundColor="yellow";
               c.style.fontSize="10pt";
	       c.style.color="blue";
	       c.style.fontFamily="Verdana, Arial, Helvetica, sans-serif";
	       c.style.backgroundColor="yellow";
	    }
	    else
	    {
               y.style.fontSize="10pt";
               y.style.color="yellow";
	       y.style.fontFamily="Verdana, Arial, Helvetica, sans-serif";
	       y.style.backgroundColor="blue";
               c.style.fontSize="10pt";
               c.style.color="yellow";
	       c.style.fontFamily="Verdana, Arial, Helvetica, sans-serif";
	       c.style.backgroundColor="blue";
	    }
        }
        z.innerHTML=document.getElementById('TeamScore1').value;
//        z.setAttribute("className", "teamplay3");
	z.style.fontSize="10pt";
	z.style.fontFamily="Verdana, Arial, Helvetica, sans-serif";
        c.innerHTML=document.getElementById('TeamNum2').value;
        d.innerHTML=document.getElementById('TeamScore2').value;
//        d.setAttribute("className", "teamplay3");
	d.style.fontSize="10pt";
	d.style.fontFamily="Verdana, Arial, Helvetica, sans-serif";
        g.innerHTML="<input type=\"checkBox\" name=\"erase\" onclick=\"chkDel(this)\"/>";
	// clear the form
        document.getElementById('roundNum').value = "";
        document.getElementById('TeamNum1').value = "";
        document.getElementById('TeamScore1').value = "";
        document.getElementById('TeamNum2').value = "";
        document.getElementById('TeamScore2').value ="";
        document.getElementById('roundNum').focus();
    }
}
//
// add header info into a new match round table of the form
//
function addhdr(tbl, num)
{
    var nav = navigator.appName;
//    if(nav == "Microsoft Internet Explorer")
//    {
//    }
    var y = document.getElementById(tbl.id);
    var arow=document.getElementById(tbl.id).insertRow(0);
    var lblhdr = document.createElement("tr");
//
// will need to check for IE/Mozilla for this className setup
//
    lblhdr.setAttribute("className", "whdr");
    lbltxt="";
//
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
    var p = document.getElementById("roundNum");
    p.focus();
}
//
//
//
function arehere(tbl, first, second)
{
    var ret=-1;
    var huh =tbl.getElementsByTagName('tr').length;
    for(var k=2; k < tbl.getElementsByTagName('tr').length; k++)
    {
        var celz=tbl.rows[k].cells;
        if((tbl.rows[k].cells[0].innerHTML == document.getElementById("TeamNum1").value) &&
	(tbl.rows[k].cells[2].innerHTML == document.getElementById("TeamNum2").value))
        {
           ret = k;
        }
    }
    return ret;
}
//
// delete a row from the table
//
function chkDel(what)
{
    var targ=what.parentNode.parentNode;
    var oarg=targ.parentNode;
    oarg.removeChild(targ);
    var p = document.getElementById("roundNum");
    p.focus();
}
//
// check the values in the input fields
//
function chkVals()
{
    var ret=false;
    var p = document.getElementById("roundNum");
    var t1 = document.getElementById("TeamNum1");
    var s1 = document.getElementById("TeamScore1");
    var t2 = document.getElementById("TeamNum2");
    var s2 = document.getElementById("TeamScore2");
    var pos = p.value.search(/[1-9][0-9]*/);
    var pos1 = t1.value.search(/[1-9][0-9]*/);
    var pos2 = s1.value.search(/[1-9][0-9]*/);
    var pos3 = t2.value.search(/[1-9][0-9]*/);
    var pos4 = s2.value.search(/[1-9][0-9]*/);
    if (pos != 0)
    {
	alert("Invalid Round Number");
        p.focus();
	p.select();
        ret=false;
    }
    else if (pos1 != 0)
    {
	alert("Team number must be numeric and not zero");
        t1.focus();
	t1.select();
        ret=false;
    }
    else if (pos2 != 0 && s1.value != "")
    {
	alert("Score number must be numeric, without leading zeroes");
        s1.focus();
	s1.select();
        ret=false;
    }
    else if (pos3 != 0)
    {
	alert("Team number must be numeric and not zero");
        t2.focus();
	t2.select
        ret=false;
    }
    else if (pos4 != 0 && s2.value != "")
    {
	alert("Score number must be numeric, without leading zeroes");
        s2.focus();
	s2.select();
        ret=false;
    }
    else
    {
	ret=true;
    }
    return ret;
}
