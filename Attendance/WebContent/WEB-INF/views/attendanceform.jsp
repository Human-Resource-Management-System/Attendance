<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upload Employee Attendance</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha2/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<h2 class="mt-4">Upload Employee Attendance</h2>
		<form id="uploadForm" enctype="multipart/form-data" class="mt-4">
			<div class="mb-3">
				<label for="fileInput" class="form-label">Choose Excel File</label>
				<input type="file" class="form-control" id="fileInput"
					accept=".xlsx, .xls">
			</div>
			<button type="submit" class="btn btn-primary">Upload</button>
		</form>
	</div>

	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha2/js/bootstrap.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#uploadForm').submit(function(e) {
				e.preventDefault();
				var formData = new FormData();
				formData.append('file', $('#fileInput')[0].files[0]);
				$.ajax({
					url : "uploadAttendance",
					type : 'POST',
					data : formData,
					processData : false,
					contentType : false,
					success : function(response) {
						// Handle success response
						console.log(response);
					},
					error : function(xhr, status, error) {
						// Handle error
						console.log(error);
					}
				});
			});
		});
	</script>
</body>
</html>
