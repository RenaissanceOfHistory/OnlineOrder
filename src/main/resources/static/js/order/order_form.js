function getOrderObj() {
    const form = {
        id: $("#orderId").val(),
        uid: $("#uid").val(),
        fid: $("#fid").val(),
        count: $("#amount").val().trim()
    }, ruleList = {
        count: [
            {condition: "" !== form.count, message: "数量不能为空", cls: "alert-danger"},
            {condition: /^\d+$/.test(form.count), message: "数量应为非负整数", cls: "alert-danger"}
        ]
    };

    return {
        form: form,
        ruleList: ruleList
    };
}