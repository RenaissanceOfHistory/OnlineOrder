package free.lifetime.controller;

import free.lifetime.constants.ServiceConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @create: 2023/11/13
 * @Description:
 * @FileName: IndexController
 */
@Slf4j
@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String toIndex(HttpSession session, Model model, Integer funcIdx) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("isRoot", session.getAttribute("isRoot"));
        model.addAttribute("funcIdx", Objects.isNull(funcIdx) ? 0 : funcIdx);
        return "index";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        Boolean isRoot = (Boolean) session.getAttribute("isRoot");
        model.addAttribute("isRoot", isRoot);
        return String.format("forward:%s/list",
                (isRoot ? ServiceConstant.FOOD_SERVICE : ServiceConstant.MENU_SERVICE));
    }

    @GetMapping("/error")
    public String oError() {
        return "error";
    }
}
