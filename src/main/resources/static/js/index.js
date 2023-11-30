async function showTip(result = {code: 0, info: "", data: {}}) {
    await message({
        message: result.info,
        cls: (200 === result.code) ? "alert-success" : "alert-danger",
        timeout: 2000
    });
}

function toPath(path = "", timeout = 0) {
    setTimeout(() => (location.href = path), timeout);
}

async function removeBatch({contextPath = "", element = "", prefix = ""}) {
    const idList = checkboxChecked(element);
    if (idList.length <= 0) return;

    const [err, result] = await to($.ajax({
        url: `${contextPath}/${prefix}/remove/id-list`,
        type: AjaxType.DELETE,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        data: JSON.stringify(idList)
    }));

    await showTip(result);
    if (!err) {
        if (200 === result.code) {
            toPath(`${contextPath}/${prefix}/list`, 1000);
        }
    }
}

async function removeItem({contextPath = "", id = 0, prefix = ""}) {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/${prefix}/remove/${id}`,
        type: AjaxType.DELETE
    }));

    await showTip(result);
    if (!err) {
        if (200 === result.code) {
            toPath(`${contextPath}/${prefix}/list`, 2000);
        }
    }
}

async function addItem(contextPath = "", prefix = "", obj = {form: {}, ruleList: [{}]}) {
    if (await validate(obj.ruleList, message)) {
        const [err, result] = await to($.ajax({
            url: `${contextPath}/${prefix}/add`,
            type: AjaxType.POST,
            headers: {
                "Content-Type": MediaType.APPLICATION_JSON
            },
            data: JSON.stringify(obj.form)})
        );

        await showTip(result);
        if (!err) {
            if (200 === result.code) {
                toPath(`${contextPath}/${prefix}/list`, 2000);
            }
        }
    }
}

async function updateItem(contextPath = "", prefix = "", obj = {form: {}, ruleList: [{}]}) {
    if (await validate(obj.ruleList, message)) {
        const [err, result] = await to($.ajax({
            url: `${contextPath}/${prefix}/update`,
            type: AjaxType.PUT,
            headers: {
                "Content-Type": MediaType.APPLICATION_JSON
            },
            data: JSON.stringify(obj.form)})
        );

        await showTip(result);
        if (!err) {
            if (200 === result.code) {
                toPath(`${contextPath}/${prefix}/list`, 2000);
            }
        }
    }
}

