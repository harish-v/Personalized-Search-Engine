//====================================================
	//Home

	function onLoadHome() {
		//checkCookie
		var user_pk = getName("user_pk");
		if (user_pk == null || user_pk == "")
                    ip_reg();
	}
	function ip_reg() {
		var xhrip = new XMLHttpRequest();

		var baseurl = "/nr/api/auth/ip_register";
		var url = baseurl;

		xhrip.onreadystatechange = function () {
			if (xhrip.readyState == XMLHttpRequest.DONE) {
				var response = JSON.parse(xhrip.responseText);

				if (response.status == 'SUCCESS') {
					createUser(response.out);
				} else {
					dispMessage("Cannot Connect !");
				}
			}
		};

		xhrip.open("GET", url, true);
		xhrip.send();
	}

	function closeLoginDiv() {
		document.getElementById("loginDiv").style.display = "none";
		document.getElementById("prefDiv").style.display = "none";
	}
	function button_userPref() {
		document.getElementById("loginDiv").style.display = "none";
		document.getElementById("prefDiv").style.display = "inline";

	}
	function button_userLogin() {
//		document.getElementById("iFrame2").src = "signUp.html";
		document.getElementById("prefDiv").style.display = "none";
		document.getElementById("loginDiv").style.display = "inline";
	}
	function button_userLogout() {
		//clearCookie
		deleteUser();
//		document.getElementById("iFrame2").src = "iFrame3.html";
		document.getElementById("loginDiv").style.display = "none";
		document.getElementById("prefDiv").style.display = "none";
		document.getElementById("b_logout").display = 'none';
		alert("LogOut Successful !");
	}

	function loadUserPrefAPI()
	{
		var xhr = new XMLHttpRequest();
		
				var baseurl = "/nr/api/news";
				//var urlparams = "?query=" + encodeURIComponent(query);
				var url = baseurl + urlparams;
		
				xhr.onreadystatechange = function () {
					if (xhr.readyState == XMLHttpRequest.DONE) {
						var response = JSON.parse(xhr.responseText);
							loadPref(response);
					}
		
				};
				xhr.open("GET", url, true);
				xhr.send();
	}
	
	
	function loadPref(data)
	{
		for( i=0 ; i < data.count ; i++)
		{
			var pref = data.preferences[i];

			var snippetHTML = "<h5 id='"+pref+"' onclick='onPrefClick("+pref+");'>"+pref+"</h5>";
			
			var tag = document.getElementById("result");
			tag.innerHTML += "<li>" +snippetHTML+ "</li>";
		}
	}
	
	function onPrefClick(pref)
	{
			var flag;
			var color = document.getElementById(pref).style.color;
			if(color === 'yellow')
			{
				document.getElementById(pref).style.color = 'red';
				flag = true;
			}
			else
			{
				document.getElementById(pref).style.color = 'yellow';
				flag = false;
			}
			sendBackNewsPref(pref,flag);
	}
	
	function sendBackNewsPref(pref,flag)
		{
					var xhrip = new XMLHttpRequest();

					var baseurl = "/nr/api/";
					var urlparams = "?pref=" + encodeURIComponent(pref);
					var url = baseurl+urlparams;
			
					xhrip.onreadystatechange = function () {
						if (xhrip.readyState == XMLHttpRequest.DONE) {
							var response = JSON.parse(xhrip.responseText);
			
							if (response.status == 'SUCCESS') {
								createUser(response.out);
							} else {
								dispMessage("Cannot Connect !");
							}
						}
					};
			
					xhrip.open("GET", url, true);
					xhrip.send();
				
		}
		
//====================================================
//Home Suggest

function onKeyPressSuggest()
{
	var partialQuery = document.getElementById("searchBar").value;
	suggestAPI(partialQuery);
}

function suggestAPI(partialQuery) {
		var xhr = new XMLHttpRequest();

		var baseurl = "/nr/api/news";
		var urlparams = "?query=" + encodeURIComponent(partialQuery);
		var url = baseurl + urlparams;

		xhr.onreadystatechange = function () {
			if (xhr.readyState == XMLHttpRequest.DONE) {
				var response = JSON.parse(xhr.responseText);
					suggestSpellingDisplay(response);
			}

		};
		xhr.open("GET", url, true);
		xhr.send();
	}

	var sugg = [];
	
function suggestSpellingDisplay(data)
{
	for( i=0 ; i < data.count ; i++)
	{
		sugg.push(data.suggessions[i]);
	}
							
}


//JQuery - Autosuggest
//	$(function() {
//	    $( "#searchBar" ).autocomplete({
//	      source: sugg
//	    });
//	  });




//====================================================
//Home Search

var querySave;

	function button_search() {
		var query = document.getElementById("searchBar").value;
    querySave = query;
		search(query);
	}

	function search(query) {
		var xhr = new XMLHttpRequest();

		var baseurl = "/nr/api/news";
		var urlparams = "?query=" + encodeURIComponent(query);
		var url = baseurl + urlparams;
					
		xhr.onreadystatechange = function () {
			if (xhr.readyState == XMLHttpRequest.DONE) {
				var response = JSON.parse(xhr.responseText);
				//	console.log(response);
					displaySearchResults(response);
			}

		};
		xhr.open("GET", url, true);
		xhr.send();
	}
	
	function displaySearchResults(data)
	{
    var tag = document.getElementById("result");
    tag.innerHTML ="";

		for( i=0 ; i < data.news_results.length ; i++)
		{			
				//console.log(data.news_results[i]);
				var news_id = data.news_results[i].news_id;
				var title = data.news_results[i].title;
				var author = data.news_results[i].authors;
				var published_date = data.news_results[i].published_date;
				var place = data.news_results[i].place;
				var url = data.news_results[i].url;
				var publisher = data.news_results[i].publisher;
				var content = data.news_results[i].content;
									
				var snippetHTML = "";
														
				snippetHTML += "<div>";
				snippetHTML += "<h3><a href='javascript:void(0);' onclick='onSearchItemClick(\""+news_id+"\")'>"+title+"</a></h3>";
                                if(author!="")
				{snippetHTML += "<h5>"+author+"</h5>";}
				snippetHTML += "<h5>"+published_date+"</h5>";
				snippetHTML += "<h5>"+place+"</h5>";
				snippetHTML += "<h5>" +url+ "</h5>";
				snippetHTML += "<h5>"+publisher+"</h5>";
				snippetHTML += "<p>"+content+"<p>";
				snippetHTML += "</div>";
									
				var tag = document.getElementById("result");
				tag.innerHTML += "<li>" +snippetHTML+ "</li>";
		}
	}
				
	
	function onSearchItemClick(news_id)
	{	
		sendBackNewsID(news_id);	
	}
	
	function sendBackNewsID(news_id)
	{
				var xhrip = new XMLHttpRequest();

				var baseurl = "/nr/api/news/";
				var urlparams = news_id;
				var url = baseurl + urlparams;
		
				xhrip.onreadystatechange = function () {
					if (xhrip.readyState == XMLHttpRequest.DONE) {
						var response = JSON.parse(xhrip.responseText);
						displayOneNewsItem(response);
					}
				};
		
				xhrip.open("GET", url, true);
				xhrip.send();
	}
	
	function displayOneNewsItem(data)
	{
		//console.log("gdgd"+data.news_results.length);
		
						var news_id = data.news_id;
						var title = data.title;
						var author = data.authors;
						var published_date = data.published_date;
						var place = data.place;
						var url = data.url;
						var publisher = data.publisher;
						var content = data.content;
											
						var snippetHTML = "";
																
						snippetHTML += "<div>";
						snippetHTML += "<label onclick='search(\""+querySave+"\")'>< Back to Search Results</label><hr/>";
						snippetHTML += "<h3>"+title+"</h3>";
						snippetHTML += "<h5>"+author+"</h5>";
						snippetHTML += "<h5>"+published_date+"</h5>";
						snippetHTML += "<h5>"+place+"</h5>";
						snippetHTML += "<a href=" +url+ ">" +url+ "</a>";
						snippetHTML += "<h5>"+publisher+"</h5>";
						snippetHTML += "<p>"+content+"<p>";
						snippetHTML += "</div>";
											
						var tag = document.getElementById("result");
						tag.innerHTML = "<li>" +snippetHTML+ "</li>";
				
	}
	
	//====================================================
	//Login

	function signUpFB() {
		checkLoginState();
	}
	function signUp() {
		var xhrup = new XMLHttpRequest();

		var email1 = document.getElementById("email1").value;
		var pass1 = document.getElementById("pass1").value;
		var pass2 = document.getElementById("pass2").value;

		if(email1 === null || pass1 === null || pass2 === null
		|| email1 === "" || pass1 === "" || pass2 === ""	)
		{
			dispMessageLoginScreen("Fields Missing !");
		}
		else if (pass1 != pass2)
		{
			dispMessageLoginScreen("Password Mismatch !");
		}
		else
		{
			var baseurl = "/nr/api/auth/signup";
			var urlparams = "?user_id=" + encodeURIComponent(email1)
								  	+ "&password=" + encodeURIComponent(pass1);
			var url = baseurl + urlparams;

			xhrup.onreadystatechange = function () {
				if (xhrup.readyState == XMLHttpRequest.DONE) {
					var response = JSON.parse(xhrup.responseText);

					if (response.status == 'SUCCESS') {
						createUser(response.out);
					} else {
						dispMessageLoginScreen(response.out);
					}
				}

			};
			xhrup.open("GET", url, true);
			xhrup.send();
		}
	}

	function signIn() {
		var xhrin = new XMLHttpRequest();

		var email = document.getElementById("email").value;
		var pass = document.getElementById("pass").value;

		if(email === null || pass === null
		|| email === "" || pass === "")
		{
			dispMessageLoginScreen("Fields Missing !");
		}	
		else 
		{
		var baseurl = "/nr/api/auth";
		var urlparams = "?user_id=" + encodeURIComponent(email)
									+ "&password=" + encodeURIComponent(pass);
		var url = baseurl + urlparams;

		xhrin.onreadystatechange = function () {
			if (xhrin.readyState == XMLHttpRequest.DONE) {
				var response = JSON.parse(xhrin.responseText);

				if (response.status == 'SUCCESS') {
					createUser(response.out);
				} else {
					dispMessageLoginScreen(response.out);
				}
			}
		};
		xhrin.open("GET", url, true);
		xhrin.send();
		///nr/api/auth?user_id=san.gct&password=hello
		//var url = "http://url.php?id=login&email="+encodeURIComponent(email)+"&password="+encodeURIComponent(password);
		//window.location.href = url;
		//http://username:password@example.com/
	}
	}

	function dispMessageLoginScreen(message) {
		//document.getElementById("messageLabel").innerHTML = message;
		//document.getElementById("userLabel").innerHTML = message;
		//console.log(document.getElementById("messageLabel").textContent);
	}

	//====================================================
	//Cookie

	function createUser(out) {
		var user_pk = out;
		setName("user_pk", user_pk, 10);
		//document.cookie = "user_pk=asdasdawesdasd; expires=Fri, 31 Dec 9999 23:59:59 GMT";
	}

	function deleteUser() {
		setName("user_pk", "", -1);
	}

	//ref- http://www.w3schools.com/js/js_cookies.asp
	function checkName(name) {
		var username = getCookie(name);
		if (username != "") {
			alert("Welcome again " + username);
		} else {
			username = prompt("Please enter your name:", "");
			if (username != "" && username != null) {
				setCookie("username", username, 1);
			}
		}
	}

	function setName(cname, cvalue, exdays) {
		var d = new Date();
		d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
		var expires = "expires=" + d.toUTCString();
		document.cookie = cname + "=" + cvalue + "; " + expires;
	}

	function getName(cname) {
		var name = cname + "=";
		var ca = document.cookie.split(';');
		for (var i = 0; i < ca.length; i++) {
			var c = ca[i];
			while (c.charAt(0) == ' ')
				c = c.substring(1);
			if (c.indexOf(name) != -1)
				return c.substring(name.length, c.length);
		}
		return "";
	}

//====================================================

	function tog_signPref() {

		var d = document.getElementById("iFrame1");

		if (d.style.display === "inline")
			d.style.display = "none";
		else
			d.style.display = "inline";

	}
	
//====================================================	
