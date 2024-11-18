function showAlert(message, alertType, contentElement) {
    var alert = $("#" + alertType);
    alert.find(contentElement).text(message);
    alert.removeClass("hidden");
    alert.animate({width: "700px", height: "70px"}, 500, function() {
        setTimeout(function() {
            alert.animate({width: "0"}, 500, function() {
                alert.addClass("hidden");
            });
        }, 2000);
    });
}
$(document).ready(function() {
    var message = $('#message').val();
    if (message !== null && message.trim() !== "") {
        showAlert(message, "successAlert", "span");
    }
});
