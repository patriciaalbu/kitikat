package com.ciutz.kitikat.controller;

import com.ciutz.kitikat.entities.User;
import com.ciutz.kitikat.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class AuthenticationController {
    AuthenticationService authenticationService;
    Map<String, String> knownUserSessions = new HashMap<String, String>();

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "/login")
    public String login(Model model, HttpSession session) {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        System.out.println("I am in login with email='" + email + "' and password='" + password + "'");

        User user = authenticationService.getUserByCredentials(email, password);

        if (user != null) {
            model.addAttribute("user", user);

            String sessionId = UUID.randomUUID().toString();
            knownUserSessions.put(sessionId, email);
            model.addAttribute("sessionId", sessionId);

            session.setAttribute("sessionId", sessionId);

            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Invalid login!");
            return "login";
        }


    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String doLogout(@RequestParam("sessionId") String sessionId, Model model, HttpSession session) {
        System.out.println("Logging out");
        if (knownUserSessions.containsKey(sessionId)) {
            knownUserSessions.remove(sessionId);
            session.removeAttribute("sessionId");
        }

        return "redirect:/login";
    }

    @GetMapping(value = "/register")
    public String signup(Model model, HttpSession session) {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String doSignup(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("password") String password, Model model, HttpSession session){
        System.out.println("I am in register with email='" + email + "', name='" + name + "' and password='" + password + "'");
        String error = null;
        if (email.equals("") || password.equals("") || name.equals("")) {
            error = "email, name and password are required";
        } else if (authenticationService.userExists(email)) {
            error = "this email is taken";
        }

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            model.addAttribute("password", password);
            return "register";
        } else {
            try {
                User user = authenticationService.registerUser(email, password, name);

                String sessionId = UUID.randomUUID().toString();
                knownUserSessions.put(sessionId, email);
                session.setAttribute("sessionId", sessionId);
                model.addAttribute("sessionId", sessionId);
                model.addAttribute("user", user);
                return "redirect:/profile";
            } catch (Exception ex) {
                ex.printStackTrace();
                model.addAttribute("error", "Server error, could not register user!");
                model.addAttribute("email", email);
                model.addAttribute("name", name);
                model.addAttribute("password", password);
                return "register";
            }
        }
    }

    @GetMapping(value = "/profile")
    public String profile(Model model, HttpSession session) {
        if (session.getAttribute("sessionId") == null || "".equals(session.getAttribute("sessionId"))) {
            return "redirect:/login";
        } else {
            String sessionId = session.getAttribute("sessionId").toString();
            model.addAttribute("sessionId", sessionId);

            User user = authenticationService.getUserByEmail(knownUserSessions.get(sessionId));

            if (user == null) {
                if (knownUserSessions.containsKey(sessionId)) {
                    knownUserSessions.remove(sessionId);
                    session.removeAttribute("sessionId");
                }
                return "redirect:/login";
            }

            model.addAttribute("user", user);

            return "profile";
        }

    }

    @GetMapping(value = "/edit-profile")
    public String editProfile(Model model, HttpSession session) {
        if (session.getAttribute("sessionId") == null || "".equals(session.getAttribute("sessionId"))) {
            return "redirect:/login";
        } else {
            String sessionId = session.getAttribute("sessionId").toString();
            model.addAttribute("sessionId", sessionId);

            User user = authenticationService.getUserByEmail(knownUserSessions.get(sessionId));

            if (user == null) {
                if (knownUserSessions.containsKey(sessionId)) {
                    knownUserSessions.remove(sessionId);
                    session.removeAttribute("sessionId");
                }
                return "redirect:/login";
            }

            model.addAttribute("imgSrc", user.getImgSrc());
            model.addAttribute("fullname", user.getName());
            model.addAttribute("birthdate", user.getBirthdate());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("race", user.getRace());
            model.addAttribute("hobbies", user.getHobbies());

            return "edit-profile";
        }

    }

    @RequestMapping(value = "/edit-profile", method = RequestMethod.POST)
    public String doUpdateProfile(@RequestParam("imgSrc") String imgSrc, @RequestParam("fullname") String fullname, @RequestParam("birthdate") String birthdate, @RequestParam("email") String email, @RequestParam("race") String race, @RequestParam("hobbies") String hobbies, Model model, HttpSession session){
        System.out.println("I am updating profile: imgSrc='" + imgSrc + "', fullname='" + fullname + "', birthdate='" + birthdate + "', email='" + email + "', race='" + race + "' and hobbies='" + hobbies + "'");
        if (session.getAttribute("sessionId") == null || "".equals(session.getAttribute("sessionId"))) {
            return "redirect:/login";
        } else {
            String sessionId = session.getAttribute("sessionId").toString();
            model.addAttribute("sessionId", sessionId);
            User user = authenticationService.getUserByEmail(knownUserSessions.get(sessionId));
            String error = null;

            if (email.equals("") || fullname.equals("")) {
                error = "email and name are required";
            }

            if (error != null) {
                model.addAttribute("error", error);
                model.addAttribute("imgSrc", imgSrc);
                model.addAttribute("fullname", fullname);
                model.addAttribute("birthdate", birthdate);
                model.addAttribute("email", email);
                model.addAttribute("race", race);
                model.addAttribute("hobbies", hobbies);
                return "edit-profile";
            } else {
                try {
                    user.setImgSrc(imgSrc);
                    user.setName(fullname);
                    user.setBirthdate(birthdate);
                    user.setEmail(email);
                    user.setRace(race);
                    user.setHobbies(hobbies);

                    authenticationService.updateUser (user);

                    knownUserSessions.put(sessionId, email);
                    model.addAttribute("user", user);

                    return "redirect:/profile";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    model.addAttribute("error", "Server error, could not register user!");
                    model.addAttribute("imgSrc", imgSrc);
                    model.addAttribute("fullname", fullname);
                    model.addAttribute("birthdate", birthdate);
                    model.addAttribute("email", email);
                    model.addAttribute("race", race);
                    model.addAttribute("hobbies", hobbies);
                    model.addAttribute("user", user);
                    return "edit-profile";
                }
            }
        }


    }
}
