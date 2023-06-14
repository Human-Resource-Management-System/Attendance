<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload Employee Attendance</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <h2>Upload Employee Attendance</h2>
    <form id="uploadForm" enctype="multipart/form-data">
        <input type="file" id="fileInput" accept=".xlsx, .xls">
        <br><br>
        <input type="submit" value="Upload">
    </form>

    <script>
        $(document).ready(function() {
            $('#uploadForm').submit(function(e) {
                e.preventDefault();
                var formData = new FormData();
                formData.append('file', $('#fileInput')[0].files[0]);
                $.ajax({
                    url: "uploadAttendance",
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function(response) {
                        // Handle success response
                        console.log(response);
                    },
                    error: function(xhr, status, error) {
                        // Handle error
                        console.log(error);
                    }
                });
            });
        });
    </script>
</body>
</html>