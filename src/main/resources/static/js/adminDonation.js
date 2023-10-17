$(document).ready(function() {

    // 책 나눔 이벤트 생성
    function createDonationEvent(eventData) {
        $.ajax({
            type: 'POST',
            url: '/api/admin/donation',
            data: JSON.stringify(eventData),
            contentType: 'application/json',
            success: function(response) {
                alert(response.message);
                location.reload();
            },
            error: function(error) {
                alert('기부 이벤트 생성에 실패했습니다.');
            }
        });
    }

    // 책 나눔 이벤트 업데이트
    function updateDonationEvent(donationId, eventData) {
        $.ajax({
            type: 'PUT',
            url: '/api/admin/donation/' + donationId,
            data: JSON.stringify(eventData),
            contentType: 'application/json',
            success: function(response) {
                alert(response.message);
                location.reload();
            },
            error: function(error) {
                alert('책 나눔 이벤트 업데이트에 실패했습니다.');
            }
        });
    }

    // 책 나눔 이벤트 삭제
    window.deleteEvent = function(donationId) {
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/donation/' + donationId,
            success: function(response) {
                alert(response.message);
                location.reload();
            },
            error: function(error) {
                alert('책 나눔 이벤트 삭제에 실패했습니다.');
            }
        });
    }


});
