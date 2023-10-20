$(document).ready(function() {
    // 검색 기능 추가
    window.searchCategories = function() {
        var keyword = $('input[name="keyword"]').val();
        window.location.href = '/admin/categories?keyword=' + keyword;
    };

    // 검색 폼 제출 시 검색 기능 호출
    $('form').submit(function(event) {
        event.preventDefault();
        searchCategories();
    });


    window.createMainCategory = function() {
        var data = {
            bookCategoryIsbnCode: $('input[id="bookUpCategoryIsbnCode"]').val(),
            bookCategoryName: $('input[id="bookUpCategoryName"]').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/admin/categories',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('상위 카테고리 생성 성공!');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                alert('상위 카테고리 생성 실패!');
                window.location.reload();
            }
        });
    };

    window.createSubCategory = function() {
        var data = {
            bookCategoryIsbnCode: $('input[id="bookSubCategoryIsbnCode"]').val(),
            bookCategoryName: $('input[id="bookSubCategoryName"]').val()
        };

        var parentCategoryId = $('input[id="parentCategoryId"]').val();

        $.ajax({
            type: 'POST',
            url: '/api/admin/categories/' + parentCategoryId + '/subcategories',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('하위 카테고리 생성 성공!');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                alert('하위 카테고리 생성 실패!');
                window.location.reload();
            }
        });
    };


    window.updateBookCategory = function(bookCategoryId) {
        var row = $('input[name="bookCategoryId"][value="' + bookCategoryId + '"]').closest('tr');
        var data = {
            bookCategoryIsbnCode: row.find('input[name="bookCategoryIsbnCode"]').val(),
            bookCategoryName: row.find('input[name="bookCategoryName"]').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/api/admin/categories/' + bookCategoryId,
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('카테고리 업데이트 성공!');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                alert('카테고리 업데이트 실패!');
                window.location.reload();
            }
        });
    };


    window.deleteBookCategory = function(bookCategoryId) {
        var row = $('input[name="bookCategoryId"][value="' + bookCategoryId + '"]').closest('tr');
        var parentCategoryId = $('input[name="parentCategoryId"]', row).val();

        if (parentCategoryId === null || parentCategoryId === '') {
            if (confirm('이 카테고리를 삭제하면 모든 서브카테고리도 함께 삭제됩니다. \n계속 진행하시겠습니까?')) {
                deleteCategory(bookCategoryId, row);
            }
        } else {
            if (confirm('정말로 이 카테고리를 삭제하시겠습니까?')) {
                deleteCategory(bookCategoryId, row);
            }
        }
    };
    function deleteCategory(bookCategoryId, row) {
        $.ajax({
            type: 'DELETE',
            url: '/api/admin/categories/' + bookCategoryId,
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            success: function(response) {
                alert('카테고리 삭제 성공!');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                alert('카테고리 삭제 실패!');
                window.location.reload();
            }
        });
    };

});
