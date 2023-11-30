async function chartSalesAmount(labelList = [], dataList = []) {
    let ctx = $("#sales-amount-3d-pie"),
        data = {
            labels: labelList,
            datasets: [{
                data: dataList,
                hoverOffset: 4
            }],
        },
        config = {
            type: 'doughnut',
            data: data,
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: "不同店铺菜品销售量对比",
                        font: {
                            size: 16
                        }
                    },
                    subtitle: {
                        display: true,
                        text: "销售量的环形图"
                    }
                },
                responsive: true,
                maintainAspectRatio: false,
            }
        };
    new Chart(ctx, config);
}

async function chartBar(labelList = [], dataList = []) {
    let ctx = $("#sales-amount-bar"),
        data = {
            labels: labelList,
            datasets: [{
                data: dataList,
                hoverOffset: 4
            }],
        },
        config = {
            type: 'bar',
            data: data,
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: "菜品销售量排行榜",
                        font: {
                            size: 16
                        }
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        }
                    },
                },
                responsive: true,
                maintainAspectRatio: false,
            }
        };

    new Chart(ctx, config);
}

async function querySalesAmount(contextPath = "") {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/statistics/amount`,
        type: AjaxType.GET
    }));

    return (!!err) ? [] : result.data;
}