document.addEventListener('DOMContentLoaded', function() {
    // Function to move focus to the next input
    function moveToNextInput(currentInput, nextInput) {
        if (currentInput.value.length === currentInput.maxLength) {
            nextInput.focus();
        }
    }

    // Event listeners to move focus to the next input
    document.getElementById('digit1').addEventListener('input', function () {
        moveToNextInput(this, document.getElementById('digit2'));
    });
    document.getElementById('digit2').addEventListener('input', function () {
        moveToNextInput(this, document.getElementById('digit3'));
    });
    document.getElementById('digit3').addEventListener('input', function () {
        moveToNextInput(this, document.getElementById('digit4'));
    });
    document.getElementById('digit4').addEventListener('input', function () {
        moveToNextInput(this, document.getElementById('digit5'));
    });
    document.getElementById('digit5').addEventListener('input', function () {
        moveToNextInput(this, document.getElementById('digit6'));
    });

    // Check if the error message exists and show it
    var errorMessage = $('#errMessage').val();
    if (errorMessage) {
        showAlert(errorMessage, "failAlert", "span");
    }

    // Form submission handler
    $('#submitButton').submit(function(event) {
        event.preventDefault();

        //Get value in input form
        let digit1 = parseInt($('#digit1').val());
        let digit2 = parseInt($('#digit2').val());
        let digit3 = parseInt($('#digit3').val());
        let digit4 = parseInt($('#digit4').val());
        let digit5 = parseInt($('#digit5').val());
        let digit6 = parseInt($('#digit6').val());

        //Get Token
        let token = '' + digit1 + digit2 + digit3 + digit4 + digit5 + digit6;

        //Check valid Token
        let invalidCharacter = false;
        if (isNaN(digit1) || isNaN(digit2) || isNaN(digit3) || isNaN(digit4) || isNaN(digit5) || isNaN(digit6)) {
            invalidCharacter = true;
        }

        //Handle valid form and invalid form
        if (invalidCharacter) {
            var message = "Invalid Character! Please enter only numeric characters.";
            showAlert(message, "failAlert", "span");
        } else {
            $('#token').val(token);
            $(this).unbind('submit').submit();
        }
    });
});
