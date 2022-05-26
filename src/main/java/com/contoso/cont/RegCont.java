package com.contoso.cont;

import com.contoso.cont.general.AddAttribute;
import com.contoso.models.Users;
import com.contoso.models.enums.Roles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegCont extends AddAttribute {

    @GetMapping("/reg")
    public String reg() {
        return "reg";
    }

    @PostMapping("/reg")
    public String regUser(Model model, @RequestParam String username, @RequestParam String fio, @RequestParam String password, @RequestParam String passwordRepeat) {
        if (repoUsers.findByUsername(username) != null) {
            model.addAttribute("message", "Пользователь с таким логином уже существует");
            AddAttributes(model);
            return "reg";
        }

        if (!password.equals(passwordRepeat)) {
            model.addAttribute("message", "Некорректный ввод паролей");
            AddAttributes(model);
            return "reg";
        }

        repoUsers.save(new Users(username, password, fio, Roles.Сотрудник, defAvatar));

        return "redirect:/login";
    }
}
