
function ShowMessageErrr(title, message) {
    $('#Title').text(title);
    $('#Message').text(message);
    $('#AlerMessage').removeClass('hidden');
    // Hide the alert after 10 seconds
    setTimeout(function() {
        $('#AlerMessage').addClass('hidden');
    }, 10000);
}
$('#closeBtn').click(function() {
    $('#AlerMessage').addClass('hidden');
});

    $('#registerForm').submit(function(event) {
        let id=$('#id').val();
        if(id==='0'||id==='')
        {
            let password = $('#password').val();
            let repassword = $('#repassword').val();
            if (password !== repassword) {
                ShowMessageErrr("Password not Match!", "Please make sure your passwords match.");
                event.preventDefault();
            }
        }
});