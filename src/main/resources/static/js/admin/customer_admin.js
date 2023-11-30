function queryUser({contextPath = ""}) {
    const username = $("#search-user").val().trim();
    location.href = `${contextPath}/users/list?username=${username}`;
}

function changeUserPage({contextPath = ""}) {
    const username = $("#search-user").val().trim(),
        pageSize = $(".select-page").val();
    location.href = `${contextPath}/users/list?pageSize=${pageSize}&username=${username}`;
}