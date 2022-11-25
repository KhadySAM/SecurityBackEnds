package com.GrafDigital.SecuCom.SecuCom;

import com.GrafDigital.SecuCom.SecuCom.Filters.JwtAuthentificationFilter;
import com.GrafDigital.SecuCom.SecuCom.Filters.JwtAuthorizationFilter;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import com.GrafDigital.SecuCom.SecuCom.Services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

// Cette classe va étendre de la classe WebSecurityConfigurerAdapter;
@Configuration // une classe de configuration
@EnableWebSecurity
@AllArgsConstructor // Pour l'injections de service pour prendre la méthode qui va chargé le user
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // injectons notre service
    private AccountService accountService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Ici on Idenfitie les Users qui ont le Droit d'accéder;

        // Repérer le User avec ses droits grâce à la méthode loadUserByUserName;
        auth.userDetailsService(new UserDetailsService() { // on le donne un Objet qui implement de l'interface UserDetailsService
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // recupérer le User dans la base de donnée
                AppUser appUser = accountService.loadUserByUserName(username);

                Collection<GrantedAuthority> authorities = new ArrayList<>();
                appUser.getAppRoles().forEach(r->{
                    authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
                });

                // On retourne un User de Spring et compare les informations
                return new User(appUser.getUserName(), appUser.getPassword(), authorities);
            }
        });

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthentificationFilter jwtAuthentificationFilter = new JwtAuthentificationFilter(authenticationManagerBean());
        jwtAuthentificationFilter.setFilterProcessesUrl("/SecuCom/login");
        // Ici on spéficie les Droits d'Accès;

        // Désactivé le CSRF vu qu'on va utilisé une session StateLess
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Gesion des Doits
        http.authorizeRequests().antMatchers("/SecuCom/login/**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/SecuCom/users/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(POST, "/SecuCom/AddUser/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/SecuCom/addRole/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/SecuCom/addRoleToUser/**").hasAnyAuthority("ADMIN");

        // Toutes les requêtte doivent être authentifier;
        http.authorizeRequests().anyRequest().authenticated();

        // Intégré le Filtre qu'on vient de créer
        http.addFilter(jwtAuthentificationFilter);  

        // Le Filtre qui va intercepté toutes les requêtes
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // Créons cette méthode pour la passer à JwtAuthentificationFilter();
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
