<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

<h2>图片上传页面</h2>

<input type="file" value="选择图片并上传" id="input-file">  <button id="btn_upload">点击上传</button>
<div id="data-show"></div>
</body>
<script type="text/javascript" src="<%= request.getContextPath()%>/res/js/jquery.min.js"></script>
<script>

	var file = null;
	$('#input-file').on('change', function() {
		this.files.length != 0 && (file = this.files[0]);
	});

	$('#btn_upload').on('click', function() {
		// 模拟表单提交
		if(file != null) {
			var form = new FormData();
			form.append('file', file);
			form.append('name', 'zoudongjun');
			form.append('password', '123');
			form.append('number', '2013440020');
			form.append('college.collegeCode', '5001');
			form.append('department.departmentCode', '50010');
			form.append('classes.classesCode', '500100');
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/image/do-upload',
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
</script>
</html>