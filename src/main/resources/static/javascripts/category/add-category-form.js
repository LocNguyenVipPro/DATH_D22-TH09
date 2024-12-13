$(document).ready(function () {

    function ShowMessageErrr(title, message) {
        $('#Title').text(title);
        $('#Message').text(message);
        $('#AlertMessage').removeClass('hidden');
        // Hide the alert after 10 seconds
        setTimeout(function() {
            $('#AlertMessage').addClass('hidden');
        }, 10000);
    }
    $('#closeBtn').click(function() {
        $('#AlertMessage').addClass('hidden');
    });

    $('#submitForm').on('submit', function (e) {
        let name = $('#name').val().trim();
        let slug = $('#slug').val().trim();
        let id = $('#id').val().trim();
        let check = false;
        $.ajax({
            url: "/category/check-slug-name-uni",
            method: "GET",
            data: {
                id:id,
                name:name,
                slug:slug
            },
            success: function(data) {
                if (data !== "ok") {
                    ShowMessageErrr("Name or Slug!", "Exist Please choose another Name Or Slug.");
                    check = true;
                }
            },
            async: false
        });

        if (check) {
            e.preventDefault();
        }
    });
        $('#image').on('change', function (e) {
            if (event.target.files.length > 0) {
                var src = URL.createObjectURL(event.target.files[0]);
                var thumbnail = document.getElementById("thumbnail");
                thumbnail.src = src;
                thumbnail.style.display = "block";
            }
        });
});

