package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import com.contoso.models.Users;
import com.contoso.models.enums.Roles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfilesCont extends Attributes {

    @GetMapping("/profiles")
    public String Profiles(Model model) {
        AddAttributesProfiles(model);
        return "profiles";
    }

    @PostMapping("/profiles/{id}/edit")
    public String ProfilesEdit(@PathVariable long id, @RequestParam Roles role) {
        Users user = repoUsers.findById(id).orElseThrow();
        user.setRole(role);
        repoUsers.save(user);
        return "redirect:/profiles";
    }

    @GetMapping("/profiles/{id}/delete")
    public String ProfilesDelete(Model model, @PathVariable long id) {
        if (getUserID() == id) {
            AddAttributesProfiles(model);
            model.addAttribute("message", "Вы не можете удалить свой профиль");
            return "profiles";
        }

        repoUsers.deleteById(id);

        return "redirect:/profiles";
    }


}
