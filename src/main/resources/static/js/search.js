$(document).ready(function () {
    let urlParams = new URLSearchParams(window.location.search);
    let currentPage = urlParams.get('page')==null?1:parseInt(urlParams.get('page'));
    let currentCategory = urlParams.get('bookCategoryName');
    let currentKeyword = urlParams.get('keyword');
    let pagination = document.getElementById('paging');
    let rangeStart = Math.floor((currentPage-1) / 10) * 10+1;

    let rangeEnd = Math.min(rangeStart + 9, maxCount);

    let url = ""
    if(currentKeyword != null & currentCategory !=null)
        url = `/search?keyword=${currentKeyword}&bookCategoryName=${currentCategory}&page=`
    else if(currentKeyword == null & currentCategory !=null)
        url = `/search?bookCategoryName=${currentCategory}&page=`
    else if(currentKeyword != null & currentCategory ==null)
        url = `/search?keyword=${currentKeyword}&page=`
    else if(currentKeyword == null & currentCategory ==null)
        url = "/search?page="
    if (rangeStart > 1) {
        pagination.innerHTML += `<a href="${url}${rangeStart - 1}">Prev</a> `;
    }

    for (var i = rangeStart; i <= rangeEnd; i++) {
        if (i!=currentPage)
            pagination.innerHTML += `<a href="${url}${i}">${i}</a> `;
        else
            pagination.innerHTML += `<a>${i}</a> `;
    }
    if (rangeEnd < maxCount) {
        pagination.innerHTML += `<a href="${url}${rangeEnd+1}">Next</a>`;
    }

    // 대출하기 버튼 클릭 시 이벤트 핸들러 설정
    $(document).on('click', '.rent-button', function () {
        const bookId = $(this).data('book-id');
        rentBook(bookId);
    });


    // 예약하기 버튼 클릭 시 이벤트 핸들러 설정
    $(document).on('click', '.reserve-button', function () {
        const bookId = $(this).data('book-id');
        reserveBook(bookId);
    });
})

function search() {
    let keyword = document.getElementById('keyword').value;
    let category = document.getElementById('bookCategoryId').value;

    if(keyword != '' & category != '')
        url = `/search?keyword=${keyword}&bookCategoryName=${category}&page=1`
    else if(keyword == '' & category !='')
        url = `/search?bookCategoryName=${category}&page=1`
    else if(keyword != '' & category =='')
        url = `/search?keyword=${keyword}&page=1`
    else if(keyword == '' & category =='')
        url = "/search?page=1"

    window.location.href = url;
}

function rentBook(bookId) {
    $.ajax({
        url: `/api/books/${bookId}/rental`,
        type: "POST",
        success: function (response) {
            alert("대여가 완료되었습니다.");
            //현재 성능상 문제로 reload는 안함
        },
        error: function (error) {
            alert("대여에 실패했습니다.");
            console.error(error);
            //추후 오류 문구 전달 필요?
        },
    });
}

function reserveBook(bookId) {
    $.ajax({
        url: `/api/books/${bookId}/reservation`,
        type: "POST",
        success: function (response) {
            alert("예약이 완료되었습니다.");
            //현재 성능상 문제로 reload는 안함
        },
        error: function (error) {
            alert("예약에 실패했습니다.");
            console.error(error);
            //추후 오류 문구 전달 필요?
        },
    });
}