<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upload Employee Attendance</title>
<!-- CSS styles -->
<style>
    .attendancecontainer {
        max-width: 800px;
        margin: 40px auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 4px;
        background-color: #FFFAF4;
    }

    h2 {
        color: #333;
        margin-top: 0;
    }

    .form-label {
        font-weight: bold;
    }

    .form-control {
        margin-bottom: 10px;
    }

    .download-section {
        margin-top: 40px;
    }

    .download-button {
        display: inline-block;
        padding: 10px 20px;
        font-size: 16px;
        font-weight: bold;
        text-align: center;
        text-decoration: none;
        color: #fff;
        background-color: #4F709C;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .info-message {
        margin-top: 20px;
        color: #555;
    }

    /* Styles for the file input */
    .file-input-label {
        display: inline-block;
        padding: 8px 12px;
        font-size: 14px;
        font-weight: normal;
        color: #555;
        background-color: #fff;
        border: 1px solid #ccc;
        border-radius: 4px;
        cursor: pointer;
    }

    .file-input-label:hover {
        background-color: #f0f0f0;
    }

    .file-input-label::after {
        content: 'Browse';
        display: inline-block;
        padding: 6px 12px;
        margin-left: 10px;
        font-size: 14px;
        font-weight: bold;
        color: #fff;
        background-color: #4F709C;
        border-radius: 4px;
    }

    /* Hide the actual file input element */
    .file-input {
        display: none;
    }

    .attend-success-msg {
        color: #609966;
        font-weight: bold;
    }

    .attend-error-msg {
        color: 	#ff7b7b;
        font-weight: bold;
    }
    
    
</style>
</head>
<body>
    <div class="attendancecontainer">
        <h2>Upload Employee Attendance</h2>
        <form id="uploadForm" enctype="multipart/form-data">
            <div>
                <label for="fileInput" class="form-label">Choose Excel File</label>
                <label for="fileInput" class="file-input-label">Select File</label>
                <input type="file" class="form-control file-input" id="fileInput" accept=".xlsx, .xls">
            </div>
            <button type="submit" id="upload" class="btn btn-primary download-button">Upload</button>
        </form>
        <div id="uploadStatus"></div>
    </div>

    <div class="download-section attendancecontainer">
        <h2>Need More Info About Excel File? Download The Excel Template File</h2>
        <button id="downloadButton" class="download-button">Download Excel Template</button>
        <div id="info" class="info-message"></div>
    </div>

    <!-- Library to create the excel files  -->
    <script src="https://unpkg.com/xlsx/dist/xlsx.full.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
    $(document).ready(function() {
        $('#uploadForm').submit(function(e) {
            e.preventDefault();

            if (fileInput.files.length === 0) {
                // No file selected, display an error message or perform appropriate action
                $("#uploadStatus").addClass('attend-error-msg').text('Please select a file.');
                return;
            }

            // Show loading indicator
            $('#upload').html('Uploading...');

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
                    $('#uploadStatus').removeClass().addClass('attend-success-msg').text('File uploaded successfully.');
                    // Hide loading indicator
                    $('.download-button').html('Upload');
                },
                error: function(xhr, status, error) {
                    // Handle error
                    console.log(error);
                    $('#uploadStatus').removeClass().addClass('attend-error-msg').text('Error uploading file.');
                    // Hide loading indicator
                    $('.download-button').html('Upload');
                }
            });
        });

        $('#fileInput').on('change', function() {
            var fileName = $(this).val().split('\\').pop();
            $('.file-input-label').text(fileName);
        });

        $('#downloadButton').click(function() {
            // Create a new workbook
            var workbook = XLSX.utils.book_new();

            // Define column names
            var columnNames = ["empl_id", "punchin (DateTime)", "punchout (DateTime)", "punchsystem"];

            // Create a new worksheet
            var worksheet = XLSX.utils.aoa_to_sheet([columnNames]);

            // Add the worksheet to the workbook
            XLSX.utils.book_append_sheet(workbook, worksheet, "EmployeeAttendance");

            // Convert the workbook to a binary Excel file
            var excelData = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

            // Create a Blob from the Excel data
            var blob = new Blob([excelData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });

            // Create a download link
            var downloadLink = document.createElement('a');
            downloadLink.href = URL.createObjectURL(blob);
            downloadLink.download = 'employee_attendance.xlsx';

            // Simulate a click on the download link
            downloadLink.click();

            // Display additional information
            var infoElement = $('#info');
            infoElement.text('The Excel Template file has been generated and is ready for download. Please note that the "punchin" and "punchout" columns represent DateTime values.');
        });
    });

    </script>
</body>
</html>
