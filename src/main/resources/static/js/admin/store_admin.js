function queryStore({contextPath = ""}) {
    const storeName = $("#search-store").val().trim();
    location.href = `${contextPath}/stores/list?name=${storeName}`;
}

function changeStorePage({contextPath = ""}) {
    const storeName = $("#search-store").val().trim();
    location.href = `${contextPath}/stores/list?pageSize=${$(".select-page").val()}&name=${storeName}`;
}