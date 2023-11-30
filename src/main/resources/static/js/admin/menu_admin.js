function queryFood({contextPath = ""}) {
    const sid = $(".select-store").val(),
        foodName = $("#search-food").val().trim();
    location.href = `${contextPath}/foods/list?sid=${sid}&name=${foodName}`;
}

function changeFoodPage({contextPath = ""}) {
    const sid = $(".select-store").val(),
        foodName = $("#search-food").val().trim();
    location.href = `${contextPath}/foods/list?pageSize=${$(".select-page").val()}&sid=${sid}&name=${foodName}`;
}