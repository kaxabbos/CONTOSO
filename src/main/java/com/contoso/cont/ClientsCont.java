package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import com.contoso.models.Clients;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientsCont extends Attributes {

    @GetMapping("/clients")
    public String Clients(Model model) {
        AddAttributesClients(model);
        return "clients";
    }

    @PostMapping("/client/add")
    public String ClientAdd(@RequestParam String fio, @RequestParam String tel) {
        repoClients.save(new Clients(fio, tel));
        return "redirect:/clients";
    }

    @PostMapping("/client/{id}/edit")
    public String ClientEdit(@RequestParam String fio, @RequestParam String tel, @PathVariable Long id) {
        Clients client = repoClients.getById(id);
        client.setFio(fio);
        client.setTel(tel);
        repoClients.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/client/{id}/delete")
    public String ClientDelete(@PathVariable Long id) {
        repoClients.deleteById(id);
        return "redirect:/clients";
    }
}
