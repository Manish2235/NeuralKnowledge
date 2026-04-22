package com.incapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.incapp.entity.MyUser;
import com.incapp.service.UserService;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	UserService userService;
	
	//Authorization
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {
		http.authorizeHttpRequests((requests) -> requests
		.requestMatchers("/","/signup","/viewPdf","/forget_password","/forget","/reset_password","/update_password","/images/**").permitAll()
		.requestMatchers("/dashboard").hasRole("USER")
		.requestMatchers("/upload_pdf").hasRole("USER")
//		.requestMatchers("/viewPdf").hasRole("USER")
		.requestMatchers("/upload").hasRole("USER")
		.requestMatchers("/ask_question").hasRole("USER")
		.requestMatchers("/my_document").hasRole("USER")
		.requestMatchers("/my_profile").hasRole("USER")
		.requestMatchers("/getProfileImage").hasRole("USER")
		.requestMatchers("/uploadProfileImage").hasRole("USER")
		.requestMatchers("/download").hasRole("USER")
		.requestMatchers("/change_password").hasRole("USER")
		.requestMatchers("/changePassword").hasRole("USER")
//		.requestMatchers("/adminHome").hasRole("ADMIN")
		.anyRequest().authenticated())
		
		//to allow iframe for pdf view 
		.headers(headers->headers.frameOptions(frame->frame.sameOrigin()))
		
		.formLogin((form) -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/dashboard").permitAll())

		// Google login
        
		.oauth2Login(oauth2 -> oauth2
        .loginPage("/oauth2/authorization/google") // Separate login page for Google OAuth2
        .defaultSuccessUrl("/dashboard", true)
        
        .userInfoEndpoint(userInfo -> userInfo
                .userService(new DefaultOAuth2UserService(){
                	@Override
                    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                        OAuth2User oauth2User = super.loadUser(userRequest);
                        
                        
                        // Extract user details
                        String email = oauth2User.getAttribute("email");
                        String phone = oauth2User.getAttribute("phone");
                        String name = oauth2User.getAttribute("name");
                        String pictureUrl = oauth2User.getAttribute("picture");
                       
                        
                        MyUser u=new MyUser();
                        u.setUsername(email);
                        u.setName(name);
                        userService.save(u);
                        
                        
                        // Set default role
                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

                        // Wrap the OAuth2User with additional authorities
                        HashMap<String,Object> h=new HashMap<>();
                        h.put("email", email);
                        h.put("pictureUrl", pictureUrl);
                        return new DefaultOAuth2User(Collections.singleton(authority), h ,"email");
                    }
        		}))
        .permitAll())
		
		.logout((logout) -> logout.permitAll())  //take you to login page , but optional
//		.logout((logout) -> logout.logoutSuccessUrl("/").permitAll()) //to redirect to specific page
		.exceptionHandling(handling -> handling.accessDeniedPage("/accessDenied"));
		
		return http.build();
	}
	
	//Authentication
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {

	    UserDetailsService customUserDetailsService = new UserDetailsService() {
	        @Override
	        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	            MyUser user = userService.getUserByUsername(username);
	            if (user == null) {
	                throw new UsernameNotFoundException("User not found");
	            }

	            return new User(
	                user.getUsername(),
	                user.getPassword(),
	                user.isEnabled(),
	                true,
	                true,
	                true,
	                List.of(new SimpleGrantedAuthority(user.getRole()))
	            );
	        }
	    };

	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(customUserDetailsService);
	    provider.setPasswordEncoder(userService.getEncoder());
	  //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

	    return provider;
	}

	}
	
		

