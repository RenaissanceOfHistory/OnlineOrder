/** 获取验证码 */
function getCode() {
    this.src += '?';
    if (this.src.length >= 100) {
        // 截断'?'号
        this.src = this.src.substring(0, 60);
    }
}

/** 获取保存的用户名和密码 */
function init() {
    const username = localStorage.getItem("username"),
        password = localStorage.getItem("password");
    if (username != null) $("#username").val(username);
    if (password != null) $("#password").val(password);
}

/** 根据是否勾选”记住我“设置信息的本地保存 */
function rememberMe(username, password) {
    if ($("#remember").is(":checked")) {
        localStorage.setItem("username", username);
        localStorage.setItem("password", password);
    } else {
        localStorage.removeItem("username");
        localStorage.removeItem("password");
    }
}

function getUserForm() {
    const form = {
        user: {
            username: $("#username").val().trim(),
            password: $("#password").val().trim()
        },
        code: $("#code").val().trim()
    }, ruleList = {        // 校验规则
        "username": [
            {condition: "" !== form.user.username, message: "用户名不能为空", cls: "alert-danger"}],
        "password": [
            {condition: "" !== form.user.password, message: "密码不能为空", cls: "alert-danger"}],
        "code": [
            {condition: "" !== form.code, message: "验证码不能为空", cls: "alert-danger"}]
    };

    return {
        user: form,
        ruleList: ruleList
    };
}

/** 用户登录 */
async function login(contextPath = "") {
    const obj = getUserForm();

    rememberMe(obj.user.user.username, obj.user.user.password);
    if (await validate(obj.ruleList, message)) {
        // 校验成功，后端请求登录
        const [err, result] = await to($.ajax({
            url: `${contextPath}/users/login/${obj.user.code}`,
            type: AjaxType.POST,
            headers: {
                "Content-Type": MediaType.APPLICATION_JSON
            },
            data: JSON.stringify(obj.user.user)
        }));

        await showTip(result);
        if (!err && 200 === result.code) {
            localStorage.setItem("token", result.data);
            toPath(`${contextPath}/list`, 1500);
        }
    }
}

$(function () {
    $("#code-img").on("click", getCode);
});