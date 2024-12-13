function ShowMessageError(title, message) {
    $('#Title').text(title);
    $('#Message').text(message);
    $('#AlerMessage').removeClass('hidden');
    setTimeout(function() {
        $('#AlerMessage').addClass('hidden');
    }, 10000);
}

$('#closeBtn').click(function() {
    $('#AlerMessage').addClass('hidden');
});