function handleFormSubmission(message, callback) {
    var dialogConfirmed = false;
    const messageElement = document.getElementById('messageDialog');
    messageElement.textContent = message;
    if (!dialogConfirmed) {
        $('#confirmationDialog').removeClass('hidden');
    } else {
        $(this).off('submit').submit();
    }
    $('#confirmButton').click(function() {
        dialogConfirmed = true;
        callback(true);
    });
    callback(false);
}

function hideConfirmationDialog() {
    $('#confirmationDialog').addClass('hidden');
}