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
                window.location.href = '/admin/donation';
            },
            error: function(xhr, status, error) {
                alert('이벤트 생성 실패!');
                window.location.href = '/admin/donation';
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
                window.location.href = '/admin/donation';
            },
            error: function(xhr, status, error) {
                alert('이벤트 업데이트 실패!');
                window.location.href = '/admin/donation';
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
                    window.location.href = '/admin/donation';
                },
                error: function(xhr, status, error) {
                    alert('이벤트 삭제 실패!');
                    window.location.href = '/admin/donation';
                }
            });
        }
    };

});




//
    // // 책 나눔 이벤트 업데이트
    // function updateDonationEvent(donationId, eventData) {
    //     $.ajax({
    //         type: 'PUT',
    //         url: '/api/admin/donation/' + donationId,
    //         data: JSON.stringify(eventData),
    //         contentType: 'application/json;charset=UTF-8',
    //         dataType: 'json',
    //         success: function(response) {
    //             alert(response.message);
    //             location.reload();
    //         },
    //         error: function(xhr, status, error) {
    //             alert('책 나눔 이벤트 업데이트에 실패했습니다.');
    //         }
    //     });
    // }
    //
    // // 책 나눔 이벤트 업데이트 버튼 클릭
    // $(document).on('click', 'button.btn-outline-warning', function() {
    //     const donationId = $(this).closest('tr').find("input[name='donationId']").val();
    //     const row = $(this).closest('tr');
    //     const createdAt = row.find("input[name='createdAt']").val();
    //     const closedAt = row.find("input[name='closedAt']").val();
    //
    //     if (!createdAt || !closedAt) {
    //         alert('시작 시간과 종료 시간을 입력해주세요.');
    //         return;
    //     }
    //
    //     const eventData = {
    //         createdAt: createdAt,
    //         closedAt: closedAt
    //     };
    //
    //     updateDonationEvent(donationId, eventData);
    // });
    //
    // // 책 나눔 이벤트 삭제 버튼 클릭
    // $(document).on('click', 'button.btn-outline-danger', function() {
    //     const donationId = $(this).closest('tr').find("input[name='donationId']").val();
    //     $.ajax({
    //         type: 'DELETE',
    //         url: '/api/admin/donation/' + donationId,
    //         success: function(response) {
    //             alert(response.message);
    //             location.reload();
    //         },
    //         error: function(xhr, status, error) {
    //             alert('책 나눔 이벤트 삭제에 실패했습니다.');
    //         }
    //     });
    // });


