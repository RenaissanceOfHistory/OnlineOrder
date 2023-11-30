function getStoreObj() {
    const form = {
        id: $("#storeId").val(),
        name: $("#storeName").val().trim(),
        desc: $("#storeDesc").val().trim(),
    }, ruleList = {
        name: [
            {condition: form.name !== "", message: "店铺名不能为空", cls: "alert-danger"}],
    };

    return {
        form: form,
        ruleList: ruleList
    };
}
