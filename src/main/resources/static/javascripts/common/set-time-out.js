document.addEventListener('DOMContentLoaded', function() {
    var errorMessageElement = document.getElementById('errorMessage');
    if (errorMessageElement) {
        setTimeout(function() {
            errorMessageElement.style.display = 'none';
        }, 5000);
    }

    var successMessageElement = document.getElementById('successMessage');
    if (successMessageElement) {
        setTimeout(function() {
            successMessageElement.style.display = 'none';
        }, 5000);
    }
});

