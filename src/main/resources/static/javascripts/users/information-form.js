$(document).ready(function () {
    var isCheckGenderChoose = $('#isCheckGenderChoose').val();
    if (isCheckGenderChoose === 'false') {
    $('input[type=radio][name=gender]').prop('checked', false);
}
});
$(document).ready(function () {
    $('#registerForm').submit(function (event) {
        var isGenderSelected = $('input[type=radio][name=gender]:checked').length > 0;

        if (!isGenderSelected) {
            $('#checkTick').val(false);
        }
    });
});
