package free.lifetime.controller;

import free.lifetime.common.Result;
import free.lifetime.constants.ServiceConstant;
import free.lifetime.model.SalesAmount;
import free.lifetime.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @create: 2023/11/23
 * @Description:
 * @FileName: StatisticsController
 */
@Controller
@RequestMapping(ServiceConstant.STATISTICS_SERVICE)
public class StatisticsController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/amount")
    @ResponseBody
    public Result salesAmount() {
        List<SalesAmount> salesAmountList = orderService.querySalesAmount();
        if (salesAmountList.size() > 6) {
            salesAmountList = salesAmountList.subList(0, 6);
        }
        return Result.ok("查询销售量成功", salesAmountList);
    }

    @GetMapping("/sales/amount")
    public String amount() {
        return "forward:/?funcIdx=3";
    }

    @GetMapping("/sales/sum")
    public String sum() {
        return "forward:/?funcIdx=4";
    }
}
