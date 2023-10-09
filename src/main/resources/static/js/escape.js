$(document).ready(function () {
    $('#escape-button').on('click', function () {
        $.ajax({
            type: 'DELETE',
            url: '/api/user/escape',
            success: function () {
                alert('탈퇴가 완료되었습니다.');
                window.location.href = '/';
            },
            error: function () {
                alert('탈퇴에 실패하였습니다.');
                window.location.href = '/escape';
            }
        });
    });

    $('#main-page-button').on('click', function () {
        window.location.href = '/main';
    });
});
