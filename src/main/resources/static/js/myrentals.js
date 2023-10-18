
$(document).ready(function () {
    fetchAndDisplayBookRentals();
});

function fetchAndDisplayBookRentals() {
    $.ajax({
        url: '/api/books/rental', // 실제 API 엔드포인트에 맞게 URL 수정
        type: 'GET',
        success: function (data) {
            displayBookRentals(data);
        },
        error: function (error) {
            console.error('도서 대출 정보를 가져오지 못했습니다:', error);
        },
    });
}

function displayBookRentals(bookRentals) {
    const bookRentalsList = document.getElementById('bookRentalsList');
    bookRentalsList.innerHTML = ''; // 이전 내용 지우기

    bookRentals.forEach(function (bookRental) {
        const rentalItem = document.createElement('div');
        rentalItem.className = 'rental-item';

        rentalItem.innerHTML = `
            <p>책 이름: ${bookRental.bookName}</p>
            <p>반납기한: ${bookRental.returnDate}</p>
            <button class="btn btn-outline-primary return-button" data-book-rent-id="${bookRental.bookId}">도서 반납하기</button>
            <br />
            <br />
        `;

        bookRentalsList.appendChild(rentalItem);

        // 반납 버튼에 이벤트 리스너 추가
        const returnButton = rentalItem.querySelector('.return-button');
        returnButton.addEventListener('click', function () {
            const bookId = bookRental.bookId;
            handleReturnBook(bookId);
        });
    });
}

function handleReturnBook(bookId) {
    $.ajax({
        url: `/api/books/${bookId}/rental`, // 실제 API 엔드포인트에 맞게 URL 수정
        type: 'DELETE', // DELETE 메소드는 대문자로 지정
        success: function (data) {
            alert(`반납이 완료되었습니다.`);
            location.reload();
        },
        error: function (error) {
            console.error(error);
        },
    });
    alert(`도서 반납을 처리합니다.`);
}