async function addCart(contextPath = "", fid = 0) {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/orders/add/cart`,
        type: AjaxType.POST,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        data: JSON.stringify({fid: fid})
    }));

    await showTip(result);
    if (!err && 200 === result.code) {
        await setCartNum(contextPath);
        $(".left-food-stock").text(await queryFoodStock(contextPath, fid));
    }
}

async function setCollected(contextPath = "", collected = "", fid = 0) {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/menus/collection/status`,
        type: AjaxType.PUT,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        data: JSON.stringify({
            fid: fid,
            collected: collected
        })
    }));

    await showTip(result);
}

async function queryFoodStock(contextPath = "", fid = 0) {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/foods/query/stock/${fid}`,
        type: AjaxType.GET
    }));
    
    if (!err && 200 === result.code) {
        return result.data;
    }
    return -1;
}