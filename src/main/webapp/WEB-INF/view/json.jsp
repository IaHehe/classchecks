<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Springmvc json数据交互</title>
</head>
<body>
	<h2>json.jsp</h2>
	
	<button id="btn_json">获取json数据</button>
	<div id="data-show"></div>
	<br><br>
	<h3>使用Servlet API 的HttpServletRequest</h3>
  <form action="<%= request.getContextPath() %>/index/do-upload" method="POST" enctype="multipart/form-data">  
    <input type="file" name="file"/>  
    <input type="submit" value="上传" />  
  </form>
  <h2>js模拟表单上传文件</h2>
  <input type="file" id="input-file"> <button id="btn_upload">上传</button>
  <div id="file-data-show"></div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath }/res/js/jquery.min.js"></script>
<script type="text/javascript">

	var file = null;
	$('#input-file').on('change', function() {
		this.files.length != 0 && (file = this.files[0]);
	});
	
	$('#btn_upload').on('click', function() {
		var dataInfo = {
			'name': 	'zoudongjun',
			'password':	'123',
			'number'  : '2013440020',
			'college.collegeCode': '5001',
			'department.departmentCode': '50010',
			'classes.classesCode': '500100'
		};
		
		if(file != null) {
			var form = new FormData();
			form.append('file', file);
			form.append('username', 'zoudongjun');
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/index/do-upload',
				data: form,
				cache: false,
			    processData: false,
			    contentType: false,
				success: function(data) {
					$('#file-data-show').html('这是请求json后返回的数据:'+JSON.stringify(data));
				}
			});
		}else {
			alert("要上传的文件为null");
		}
	});


	$('#btn_json').on('click', function() {
		var dataInfo = {
				username: 'cqust',
				password: 'admin123'
		};
		$.ajax({
			type: 'POST',
			url: '${pageContext.request.contextPath}/index/login',
			data: JSON.stringify(dataInfo),
			contentType: 'application/json;charset=utf-8',
			dataType: 'json',
			success: function(data) {
				$('#data-show').html('这是请求json后返回的数据:'+JSON.stringify(data));
			}
		});
	});
</script>
</html>