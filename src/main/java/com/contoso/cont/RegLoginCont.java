package com.contoso.cont;

import com.contoso.models.Users;
import com.contoso.models.enums.Roles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegLoginCont extends Global {

    @GetMapping("/reg")
    public String reg(Model model) {
        addAttributes(model);
        return "reg";
    }

    @PostMapping("/reg")
    public String regUser(Model model,@RequestParam String username, @RequestParam String fio, @RequestParam String password, @RequestParam String passwordRepeat) {
        if (repoUsers.findByUsernameAndPassword(username, password) != null) {
            model.addAttribute("message", "Пользователь с таким логином и паролем уже существует");
            addAttributes(model);
            return "reg";
        }

        if (!password.equals(passwordRepeat)) {
            model.addAttribute("message", "Некорректный ввод паролей");
            addAttributes(model);
            return "reg";
        }

        repoUsers.save(new Users(username, password, fio, Roles.Сотрудник, defAvatar));

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String Login(Model model) {
        addAttributes(model);
        return "login";
    }

}
