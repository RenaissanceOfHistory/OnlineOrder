function queryOrder({contextPath = ""}) {
    const id = $("#search-order").val().trim();
    location.href = `${contextPath}/orders/list?id=${id}`;
}

function changeOrderPage({contextPath = ""}) {
    const id = $("#search-order").val().trim(),
        pageSize = $(".select-page").val();
    location.href = `${contextPath}/orders/list?pageSize=${pageSize}&id=${id}`;
}