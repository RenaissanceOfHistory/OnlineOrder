function getUserObj() {
    const form = {
        oldPassword: $("#oldPassword").val().trim(),
        newPassword: $("#newPassword").val().trim(),
        confirm: $("#confirm").val().trim()
    }, ruleList = {
        oldPassword: [
            {condition: "" !== form.oldPassword, message: "旧密码不能为空", cls: "alert-danger"}],
        newPassword: [
            {condition: "" !== form.newPassword, message: "新密码不能为空", cls: "alert-danger"}],
        confirm: [
            {condition: "" !== form.confirm, message: "确认密码不能为空", cls: "alert-danger"},
            {condition: form.newPassword === form.confirm, message: "确认密码与新密码不一致", cls: "alert-danger"}],
    };

    return {
        form: form,
        ruleList: ruleList
    };
}

async function modifyPassword(contextPath = "") {
    const obj = getUserObj();
    if (await validate(obj.ruleList, message)) {
        const [err, result] = await to($.ajax({
            url: `${contextPath}/users/password/change`,
            method: AjaxType.PUT,
            headers: {
                "Content-Type": MediaType.APPLICATION_JSON
            },
            data: JSON.stringify(obj.form)
        }));

        await showTip(result);
        if (!err && 200 === result.code) {
            localStorage.removeItem("token");
            toPath(`${contextPath}`, 2000);
        }
    }
}