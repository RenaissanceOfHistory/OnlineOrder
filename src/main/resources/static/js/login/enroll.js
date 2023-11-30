async function enroll(contextPath = "") {
    const obj = getUserForm();
    console.log(obj);

    if (await validate(obj.ruleList, message)) {
        const [err, result] = await to($.ajax({
            url: `${contextPath}/users/enroll/${obj.user.code}`,
            type: AjaxType.POST,
            headers: {
                "Content-Type": MediaType.APPLICATION_JSON
            },
            data: JSON.stringify(obj.user.user)
        }));

        await showTip(result);
        if (!err && 200 === result.code) {
            $("#enroll").css("display", "none");
            $("#login").css("display", "block");
        }
    }
}