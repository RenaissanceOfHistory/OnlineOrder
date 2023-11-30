/** 上传用户头像 */
function uploadAvatar(contextPath = "", file) {
    if (!/image\/\w+/.test(file.type)) return false;

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = async () => {
        const code = reader.result;
        $("#user-avatar").attr("src", code);
        $(".avatar-dot").css("opacity", "1");

        const [err, result] = await to($.ajax({
            url: `${contextPath}/users/avatar/upload`,
            type: AjaxType.POST,
            headers: {
                "Content-Type": MediaType.APPLICATION_JSON
            },
            data: JSON.stringify({avatar: code})})
        );

        if (!err) {
            console.log(result);
        }
    }
}

/** 下载用户头像 */
async function downloadAvatar(contextPath) {
    console.log("请求用户头像");
    const [err, data] = await to($.ajax({
        url: `${contextPath}/users/avatar/download`,
        type: AjaxType.GET,
        headers: {
            "Authorization": localStorage.getItem("token")
        },
        xhrFields: {
            responseType: "blob"
        }}));

    if (!err) {
        $("#user-avatar").attr("src", URL.createObjectURL(data));
        $(".avatar-dot").css("opacity", "1");
    }
}

async function setCartNum(contextPath = "") {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/orders/cart/count`,
        type: AjaxType.GET
    }));

    if (!err && 200 === result.code) {
        $(".cart-num").text(result.data);
    }
}