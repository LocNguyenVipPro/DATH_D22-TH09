    $(document).ready(function () {
        var checkError = document.getElementById("checkError");
        if (checkError) {
            $('#thumbnail2').removeClass('hidden');
            $('#thumbnail').addClass('hidden');
        }

        function ShowMessageErrr(title, message) {
            $('#Title').text(title);
            $('#Message').text(message);
            $('#AlertMessage').removeClass('hidden');
            // Hide the alert after 10 seconds
            setTimeout(function () {
                $('#AlertMessage').addClass('hidden');
            }, 10000);
        }

        $('#closeBtn').click(function () {
            $('#AlertMessage').addClass('hidden');
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
