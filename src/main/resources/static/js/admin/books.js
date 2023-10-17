$(document).ready(function () {
    $('#register-book-button').on('click', function () {
        var form = $('#register-book-form');

        // input 태그로부터 날짜 값을 가져오기
        var rawDate = form.find('input[name="bookPublish"]').val();

        // 입력된 날짜 값을 Date 객체로 변환
        var dateObject = new Date(rawDate);

        // 변환된 Date 객체에서 각 부분을 추출
        var year = dateObject.getFullYear();
        var month = ('0' + (dateObject.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 1을 더하고 0을 붙여서 두 자리로 만듦
        var day = ('0' + dateObject.getDate()).slice(-2);
        var hours = ('0' + dateObject.getHours()).slice(-2);
        var minutes = ('0' + dateObject.getMinutes()).slice(-2);
        var seconds = ('0' + dateObject.getSeconds()).slice(-2);

        // "YYYY-MM-DDTHH:mm:ss" 형식으로 조합
        var formattedDate = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes + ':' + seconds;

        var data = {
            bookName: form.find('input[name="bookName"]').val(),
            bookAuthor: form.find('input[name="bookAuthor"]').val(),
            bookPublish: formattedDate, // 변환된 날짜 문자열 사용
            bookCategoryId: form.find('select[name="bookCategoryId"] :selected').val()
        };
        console.log(data)
        console.log(data.bookPublish)
        $.ajax({
            type: 'POST',
            url: '/api/admin/books',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function (response) {
                alert('도서등록 성공!');
                window.location.reload();
                console.log(data)

            },
            error: function (xhr, status, error) {
                alert('도서등록 실패!');
                console.log(data)

                // 오류 메시지는 실제 API 응답에 따라 조절할 수 있습니다.
                // window.location.reload();
            }
        });
    });
});
