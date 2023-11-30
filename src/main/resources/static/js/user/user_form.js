function getUserObj(contextPath = "") {
    const form = {
        id: $("#userDetailId").val(),
        uid: $("#uid").val(),
        username: $("#username").val().trim(),
        email: $("#email").val().trim(),
        phone: $("#phone").val().trim(),
        address: $("#address").val().trim(),
        avatar: $(".user-img").attr("src")
    }, ruleList = {
        username: [
            {condition: "" !== form.username, message: "用户名不能为空", cls: "alert-danger"}],
        email: [
            {condition: /^([a-zA-Z0-9]+[-_.]?)+@[a-zA-Z0-9]+\.[a-z]+$/.test(form.email),
                message: "邮箱格式错误", cls: "alert-danger"}],
        phone: [
            {condition: /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/.test(form.phone),
                message: "电话|固话格式错误", cls: "alert-danger"}]
    };
    if (form.avatar.indexOf(contextPath) !== -1) form.avatar = "";

    return {
        form: form,
        ruleList: ruleList
    };
}
