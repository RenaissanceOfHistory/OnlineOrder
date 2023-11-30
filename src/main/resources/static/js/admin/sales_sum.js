async function chartSalesSum(contextPath = "") {
    const ctx = $("#chartBar"),
        labels = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        data = {
            labels: labels,
            datasets: [{
                label: 'Monthly Revenue',
                data: [12, 19, 3, 5, 2, 3, 12, 19, 3, 5, 2, 3],
                borderWidth: 1,
            }],
        },
        config = {
            type: 'bar',
            data: data,
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        grid: {
                            display: false
                        }
                    },
                    y: {
                        grid: {
                            display: false
                        },
                        beginAtZero: true
                    }
                },
                layout: {
                    padding: 20
                }
            }
        };

    const salesAmount = await querySalesAmount(contextPath);
    new Chart(ctx, config);
}

async function querySalesAmount(contextPath = "") {
    const [err, result] = await to($.ajax({
        url: `${contextPath}/statistics/amount`,
        type: AjaxType.GET
    }));

    return (!!err) ? [] : result.data;
}