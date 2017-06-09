<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>学生注册，参数封装死的</h2>
	<input type="file" value="选择图片并上传" id="input-file">  <button id="btn_upload">点击上传</button>
	<div id="data-show"></div>
	<hr>

	<h2>教师上传图片考勤</h2>
	<div>
		教师教务账号：<input type="text" value="2001018" id="teacher-jw-account"> <br><br>
		教师注册账号：<input type="text" value="13301236543" id="teacher-register-account"> <br><br>
		课堂照片：<input type="file" value="选择图片并上传" id="clock-in-img"> <br><br>
		<button id="btn_clockin">确认考勤</button>
	</div>
	<div id="clock-msg-show"></div>
	<hr>
	
	<h2>学生上传图片考勤</h2>
	<div>
		学生教务账号：<input type="text" value="2015440493" id="stu-jw-account"> <br><br>
		学生注册账号：<input type="text" value="18996316514" id="stu-register-account"> <br><br>
		
		经度：<input type="text" value="106.318943" id="stu-lng"> <br><br>
		纬度：<input type="text" value="29.595745" id="stu-lat"> <br><br>
		课堂照片：<input type="file" value="选择图片并上传" id="stu-clock-in-img"> <br><br>
		<button id="btn_clock_stu">确认考勤</button>
	</div>
	<div id="stu-btn_clockin_stu"></div>
	<hr>
</body>

<script type="text/javascript" src="<%= request.getContextPath()%>/res/js/jquery.min.js"></script>
<script>
	
	$(function() {
		
		var stu_img_file = null;
		$('#stu-clock-in-img').on('change', function(){
			this.files.length != 0 && (stu_img_file = this.files[0]);
		});
		$('#btn_clock_stu').on('click', function() {
			if(stu_img_file == null) {
				alert("没有选择考勤图片");
				return;
			}
			if($('#stu-jw-account').val() == '' || $('#stu-jw-account').val() == null) {
				alert("学生账号为空");
				return;
			}
			
			var form = new FormData();
			form.append('jwAccount', $('#stu-jw-account').val());
			form.append('loginAccount', $('#stu-register-account').val());
			form.append('clockinImg', stu_img_file);
			form.append('lng', $('#stu-lng').val());
			form.append('lat', $('#stu-lat').val())
			
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/student/clock-in',
				data: form,
				cache: false,
			    processData: false,
			    contentType: false,
				success: function(data) {
					$('#stu-btn_clockin_stu').html('这是学生拍照考勤请求后返回的数据:'+JSON.stringify(data));
				}
			});
		});
		
		
		var clockin_img_file = null;
		$('#clock-in-img').on('change', function() {
			this.files.length != 0 && (clockin_img_file = this.files[0]);
		});
		
		$('#btn_clockin').on('click', function(){
			if(clockin_img_file == null) {
				alert("没有选择考勤图片");
				return;
			}
			if($('#teacher-jw-account').val() == '' || $('#teacher-jw-account').val() == null) {
				alert("教务账号为空");
				return;
			}
			var form = new FormData();
			form.append('jwAccount', $('#teacher-jw-account').val());
			form.append('phone', $('#teacher-register-account').val());
			form.append('clockinImg', clockin_img_file);
			
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/teacher/clock-in',
				data: form,
				cache: false,
			    processData: false,
			    contentType: false,
				success: function(data) {
					$('#clock-msg-show').html('这是教师拍照考勤请求后返回的数据:'+JSON.stringify(data));
				}
			});
			
		});
		
		// ===================================================================
		var file = null;
		$('#input-file').on('change', function() {
			this.files.length != 0 && (file = this.files[0]);
		});

		$('#btn_upload').on('click', function() {
			
			if(file != null) {
				var form = new FormData();
				form.append('files', file);
				form.append('phone', '18302390780');
				form.append('smscode', '036541');
				from.append('regID', '102da542s51f6t5b6l1d');

				$.ajax({
					type: 'POST',
					url: '${pageContext.request.contextPath}/student/register',
					data: form,
					cache: false,
				    processData: false,
				    contentType: false,
					success: function(data) {
						$('#data-show').html('这是请求json后返回的数据:'+JSON.stringify(data));
					}
				});
			}else {
				alert("要上传的文件为null");
			}
		});
	});
	
</script>
</html>