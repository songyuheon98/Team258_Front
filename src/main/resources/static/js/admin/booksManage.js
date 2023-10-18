$(document).ready(function() {

    window.updateBook = function (bookId) {
        var row = $('input[name="bookId"][value="' + bookId + '"]').closest('tr');
        var data = {
            bookName: row.find('input[name="bookName"]').val(),
            bookAuthor: row.find('input[name="bookAuthor"]').val(),
            bookPublish: row.find('input[name="bookPublish"]').val(),
            bookStatus: row.find('input[name="bookStatus"]').val(),
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