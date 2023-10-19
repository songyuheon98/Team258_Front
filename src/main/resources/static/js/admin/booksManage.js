$(document).ready(function() {
    // 도서 검색을 위한 Ajax 요청
    $('#search-form').submit(function (event) {
        event.preventDefault(); // 폼 기본 동작 중단

        var keyword = $('#search-keyword').val(); // 검색어 입력란의 값 가져오기

        // 검색어가 비어있지 않은 경우에만 요청
        if (keyword.trim() !== '') {
            $.ajax({
                type: 'GET',
                url: '/admin/booksManage',
                data: { keyword: keyword }, // 검색어를 파라미터로 전송
                success: function (response) {
                    // 서버로부터 받은 HTML을 페이지에 적용
                    $('#book-list-container').html(response);
                },
                error: function (xhr, status, error) {
                    console.error('도서 검색 실패:', error);
                }
            });
        }
    });

    window.updateBook = function (bookId) {
        var row = $('input[name="bookId"][value="' + bookId + '"]').closest('tr');
        var data = {
            bookName: row.find('input[name="bookName"]').val(),
            bookAuthor: row.find('input[name="bookAuthor"]').val(),
            bookPublish: row.find('input[name="bookPublish"]').val(),
            bookStatus: row.find('select[name="bookStatus"]').val(), // 수정된 부분
            bookCategoryId: row.find('input[name="bookCategoryId"]').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/api/admin/books/' + bookId,
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function (response) {
                alert('도서 정보 업데이트 성공!');
                window.location.reload();
            },
            error: function (xhr, status, error) {
                alert('도서 정보 업데이트 실패!');
                window.location.reload();
            }
        });
    };

    window.deleteBook = function (bookId) {
        if (confirm('정말로 이 도서를 삭제하시겠습니까?')) {
            $.ajax({
                type: 'DELETE',
                url: '/api/admin/books/' + bookId,
                contentType: 'application/json;charset=UTF-8',
                dataType: 'json',
                success: function (response) {
                    alert('도서 삭제 성공!');
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    alert('도서 삭제 실패!');
                    window.location.reload();
                }
            });
        }
    };
})