package myApp.myApp.Controller;

import myApp.myApp.Entity.Candidate;
import myApp.myApp.Entity.User;
import myApp.myApp.Entity.UserDto;
import myApp.myApp.Entity.Votes;
import myApp.myApp.Repository.VotesRepository;
import myApp.myApp.Service.CandidateService;
import myApp.myApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private CandidateService candidateService;

    private VotesRepository votesRepository;


    public AuthController(UserService userService, CandidateService candidateService, VotesRepository votesRepository) {
        this.userService = userService;
        this.candidateService = candidateService;
        this.votesRepository = votesRepository;
    }

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }


    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "login";
    }

    @GetMapping("/all-users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "all-users";
    }

    @GetMapping("/homeLogged")
    public String homeUser(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "homeLogged";
    }

    @RequestMapping(value="/all-candidates", method = RequestMethod.GET)
    public String getCandidates(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "all-candidates";
    }

    @PostMapping("/all-candidates")
    public String registerVote(@RequestParam("idCandidate") Long idCandidate, @AuthenticationPrincipal UserDetails user, RedirectAttributes redirectAttributes) {
        if (user != null) {
            User user1 = userService.findByEmail(user.getUsername());
            Candidate candidate = candidateService.getCandidateById(idCandidate);
            if (!user1.getVoted()) {
                Long addVote = candidate.getNoVotes();
                ++addVote;
                candidate.setNoVotes(addVote);
                candidateService.saveCandidate(candidate);
                user1.setVoted(true);
                userService.updateUser(user1.getEmail(), user1);
                redirectAttributes.addFlashAttribute("message", "You have successfully voted");
                Votes votes = new Votes();
                votes.setEmailUser(user1.getEmail());
                votes.setEmailCandidate(candidate.getEmail());
                votesRepository.save(votes);
                return "redirect:/homeLogged";
            } else {
                redirectAttributes.addFlashAttribute("message", "You have already voted");
                return "alreadyVoted";
            }
        }
        return "redirect:/homeLogged";
    }
    @RequestMapping("/admin")
    public String homepageAdmin() {
        return "admin";
    }

}
