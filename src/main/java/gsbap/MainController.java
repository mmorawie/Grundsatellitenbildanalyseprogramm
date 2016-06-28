package gsbap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value="/main", method=RequestMethod.GET)
    public String mainForm(Model model) {
        model.addAttribute("main", new Main());
        return "main";
    }

    @RequestMapping(value="/main", method=RequestMethod.POST)
    public String mainSubmit(@ModelAttribute Main main, Model model) {
        model.addAttribute("main", main);
        Main.calculate();
        return "result";
    }

}
