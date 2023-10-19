$(document).ready(function () {
    window.deleteBookApplyDonation = function(applyId) {
        $.ajax({
            type: 'DELETE',
            url: '/api/user/bookApplyDonation/' + applyId,
            success: function (response) {
                alert('나눔 신청 취소가 완료되었습니다.');
                window.location.reload(); // 페이지 새로고침
            },
            error: function (error) {
                alert('나눔 신청 취소에 실패하였습니다.');
                window.location.reload(); // 페이지 새로고침
            }
        });
    };
});
