const AjaxType = {
    GET: "GET",
    POST: "POST",
    DELETE: "DELETE",
    PUT: "PUT"
}, MediaType = {
  APPLICATION_JSON: "application/json"
};


/**
 * 消息提示
 * @param obj
 */
async function message(obj = {message: "", cls: "alert-danger", timeout: 0}) {
    // 添加类并设置文本
    const message = $(".message");
    message.addClass(obj.cls).text(obj.message).fadeIn();

    // timeout 时间后移除类
    setTimeout(() => message.fadeOut().removeClass(obj.cls), obj.timeout || 2000);
}

/**
 * @param ruleList  校验对象
 * @param callback  失败回调
 * @returns {boolean}
 */
async function validate(ruleList = [{condition: false, message: "", cls: ""}],
                        callback = Function()) {
    for (const rule in ruleList) {
        if (!ruleList.hasOwnProperty(rule)) continue;   // 不存在属性(rule)时结束本次循环
        // 遍历检验项
        for (const item of ruleList[rule]) {
            let flag = (() => {     // 闭包判断条件
                if (!item.condition) {
                    callback(item);
                    return false;
                }
                return true;
            })();
            if (!flag) return false;
        }
    }
    return true;
}

/**
 * 获取checkbox为checked的值
 * @param element
 * @returns {Array}
 */
function checkboxChecked(element = "") {
    const checkedList = [];
    $(`${element}:checked`).each((_, e) => checkedList.push(e.value));
    return checkedList;
}

/**
 * 将element元素的src设置为node的图像
 * @param node  input[type=file]元素
 * @param element   img元素
 */
async function changeImage(node = new HTMLInputElement(), element = "") {
    const file = node.files[0],
        reader = new FileReader();
    reader.onload = _ => $(element).attr("src", reader.result);
    reader.readAsDataURL(file);
}

function to(promise, errorExt) {
    return promise
        .then(function (data) { return [null, data]; })["catch"](function (err) {
        if (errorExt) {
            const parsedError = Object.assign({}, err, errorExt);
            return [parsedError, undefined];
        }
        return [err, undefined];
    });
}
$(function () {
    // body 标签添加 class='message' 的 div 块
    const message = $("<div>").attr("class", "message");
    $("body").append(message);
});