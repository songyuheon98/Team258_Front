$(document).ready(function () {
    fetchAndDisplayBookRentals();
});

function fetchAndDisplayBookRentals() {
    $.ajax({
        url: '/api/books/reservation', // 실제 API 엔드포인트에 맞게 URL 수정
        type: 'GET',
        success: function (data) {
            displayBookRentals(data);
        },
        error: function (error) {
            console.error('도서 대출 정보를 가져오지 못했습니다:', error);
        },
    });
}

function displayBookRentals(bookReservations) {
    const bookReservationsList = document.getElementById('bookReservationsList');
    bookReservationsList.innerHTML = ''; // 이전 내용 지우기

    bookReservations.forEach(function (bookReservation) {
        const reservationItem = document.createElement('div');
        reservationItem.className = 'reservation-item';

        reservationItem.innerHTML = `
            <p>책 이름: ${bookReservation.bookName}</p>
            <button class="btn btn-outline-primary return-button" data-book-rent-id="${bookReservation.bookId}">예약 취소하기</button>
            <br />
            <br />
        `;

        bookReservationsList.appendChild(reservationItem);

        // 반납 버튼에 이벤트 리스너 추가
        const returnButton = reservationItem.querySelector('.return-button');
        returnButton.addEventListener('click', function () {
            const bookId = bookReservation.bookId;
            handleReturnBook(bookId);
        });
    });
}

function handleReturnBook(bookId) {
    $.ajax({
        url: `/api/books/${bookId}/reservation`, // 실제 API 엔드포인트에 맞게 URL 수정
        type: 'DELETE', // DELETE 메소드는 대문자로 지정
        success: function (data) {
            alert(`예약 취소가 완료되었습니다.`);
            location.reload();
        },
        error: function (error) {
            console.error(error);
        },
    });
    alert(`예약 취소를 처리합니다.`);
}