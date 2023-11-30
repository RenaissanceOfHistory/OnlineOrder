function queryStock({contextPath = ""}) {
    const foodName = $("#search-stock").val().trim();
    location.href = `${contextPath}/stocks/list?name=${foodName}`;
}

function changeStockPage({contextPath = ""}) {
    const foodName = $("#search-stock").val().trim();
    location.href = `${contextPath}/stocks/list?pageSize=${$(".select-page").val()}&name=${foodName}`;
}

async function addStock(contextPath = "", id = 0, stock = 0) {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/stocks/add`,
        method: AjaxType.PUT,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        data: JSON.stringify({id: id, stock: stock})
    }));

    await showTip(result);
    if (!err && 200 === result.code) {
        toPath(`${contextPath}/stocks/list`, 2000);
    }
}