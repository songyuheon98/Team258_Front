$(document).ready(function() {
    $('#login-btn').on('click', function() {
        const data = {
            username: $('#username').val(),
            password: $('#password').val()
        };

        // 로그인 버튼 비활성화 (중복 요청 방지)
        $(this).attr('disabled', 'disabled');

        $.ajax({
            type: 'POST',
            url: '/api/user/login',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('로그인 성공!');
                window.location.href = "/";
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    alert('아이디 또는 비밀번호가 잘못되었습니다.');
                } else {
                    alert('로그인 실패!');
                }
                window.location.href = "/login";
            },
            complete: function() {
                /**
                 * 요청이 완료된 후 로그인 버튼을 다시 활성화
                  */
                $('#login-btn').removeAttr('disabled');
            }
        });
    });
});

function moveSignUpView(){
    window.location.href = '/signup';
}
