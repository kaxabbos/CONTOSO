package com.contoso.cont;

import com.contoso.models.Users;
import com.contoso.models.enums.Roles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddUserCont extends Global {

    @GetMapping("/addUser")
    public String AddUser(Model model) {
        AddAttributesAddUser(model);
        return "addUser";
    }

    @PostMapping("/add/user")
    public String AddNewUser(Model model,@RequestParam String username, @RequestParam String password, @RequestParam String fio, @RequestParam Roles role) {
        if (repoUsers.findByUsernameAndPassword(username, password) != null) {
            model.addAttribute("message", "Пользователь с таким логином и паролем уже существует");
            AddAttributesAddUser(model);
            return "addUser";
        }
        repoUsers.save(new Users(username, password, fio, role, defAvatar));
        return "redirect:/addUser";
    }
}
