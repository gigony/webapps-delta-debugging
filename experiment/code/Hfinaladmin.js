//
// finaladmin.js - javascript associated with the finaladmin.html XHTML page
// Author: Wayne Motycka
// Date: Mar 25, 2008
// CS464 Spring 08
// HW 4
//
// global row counters for the tables
var rowcntA;
var rowcntB;
var rowcntF;
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
        if(document.getElementById("sftype").checked==true)  // semi-final round selected
        {
            // determine which pool, A or B in semifinals
	    //
            var what = document.getElementById('PoolGroup');
            if(what.value=="A" || what.value=="a")
	    {
                var whichtbl = document.getElementById('semiA');
                var firstteam = document.getElementById('TeamNum1').value;
                var secondteam = document.getElementById('TeamNum2').value;
                if((ret = arehere(whichtbl, firstteam, secondteam)) == -1)
                {
                    rowcntA=document.getElementById('semiA').getElementsByTagName('tr').length;
                    x=document.getElementById('semiA').insertRow(rowcntA);
                }
                else
                {
                    var x=document.getElementById('semiA').rows[ret];
                }
            }
            else if (what.value=="B" || what.value=="b")
	    {
                rowcntB=document.getElementById('semiB').getElementsByTagName('tr').length;
                x=document.getElementById('semiB').insertRow(rowcntB);
	    }
        }
        else  // final round selected
        {
            var whichtbl = document.getElementById('finals');
            var firstteam = document.getElementById('TeamNum1').value;
            var secondteam = document.getElementById('TeamNum2').value;
            if((ret = arehere(whichtbl, firstteam, secondteam)) == -1)
            {
                rowcntF=document.getElementById('finals').getElementsByTagName('tr').length;
                x=document.getElementById('finals').insertRow(rowcntF);
            }
	    else
            {
                var x=document.getElementById('finals').rows[ret];
            }
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
        z.innerHTML=document.getElementById('TeamScore1').value;
        c.innerHTML=document.getElementById('TeamNum2').value;
        d.innerHTML=document.getElementById('TeamScore2').value;
        g.innerHTML="<input type=\"checkBox\" name=\"erase\" onclick=\"chkDel(this)\"/>";
	// clear the form
        document.getElementById('PoolGroup').value = "";
        document.getElementById('TeamNum1').value = "";
        document.getElementById('TeamScore1').value = "";
        document.getElementById('TeamNum2').value = "";
        document.getElementById('TeamScore2').value ="";
        document.getElementById('PoolGroup').focus();
    }
}
//
//
//
function arehere(tbl, first, second)
{
    var ret=-1;
    for(var k=1; k < tbl.getElementsByTagName('tr').length; k++)
    {
        celz=tbl.rows[k].cells;
        if((celz[0].innerHTML == document.getElementById('TeamNum1').value) &&
               celz[2].innerHTML == document.getElementById('TeamNum2').value)
        {
           ret = k;
        }
    }
    return ret;
}
//
// add the headers to the tables executed at page load
//
function addhdr()
{
    var nav = navigator.appName;
    if(nav == "Microsoft Internet Explorer")
    {
        var at = document.getElementById('semiA');
	at.setAttribute("width", 200);
	var bt =  document.getElementById('semiB');
	bt.setAttribute("width", 200);
	var ft =  document.getElementById('finals');
	ft.setAttribute("width", 200);
    }
    var arow=document.getElementById('semiA').insertRow(0);
    var brow=document.getElementById('semiB').insertRow(0);
    var frow=document.getElementById('finals').insertRow(0);
    var hd1a=arow.insertCell(0);
    var hd2a=arow.insertCell(1);
    var hd3a=arow.insertCell(2);
    var hd4a=arow.insertCell(3);
    var hd5a=arow.insertCell(4);
    var hd1b=brow.insertCell(0);
    var hd2b=brow.insertCell(1);
    var hd3b=brow.insertCell(2);
    var hd4b=brow.insertCell(3);
    var hd5b=brow.insertCell(4);
    var hd1f=frow.insertCell(0);
    var hd2f=frow.insertCell(1);
    var hd3f=frow.insertCell(2);
    var hd4f=frow.insertCell(3);
    var hd5f=frow.insertCell(4);
    hd1a.innerHTML="<b>Team</b>";
    hd2a.innerHTML="<b>Score</b>";
    hd3a.innerHTML="<b>Team</b>";
    hd4a.innerHTML="<b>Score</b>";
    hd5a.innerHTML="<b>Delete</b>";
    hd1b.innerHTML="<b>Team</b>";
    hd2b.innerHTML="<b>Score</b>";
    hd3b.innerHTML="<b>Team</b>";
    hd4b.innerHTML="<b>Score</b>";
    hd5b.innerHTML="<b>Delete</b>";
    hd1f.innerHTML="<b>Team</b>";
    hd2f.innerHTML="<b>Score</b>";
    hd3f.innerHTML="<b>Team</b>";
    hd4f.innerHTML="<b>Score</b>";
    hd5f.innerHTML="<b>Delete</b>";
    document.getElementById('PoolGroup').focus();
}
//
// delete a row from the table
//
function chkDel(what)
{
    var targ=what.parentNode.parentNode;
    var oarg=targ.parentNode;
    oarg.removeChild(targ);
    var p = document.getElementById("PoolGroup");
    p.focus();
}
//
// check the values in the input fields
//
function chkVals()
{
    var ret=false;
    var p = document.getElementById("PoolGroup");
    var t1 = document.getElementById("TeamNum1");
    var s1 = document.getElementById("TeamScore1");
    var t2 = document.getElementById("TeamNum2");
    var s2 = document.getElementById("TeamScore2");
    var pos = p.value.search(/[aAbB]/);
    var pos1 = t1.value.search(/[1-9][0-9]*/);
    var pos2 = s1.value.search(/[1-9][0-9]*/);
    var pos3 = t2.value.search(/[1-9][0-9]*/);
    var pos4 = s2.value.search(/[1-9][0-9]*/);
    if (pos != 0 && document.getElementById("sftype").checked)
    {
	alert("Valid pool is A or B");
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
    else if (pos2 != 0 && s1.value!="")
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
    else if (pos4 != 0 && s2.value!="")
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
