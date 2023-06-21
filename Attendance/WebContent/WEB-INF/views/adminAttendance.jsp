<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Attendance Details</title>
<style>
/* Add modern CSS styles for formatting */
.empattendance {
	font-family: Arial, sans-serif;
	background-color: #f2f2f2;
	margin: 0;
	padding: 20px;
}

.attendance-container {
	max-width: 600px;
	margin: 0 auto;
	background-color: #fff;
	padding: 20px;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

h1 {
	color: #333;
	text-align: center;
	margin-top: 0;
}

h2 {
	color: #555;
}

.attendance-info {
	margin-bottom: 20px;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	padding: 10px;
	text-align: center;
	border-bottom: 1px solid #ddd;
}

th {
	background-color: #f2f2f2;
	color: #333;
}

.month-select {
	width: 150px; /* Adjust the width as needed */
	margin-bottom: 10px;
	appearance: none;
	background-color: #f2f2f2;
	border: none;
	padding: 10px;
	font-size: 14px;
	color: #333;
	border-radius: 5px;
}

.month-select option {
	background-color: #fff;
}

.month-select:focus {
	outline: none;
	box-shadow: 0 0 3px rgba(0, 0, 0, 0.2);
}

.select-container {
	position: relative;
}

/* Remove the triangles on the right side */
.select-container::before,
.select-container::after {
	content: none;
}

.minimum-hours {
	margin-top: 10px;
	font-size: 14px;
	color: #555;
}

</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
  
  
   <div class="empattendance">  

	<div class="attendance-container">
		<h1>Attendance Details</h1>

		<div class="attendance-info">
			<h2>Employee ID:</h2>
			<div class="select-container">
				<input type="text" id="employeeIdInput" class="month-select" placeholder="Enter Employee ID">
			</div>
			
			<div id="msg"></div>
			
			<h2>Year:</h2>
			<div class="select-container">
				<select id="yearSelect" class="month-select">

				</select>
			</div>

			<h2>Month:</h2>
			<div class="select-container">
				<select id="monthSelect" class="month-select">
					<option value="1">January</option>
					<option value="2">February</option>
					<option value="3">March</option>
					<option value="4">April</option>
					<option value="5">May</option>
					<option value="6">June</option>
					<option value="7">July</option>
					<option value="8">August</option>
					<option value="9">September</option>
					<option value="10">October</option>
					<option value="11">November</option>
					<option value="12">December</option>
				</select>
			</div>
			<p class="minimum-hours">
				Minimum Working Hours per Day: <span id="minimumHours"></span>
			</p>
		</div>

		<table id="attendanceTable">
		</table>
	</div>

	<script>
		function updateTableData() {
			var selectedYear = $("#yearSelect").val();
			var selectedMonth = $("#monthSelect").val();
			var employeeId = $("#employeeIdInput").val();

			// Update minimum working hours per day
			$("#minimumHours").text(8);

			console.log(selectedYear);
			console.log(selectedMonth);
			console.log(employeeId);
			var table = $("#attendanceTable");

			$.ajax({
			    url: "attendance",
			    type: "POST",
			    data: {"year": selectedYear, "month": selectedMonth, "employeeid": employeeId},
			    contentType: "application/x-www-form-urlencoded",
			    dataType:"json",
			    success: function(response) {
			        console.log(response);
			        var template = "<tr>" +
		            "<th>Days with Minimum Hours</th>" +
		            "<th>Total Days</th>" +
		            "<th>Attendance Percentage</th>" +
		            "</tr>" +
		            "<tr>" +
		            "<td>" + response.dayswithminhrs + "</td>" +
		            "<td>" + response.totaldays + "</td>" +
		            "<td>" + response.percentage + "%</td>" +
		            "</tr>";
		        
		        table.html(template);
			    },
			    error: function(error) {
			        console.log(error);
			    }
			});
		}

		// Add event listeners to the year, month, and employee ID input
		$("#yearSelect").change(updateTableData);
		$("#monthSelect").change(updateTableData);
		$("#employeeIdInput").on("input", getYearsList);

		
		function getYearsList(){
			
			var employeeId = $("#employeeIdInput").val();
			
			$("#msg").empty();
			
			$.ajax({
				url:"getYearsList",
				type:"POST",
				data: {employeeid:employeeId},
				success:function(response){
					console.log(response);
					var yearsList = JSON.parse(response);
					var selectElement = $('#yearSelect'); 
					  selectElement.empty();
					  for (var i = 0; i < yearsList.length; i++) {
					    var year = yearsList[i];
					    var optionElement = $('<option>').val(year).text(year);
					    selectElement.append(optionElement);
					  }
					
				},
				error:function(error){
					var selectElement = $('#yearSelect'); 
					  selectElement.empty();
					$("#msg").html("No Employee Found");
				}
			
			});
		}
		
		
	</script>
	
	</div>
</body>
</html>
