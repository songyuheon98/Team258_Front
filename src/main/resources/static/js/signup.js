$(document).ready(function() {
    $('#signup-button').on('click', function() {
        var form = $('#signup-form');
        var data = {
            username: form.find('input[name="username"]').val(),
            password1: form.find('input[name="password1"]').val(),
            password2: form.find('input[name="password2"]').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/users/signup',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('회원가입 성공!');
                window.location.href = "/login";
            },
            error: function(xhr, status, error) {
                alert('회원가입 실패!'); // 오류 메시지는 실제 API 응답에 따라 조절할 수 있습니다.
                window.location.href = "/signup";
            }
        });
    });
});
