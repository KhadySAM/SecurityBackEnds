package com.GrafDigital.SecuCom.SecuCom.Controllers;

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import com.GrafDigital.SecuCom.SecuCom.Services.AccountService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Identifier la classe comme Controllers
@RequestMapping("/SecuCom") // Nom de path dans l'URL
@AllArgsConstructor // Pour l'injections des dependances, AccountService;
public class AccountRestController {
    // Injectons la dependance
    public final AccountService accountService;

    // Une méthode qui permet de retourner une liste des Users
    @GetMapping("/users")
    //@PostAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers(){
        return accountService.listUsers();
    }

    // Une méthode qui permet d'ajouter un User
    @PostMapping("/AddUser")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public AppUser saveUser(@RequestBody AppUser appUser){ // @RequestBody pour prendre les données de qui se trouve dans le Body
        return accountService.addNewUser(appUser);
    }

    // Une méthode qui permet d'ajouter un Rôle
    @PostMapping("/addRole")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }

    // Une méthode qui permet d'Affecter un rôle à un User
    @PostMapping("/addRoleToUser")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
         accountService.addRoleToUser(roleUserForm.getUserName(), roleUserForm.getRoleName());
    }

}

@Getter
@Setter
class RoleUserForm{
    private String userName;
    private String roleName;
}
