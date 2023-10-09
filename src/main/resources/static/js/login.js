// ajax 요청 샘플입니다.
/*
$(document).ready(function() {
    $("#deleteColumnForm").on("submit", function(e) {
        e.preventDefault();

        const columnId = $("#columnId").val();

        $.ajax({
            type: "DELETE",
            url: "/api/column/" + columnId,
            success: function() {
                alert("컬럼 삭제에 성공하였습니다."); // 예상 응답에 따라 수정이 필요합니다.
                window.history.back();
            },
            error: function(error) {
                alert("컬럼 삭제에 실패하였습니다.");
            }
        });
    });
});
*/
function login(){
    alert("login기능을 구현해 주세요.")
}

function moveSignUpView(){
    window.location.href = '/signup';
}