async function removeOrder(contextPath = "", oid = 0) {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/orders/remove/${oid}`,
        type: AjaxType.DELETE
    }));

    await showTip(result);
    if (!err && 200 === result.code) {
        toPath(`${contextPath}/menus/cart`, 2000);
    }
}

async function addOrderCount(contextPath = "", element) {
    const fid = element.data("fid"),
        oid = element.data("oid"),
        count = Number(element.data("count"));

    if (count >= 999) {
        message({
            message: "购买数量至多999",
            cls: "alert-danger",
            timeout: 2000
        });
        return;
    }

    const [err, result] = await to($.ajax({
        url: `${contextPath}/orders/update`,
        type: AjaxType.PUT,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        data: JSON.stringify({
            fid: fid,
            id: oid,
            count: count + 1
        })
    }));

    if (!err && 200 === result.code) {
        toPath(`${contextPath}/menus/cart`, 100);
    }
}

async function subtractOrderCount(contextPath = "", element) {
    const fid = element.data("fid"),
        oid = element.data("oid"),
        count = Number(element.data("count"));

    if (count <= 1) {
        message({
            message: "购买数量不能低于1",
            cls: "alert-danger",
            timeout: 2000
        });
        return;
    }

    const [err, result] = await to($.ajax({
        url: `${contextPath}/orders/update`,
        type: AjaxType.PUT,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        data: JSON.stringify({
            fid: fid,
            id: oid,
            count: count - 1
        })
    }));

    if (!err && 200 === result.code) {
        toPath(`${contextPath}/menus/cart`, 100);
    }
}

async function payOrder(contextPath = "") {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/orders/pay`,
        type: AjaxType.GET
    }));

    await showTip(result);
    if (!err && 200 === result.code) {
        toPath(`${contextPath}/menus/order`, 2000);
    }
}