<%@ page import="org.essobedo.lc.service.Robot"%>
<%
boolean debug = false;
String frequencyValue = request.getParameter("f");
int frequency = 2000;
boolean frequencySet = false;
if (frequencyValue != null) {
  try {
    frequency = Integer.parseInt(frequencyValue);
    frequencySet = true;
  } catch(Exception e) {}
}
String qualityValue = request.getParameter("q");
boolean qualitySet = false;
if (qualityValue != null) {
  try {
    qualityValue = Float.toString(Float.parseFloat(qualityValue));
    qualitySet = true;
  } catch(Exception e) {
    qualityValue = "null";
  }
} else {
  qualityValue = "null";
}
%>
<html>
<head>
<style type="text/css">
body {
  display: block;
  padding: 9.5px;
  margin: 0 0 4px;
  font-size: 13px;
  background-color: #f5f5f5;
  border: 1px solid #ccc;
  border: 1px solid rgba(0, 0, 0, 0.15);
  -webkit-border-radius: 4px;
     -moz-border-radius: 4px;
          border-radius: 4px;
}
</style>
<script type="text/javascript">
<!--
var refreshFrequency = <%=frequency%>;
var quality = <%=qualityValue%>;
<%if (!frequencySet || !qualitySet) {%>
var mobileDevice = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
if (!mobileDevice) {
  // If no mobile device has been detected, we set better default values
  <%if (!frequencySet) {%>
  refreshFrequency = 1000;
  <%}%>
  <%if (!qualitySet) {%>
  quality = 1.0;
  <%}%>
}
<%}%>
var hash = "";
function FindPosition(oElement) {
  if(typeof( oElement.offsetParent ) != "undefined") {
    for(var posX = 0, posY = 0; oElement; oElement = oElement.offsetParent) {
      posX += oElement.offsetLeft;
      posY += oElement.offsetTop;
    }
    return [ posX, posY ];
  } else {
      return [ oElement.x, oElement.y ];
  }
}
function GetCoordinates(e) {
  var PosX = 0;
  var PosY = 0;
  var ImgPos;
  ImgPos = FindPosition(myImg);
  if (!e) var e = window.event;
  if (e.pageX || e.pageY) {
    PosX = e.pageX;
    PosY = e.pageY;
  } else if (e.clientX || e.clientY) {
      PosX = e.clientX + document.body.scrollLeft
        + document.documentElement.scrollLeft;
      PosY = e.clientY + document.body.scrollTop
        + document.documentElement.scrollTop;
  }
  ImgPos[0] = PosX - ImgPos[0];
  ImgPos[1] = PosY - ImgPos[1];
  return ImgPos;
}
var action;
var lastTouch;
var pressTimer;
var clickTimeout = 600;
function OnMouseUp(e) {
  clearTimeout(pressTimer);   // clear the timeout
}
function OnMouseDown(e) {
  pressTimer = setTimeout(function(evt){
    document.getElementById("contentInput").focus();
  }, clickTimeout);
}
var totalClicks = 0;
function OnClick(e) {
  ++totalClicks;
  var now = new Date().getTime();
  lastTouch = lastTouch || now + 1;
  var delta = now - lastTouch;
  clearTimeout(action);
  lastTouch = now;
  if(delta < clickTimeout && delta > 0) {
    if (totalClicks > 2) {
      triggerClick(e, 1, true);
      document.getElementById("contentInput").focus();
    } else {
      action = setTimeout(function(evt){
        triggerClick(evt, 2);
        clearTimeout(action);   // clear the timeout
      }, clickTimeout, e);
    }
  } else {
    action = setTimeout(function(evt){
      triggerClick(evt, 1);
      clearTimeout(action);   // clear the timeout
    }, clickTimeout, e);
  }
}
function triggerClick(e,clicks, setContentArea) {
  totalClicks = 0;
  var ImgPos = GetCoordinates(e);
  var PosX = ImgPos[0];
  var PosY = ImgPos[1];
  if (setContentArea) {
    contentarea.style.top=PosY;
    contentarea.style.left=PosX;
    contentarea.style.width=0;
    contentarea.style.height=0;
    contentarea.style.opacity=0;
  }
  var dc = "";
  if (clicks > 1) {
    dc = "&t=1";
  }
  var rc = "";
  if (document.getElementById("rightClick").checked) {
    document.getElementById("rightClick").checked = false;
    rc="&b=1";
  }
  callAction("c?p=" + PosX + "," + PosY + dc + rc);
}
var loadingStartTime = null;
function callAction(url, maxDuration, callback){
  var xmlhttp;
  var status;
  if (window.XMLHttpRequest){ // code for IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp=new XMLHttpRequest();
  } else {// code for IE6, IE5
    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
  xmlhttp.onreadystatechange=function() {
    if (xmlhttp.readyState==4) {
      status = xmlhttp.status;
      <%if (debug) {%>
      document.getElementById("Status").innerHTML=status;
      <%}%>
      if(callback != null && typeof callback == 'function') {
        callback(xmlhttp);
      }
      if (status == 200 && ((/^u/).test(url) || !loadingStartTime)) {
        reloadImg();
      }
    }
  }
  <%if (debug) {%>
  document.getElementById("Req").innerHTML=url;
  <%}%>
  xmlhttp.open("GET",url,true);
  xmlhttp.send();
  if (maxDuration && maxDuration > 0) {
    setTimeout(function(){
      if (!status) {
        xmlhttp.abort();
        if(callback != null && typeof callback == 'function') {
          callback();
        }
      }
    },maxDuration);

  }
}
function reloadImg() {
  var q = "";
  if (quality) {
    q = "&q=" + quality;
  }
  loadingStartTime = new Date().getTime();
  myImg.src = "s?r=" + loadingStartTime + q;
}
var maxDuration4UQuery = 2000;
function updateHash(xmlhttp) {
  if (!xmlhttp) {
    beforeQuery = null;
    return;
  }
  if (xmlhttp.status = 200 && xmlhttp.responseText && xmlhttp.responseText != "") {
    hash = xmlhttp.responseText;
  }
  var time = beforeQuery;
  if (!beforeQuery)
    return;
  var totalTime = new Date().getTime() - beforeQuery;
  beforeQuery = null;
  if (totalTime > refreshFrequency) {
    var freq = Math.round((totalTime + refreshFrequency) * 0.005) * 100;
    if (freq > maxDuration4UQuery) {
      freq = maxDuration4UQuery;
    }
    SetRefreshFrequency(freq);
  }
}
function OnPress(event) {
  var input = document.getElementById("contentInput");
  input.value = "";
  var key = event.which || event.keyCode; // event.keyCode is used for IE8 and earlier versions
  if (key == 13)
    key = 10;
  if (key == 173 || key == 189)
    key = 109;
  callAction("h?c=" + key);
}
function OnEnter(e) {
  callAction("h?c=10");
}
function getOffset( el ) {
  var _x = 0;
  var _y = 0;
  while( el && !isNaN( el.offsetLeft ) && !isNaN( el.offsetTop ) ) {
    _x += el.offsetLeft - el.scrollLeft;
    _y += el.offsetTop - el.scrollTop;
    el = el.offsetParent;
  }
  return { top: _y, left: _x };
}
function resetInput() {
  var top = 10;
  contentarea.style.top=top;
  var left = 185;
  if (/iPhone/i.test(navigator.userAgent) && /Safari/i.test(navigator.userAgent)) {
    left = 265;
  }
  contentarea.style.left=left;
  contentarea.style.width=220;
  contentarea.style.height=25;
  contentarea.style.opacity=1;
}
var refreshTimer;
var beforeQuery;
var canRefresh = false;
function SetRefreshFrequency(frequency) {
  if (frequency < 500) {
    frequency = 500;
  }
  refreshFrequency = frequency;
  document.getElementById("Freq").innerHTML=frequency;
  if (refreshTimer) {
    clearInterval(refreshTimer);
  }
  canRefresh = false;
  refreshTimer = window.setInterval(function() {
    if (!canRefresh) {
      canRefresh = true;
      return;
    }
    var q = "";
    if (quality) {
      q = "&q=" + quality;
    }
    if (!beforeQuery) {
      beforeQuery = new Date().getTime();
      callAction("u?h=" + hash + q, maxDuration4UQuery, updateHash);
    }
  }, frequency);
}
function OnImgLoaded() {
  error = 0;
  if (!loadingStartTime)
    return;
  var totalTime = new Date().getTime() - loadingStartTime;
  loadingStartTime = null;
  var delta = (totalTime - refreshFrequency)
  var threshold = refreshFrequency * 0.15;
  if (delta <= -threshold || delta >= threshold) {
    var freq = Math.round((totalTime + refreshFrequency) * 0.005) * 100;
    var q = null;
    if (freq < 500) {
      q = quality;
      if (!q) {
        q = <%=Robot.SCREEN_SHOT_DEFAULT_QUALITY%>;
      }
      q += 0.1;
    } else if (freq > 2000) {
      q = quality;
      if (!q) {
        q = <%=Robot.SCREEN_SHOT_DEFAULT_QUALITY%>;
      }
      q -= 0.1;
    }
    if (q) {
      if (q > 1.0) {
        q = 1.0;
      } else if (q < 0.1) {
        q = 0.1;
      }
      quality = q;
      document.getElementById("Qua").innerHTML=quality + "F";
    }
    SetRefreshFrequency(freq);
  }
}
var error = 0;
function OnImgError() {
  error++;
  if (error > 2) {
    return;
  }
  reloadImg();
}
window.addEventListener('DOMContentLoaded', function() {
  var input = document.getElementById("contentInput");
  var focus = function(e) {
    e.stopPropagation();
    e.preventDefault();
    var clone = input.cloneNode(true);
    var parent = input.parentElement;
    parent.appendChild(clone);
    parent.replaceChild(clone, input);
    input = clone;
    window.setTimeout(function() {
      input.value = input.value || "";
      input.focus();
    }, 0);
  }
}, false);
//-->
</script>
</head>
<body>
<p>Right Click: <input id="rightClick" type="checkbox" value="false"/> Enter Content: </p>
<img src="s" width="<%=Robot.SCREEN_DIMENSION.width%>" height="<%=Robot.SCREEN_DIMENSION.height%>" alt="" id="screenshot" onload="OnImgLoaded()" onerror="OnImgError()"/>
<div id="contentarea" style="position: absolute"><input id="contentInput" type="text" onkeydown="OnPress(event)" onblur="resetInput()"/> <button id="enter" onclick="OnEnter(event)">OK</button></div>
<script type="text/javascript">
<!--
var myImg = document.getElementById("screenshot");
myImg.onclick = OnClick;
myImg.onmouseup = OnMouseUp;
myImg.onmousedown = OnMouseDown;
var contentarea = document.getElementById("contentarea");
resetInput();
//-->
</script>
<p>Refresh frequency: <span id="Freq"></span> <img src="img/info.gif" alt="To modify the default frequency reload the page and add ?f=&lt;new frequency value&gt; at the end of the URL" title="To modify the default frequency reload the page and add ?f=&lt;new frequency value&gt; at the end of the URL"/> Image quality: <span id="Qua"></span> <img src="img/info.gif" alt="To modify the default quality reload the page and add ?q=&lt;new quality value&gt; at the end of the URL" title="To modify the default quality reload the page and add ?q=&lt;new quality value&gt; at the end of the URL"/>
<script type="text/javascript">
<!--
if (!quality) {
  document.getElementById("Qua").innerHTML="<%=Robot.SCREEN_SHOT_DEFAULT_QUALITY%>F";
} else {
  document.getElementById("Qua").innerHTML=quality + "F";
}
SetRefreshFrequency(refreshFrequency);
//-->
</script>
</p>
<%if (debug) {%>
<p>Last request: <span id="Req"></span> Status of the last request: <span id="Status"></span></p>
<%}%>
</body>
</html>