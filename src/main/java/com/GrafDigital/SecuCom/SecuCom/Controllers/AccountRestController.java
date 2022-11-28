package com.GrafDigital.SecuCom.SecuCom.Controllers;

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import com.GrafDigital.SecuCom.SecuCom.Repositories.AppUserRepository;
import com.GrafDigital.SecuCom.SecuCom.Services.AccountService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // Identifier la classe comme Controllers
@AllArgsConstructor // Pour l'injections des dependances, AccountService;
@RequestMapping("/SecuCom")
public class AccountRestController {
  // Injectons la dependance
  public final AccountService accountService;
  private final AppUserRepository appUserRepository;


  // Une méthode qui permet de retourner une liste des Users
  @GetMapping("/users")
  //@PostAuthorize("hasAuthority('USER')")
  public List<AppUser> appUsers(){

    return accountService.listUsers();
  }

//  @GetMapping("/users")
//  public AppUser getUser(Long id){
//
//    return appUserRepository.findAllById(id).get();
//  }
//
//  @GetMapping("/login")
//  public String loadUserByUserName(String userName) {
//
//    return ("Bonjour" + accountService.loadUserByUserName(userName));
//  }

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
