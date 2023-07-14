document.getElementById("expenseForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent form submission

    // Get form data
    var date = document.getElementById("dateInput").value;
    // Retrieve other input field values for amount, category, and description

    // Create an AJAX request to send the expense data to the server
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/recordExpense", true);
    xhr.setRequestHeader("Content-Type", "application/json");

    // Convert form data to JSON
    var expenseData = JSON.stringify({
        date: date,
        // Other expense properties
    });

    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Successful response, update UI or display a message
            } else {
                // Error occurred, handle accordingly
            }
        }
    };

    xhr.send(expenseData);
});

// Add more JavaScript code to interact with the UI and handle other functionality
