<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Punch Data</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            // AJAX call to retrieve punch data
            $.ajax({
                url: "punchData",
                type: "GET",
                dataType: "json", // Specify the data type as JSON
                success: function(response) {
                    // Handle the response data here
                    console.log(response); // Print the response data to the console for testing

                    // Iterate over the response data and process each item
                    for (var i = 0; i < response.length; i++) {
                        var eventData = response[i];
                        var time = eventData.time;
                        var event = eventData.event;

                        // Create a new row in the table to display the punch data
                        var newRow = $("<tr>");
                        $("<td>").text(time).appendTo(newRow);
                        $("<td>").text(event).appendTo(newRow);
                        newRow.appendTo("#punchTable");
                    }
                },
                error: function(xhr, status, error) {
                    // Handle the error case
                    console.log(error);
                }
            });
        });
    </script>
</head>
<body>
    <h1>Punch Data</h1>
    <table id="punchTable">
        <thead>
            <tr>
                <th>Time</th>
                <th>Event</th>
            </tr>
        </thead>
        <tbody>
            <!-- Punch data will be dynamically added here -->
        </tbody>
    </table>
</body>
</html>
