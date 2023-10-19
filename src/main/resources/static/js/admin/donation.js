$(document).ready(function() {

    window.createDonationEvent = function() {
        var data = {
            createdAt: $('input[name="createdAt"]').last().val(),
            closedAt: $('input[name="closedAt"]').last().val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/admin/donation',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('이벤트 생성 성공!');
                window.location.href = '/admin/donation/v2';
            },
            error: function(xhr, status, error) {
                alert('이벤트 생성 실패!');
                window.location.href = '/admin/donation/v2';
            }
        });
    };

    window.updateDonationEvent = function(donationId) {
        var row = $('input[name="donationId"][value="' + donationId + '"]').closest('tr');
        var data = {
            createdAt: row.find('input[name="createdAt"]').val(),
            closedAt: row.find('input[name="closedAt"]').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/api/admin/donation/' + donationId,
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('이벤트 업데이트 성공!');
                window.location.href = '/admin/donation/v2';
            },
            error: function(xhr, status, error) {
                alert('이벤트 업데이트 실패!');
                window.location.href = '/admin/donation/v2';
            }
        });
    };

    window.deleteDonationEvent = function(donationId) {
        if(confirm('정말로 이 이벤트를 삭제하시겠습니까?')) {
            $.ajax({
                type: 'DELETE',
                url: '/api/admin/donation/' + donationId,
                contentType: 'application/json;charset=UTF-8',
                dataType: 'json',
                success: function(response) {
                    alert('이벤트 삭제 성공!');
                    window.location.href = '/admin/donation/v2';
                },
                error: function(xhr, status, error) {
                    alert('이벤트 삭제 실패!');
                    window.location.href = '/admin/donation/v2';
                }
            });
        }
    };

    window.endDonationEvent = function(donationId) {
        if(confirm('정말로 이 이벤트를 삭제하시겠습니까?')) {
            $.ajax({
                type: 'DELETE',
                url: '/api/admin/donation/end/' + donationId,
                contentType: 'application/json;charset=UTF-8',
                dataType: 'json',
                success: function(response) {
                    alert('이벤트 종료 성공!');
                    window.location.href = '/admin/donation/v2';
                },
                error: function(xhr, status, error) {
                    alert('이벤트 종료 실패!');
                    window.location.href = '/admin/donation/v2';
                }
            });
        }
    };

    window.setDonationEvent = function() {
        var selectedBooks = [];
        $('.book-checkbox:checked').each(function() {
            selectedBooks.push($(this).val());
        });

        if (selectedBooks.length === 0) {
            alert('설정할 책을 선택해주세요.');
            return;
        }

        var data = {
            donationId: $('#donationId').val(),
            bookIds: selectedBooks.map(Number)
        };

        $.ajax({
            type: 'PUT',
            url: '/api/admin/donation/setting',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('이벤트 설정 성공!');
                window.location.href = '/admin/donation/v2';
            },
            error: function(xhr, status, error) {
                window.location.href = '/admin/donation/v2';
                alert('이벤트 설정 실패!');
            }
        });
    };
    window.bookApplyCancle = function(donationId, bookId) {
        const requestData = {
            donationId: donationId,
            bookId: bookId
        };

        $.ajax({
            type: 'PUT',
            url: '/api/admin/donation/settingCancel',
            contentType: 'application/json',
            data: JSON.stringify(requestData),
            success: function (response) {
                alert('책 취소 설정이 완료되었습니다.');
                window.location.reload();  // 현재 페이지 새로고침
            },
            error: function (error) {
                alert('책 취소 설정에 실패하였습니다.');
            }
        });
    };
});

