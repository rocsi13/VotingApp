package myApp.myApp.Controller;

import myApp.myApp.Service.CandidateService;
import myApp.myApp.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private CandidateService candidateService;

    public AdminController(UserService userService, CandidateService candidateService) {
        this.candidateService = candidateService;
        this.userService = userService;
    }

    @GetMapping("/setTimer")
    public String setTimer(Model model) {
        return "setTimer";
    }
}
