package com.dbserver.developertest.security;

import com.dbserver.developertest.dto.UserDetailDTO;
import com.dbserver.developertest.model.HungryProfessional;
import com.dbserver.developertest.repository.HungryProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, HungryProfessionalRepository hungryProfessionalRepository)
            throws Exception {
        if (hungryProfessionalRepository.count() == 0) {
            String admin = "admin";
            hungryProfessionalRepository.save(HungryProfessional.builder().nickname(admin).password(admin).name(admin).build());
        }

        builder.userDetailsService(nickname -> new UserDetailDTO(hungryProfessionalRepository.findByNicknameEquals(nickname)));
    }

}
