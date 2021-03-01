<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.assessment.data.*, java.text.*, java.util.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
 <title>eAssess</title>
      <link href="images/E-assess_E.png" rel="shortcut icon">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Open+Sans+Condensed:wght@300&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="css_new/bootstrap.min.css">
    <link rel="stylesheet" href="css_new/styles_new.css">
    <link href="css/responsive.css" rel="stylesheet" type="text/css">
    <link href="css/font-awesome_new.css" rel="stylesheet" type="text/css">
    <link href="css/responsive_new.css" rel="stylesheet" type="text/css">
    <link href="css_new/pnotify.custom.min.css" rel="stylesheet" type="text/css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>
    	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/7.33.1/sweetalert2.css">
</head>
<body class="pre-login">
    <header>
        <div class="container">
            <h1 class="text-center">
                <img src="images_new/yaksha.png" width="250px" alt="EAssess Online"> is ready to ask <br>
                <span>what you already know best!</span>
            </h1>
            <h2>
                ${studentTestForm.testName}
                <!-- <div class="company">
                    ${testUserData.user.companyName}
                </div> -->
            </h2>
            <div class="start-test">
                <form:form id="studentIntro" method="POST" action="preStudentJourney"  modelAttribute="studentTestForm">
                    <ul>
                        <li>
                            Total No. of questions :
                        </li>
                        <li>
                            ${studentTestForm.totalQuestions}
                        </li>
                    </ul>
                    <ul>
                        <li>
                            Duration :
                        </li>
                        <li>
                            ${studentTestForm.duration} Minutes
                        </li>
                    </ul>
                    <ul>
                        <li>
                            Published On :
                        </li>
                        <li>
                            ${studentTestForm.formattedPublishedDate}
                        </li>
                    </ul>
					<ul>
                        <li>
						<%
							Integer noOfAttempts = (Integer) request.getSession().getAttribute("noOfAttempts");
								System.out.println("att "+noOfAttempts);
								
								if(noOfAttempts == null){
									noOfAttempts = 0;
									
								}
								else{
									noOfAttempts = noOfAttempts + 1;
								}
						%>
                            Current Attempt : 
                        </li>
                        <li>
                           <%= noOfAttempts %> 
                        </li>
                    </ul>
                    <ul>
                        <li>
                            Should be completed Within :
                        </li>
                        <li>
                            ${studentTestForm.noOfAttemptsAvailable} attempt(s)
                        </li>
                    </ul>
                    <div class="mt-2 mb-3 user-actions">
                        <div class="warning-msg col-orange">
                            <i class="material-icons">
                                warning
                            </i>
                            <span>Read the instructions bellow before starting the assessment</span>
                        </div>
                        <div class="start">
							<c:choose>
								<c:when test="${practice != null && practice == true}">
									 <button type="button" class="btn btn-primary" onClick="start2()">Start Practice</button>
									<br />
								</c:when>    
								<c:when test="${test.webProctoring}">
									 <button type="button" class="btn btn-primary" onClick="checkCamera()" id="cam">Check Web Cam</button>
									<br />
								</c:when>    
								<c:otherwise>
									 <button type="button" class="btn btn-primary" onClick="start2()" id="sss">Start Assessment</button>
									<br />
								</c:otherwise>
							</c:choose>
						
						
                           
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <a href="javascript:void" class="contact-us">
            reachus@eassess.com
        </a>
    </header>
    <section>
        <div class="container">
			<ul>
				<li class="mb-3">
					<h3 class="mb-2">
						instructions
					</h3>
					<ul>
						<li class="mb-2">
							Test Results will be sent to you on Completion
						</li>
						<li class="mb-2">
							Click Submit for Submission of your Test
						</li>
						<li class="mb-2">
							System will auto Submit Test if Timer Expires
						</li>
					</ul>
				</li>
				<li class="mb-3">
					<h3 class="mb-2">
						web proctoring
					</h3>
					<ul>
						<li class="mb-2">
							Do not move mouse pointer to a different tab
						</li>
						<li class="mb-2">
							Use F11 windows for Test if required
						</li>
						<li class="mb-2">
							Non Compliance can result in your Test Declared Invalid
						</li>
					</ul>
				</li>
			</ul>
			<ul>
				<li class="mb-3">
					<h3 class="mb-2">
						tenants
					</h3>
					<ul>
						<li class="mb-2">
							Domain specific Users are advise to login using Corporate Credentials
						</li>
						<li class="mb-2">
							Every User is directed to provide Login data for Individual Reporting
						</li>
						<li class="mb-2">
							Tenant Admin will see Tenant specfic data only
						</li>
					</ul>
				</li>
				<li class="mb-3">
					<h3 class="mb-2">
						eAssess
					</h3>
					<ul>
						<li class="mb-2">
							Multi Technology Assessments
						</li>
						<li class="mb-2">
							Test Cases Based Evaluation
						</li>
						<li class="mb-2">
							Weighted Adaptive Assessments
						</li>
					</ul>
				</li>
			</ul>
		</div>
    </section>
    <footer>
		<div class="container pt-3 pb-3">
			<span>
				&copy; Copyright 2020 :: eAssess :: Powered by eAssess
			</span>
			<a href="javascript:void(0)" class="mr-5">
				Terms and Conditions
			</a>
			<a href="javascript:void(0)">
				Privacy Policy
			</a>
		</div>
	</footer>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="scripts/custom.js"></script>
<script type="text/javascript" src="scripts/pnotify.custom.min.js"></script>
<script type="text/javascript" src="scripts/pnotify.nonblock.js"></script>
<script type="text/javascript" src="scripts/pnotify.buttons.js"></script>
<script src="scripts/src-min-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
<script>
    var randomNumber = Math.floor(Math.random() * (6 - 1 + 1)) + 1;
    $('header').attr('style', "background-image: url(images_new/banner_" + randomNumber + ".jpg);");
    /*   function handleClick(cb) {
          if(cb.checked){
             document.getElementById("startTest").disabled = false;
          }
          else{
          document.getElementById("startTest").disabled = true;
          }
     } */
     
     function start2(){
         //fullScreen();
         document.getElementById("studentIntro").submit();
     }
     
 	function checkCamera(){

			navigator.getMedia = ( navigator.getUserMedia || // use the proper vendor prefix
		            navigator.webkitGetUserMedia ||
		            navigator.mozGetUserMedia ||
		            navigator.msGetUserMedia);
		
				navigator.getMedia({video: true}, function() {
					sweetAlert("Info","You can start test Now","success")
					$("#cam").attr("onclick","start2()");
					$("#cam").text("Start Assessment");
				}, function() {
					sweetAlert("Information","You can not start test without camera","warning")
//							alert("Test cannot start without camera");
		});	
		}
     
    
    function fullScreen(){
        $("#cls").click()
        var elem = document.documentElement;
        if (elem.requestFullscreen) {
            elem.requestFullscreen();
        } else if (elem.mozRequestFullScreen) { /* Firefox */
            elem.mozRequestFullScreen();
        } else if (elem.webkitRequestFullscreen) { /* Chrome, Safari and Opera */
            elem.webkitRequestFullscreen();
        } else if (elem.msRequestFullscreen) { /* IE/Edge */
            elem.msRequestFullscreen();
        }
    } 
    
    function sweetAlert(msgtype,message,icon){
		  Swal.fire(
			       msgtype,
			       message,
			       icon
			    )
		}
</script>
</html>