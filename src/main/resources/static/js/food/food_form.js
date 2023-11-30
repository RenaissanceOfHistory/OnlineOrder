function getFoodObj(contextPath = "") {
    const form = {
        id: $("#foodId").val(),
        name: $("#foodName").val().trim(),
        sid: $("#store").val(),
        price: $("#foodPrice").val().trim(),
        stock: $("#foodStock").val().trim(),
        desc: $("#foodDesc").val().trim(),
        image: $(".food-img").attr("src")
    }, ruleList = {
        name: [
            {condition: form.name !== "", message: "菜品名不能为空", cls: "alert-danger"}],
        sid: [
            {condition: form.sid !== "", message: "所属店铺不能为空", cls: "alert-danger"}],
        price: [
            {condition: form.price !== "", message: "单价不能为空", cls: "alert-danger"},
            {condition: /^\d+(\.\d+)?$/.test(form.price), message: "单价应为非负数", cls: "alert-danger"}],
        stock: [
            {condition: form.stock !== "", message: "容量不能为空", cls: "alert-danger"},
            {condition: /^\d+$/.test(form.stock), message: "容量应为非负整数", cls: "alert-danger"}]
    };

    if (form.image.indexOf(contextPath) !== -1) form.image = "";

    return {
        form: form,
        ruleList: ruleList
    };
}
