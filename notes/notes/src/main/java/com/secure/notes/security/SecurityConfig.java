package com.secure.notes.security;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.secure.notes.config.OAuth2LoginSuccessHandler;
import com.secure.notes.security.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthenticationEntryPoint unAuthorized;

	@Autowired
	private AuthTokenFilter authTokenFilter;
	
	@Autowired
	@Lazy
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

//	@Autowired
//	private CustomLoggingFilter customLoggingFilter;

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.ignoringRequestMatchers("/api/auth/public/**"))
				// http.csrf(AbstractHttpConfigurer::disable);
				.authorizeHttpRequests((requests) -> requests.requestMatchers("/api/note/**").hasRole("ADMIN")
						.requestMatchers("/api/audit/**").hasRole("ADMIN").requestMatchers("/api/csrf-token")
						.permitAll().requestMatchers("/api/auth/public/**").permitAll()
						.requestMatchers("/oauth2/**").permitAll()

						.anyRequest().authenticated())
				.oauth2Login(oauth2 -> {
					oauth2.successHandler(oAuth2LoginSuccessHandler);

				}).addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unAuthorized))

				.formLogin(withDefaults()).httpBasic(withDefaults());
		return http.build();
	}
//
//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/api/admin/**").hasRole("USER")
//                        .requestMatchers("/api/csrf-token").permitAll()
//                        .requestMatchers("/admin/**").denyAll()
//                        .anyRequest().authenticated())
////                .sessionManagement(session -> session
////                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // .addFilterBefore(customLoggingFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(withDefaults())
//                .httpBasic(withDefaults());
//
//        return http.build();
//    }

//	@Bean
//	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//		//http
//				// .csrf(csrf -> csrf.disable())
////		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//		
//				http.csrf(csrf -> csrf
//						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//					.authorizeHttpRequests(requests -> requests
//								
//								.requestMatchers("/contact/**").permitAll()
//								.requestMatchers("/admin/**").denyAll()
//								.anyRequest().authenticated())
//					
//              .addFilterBefore(customLoggingFilter, UsernamePasswordAuthenticationFilter.class)
//           .addFilterAfter(new RequestValidationFilter(), CustomLoggingFilter.class)
//						// .formLogin(withDefaults());
//						.httpBasic(withDefaults()));
//		return http.build();
//	}

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	 UserDetailsService userDetailsService() {
//		
////		UserDetails user=User.withUsername("user").password("{noop}1122").roles("USER").build();
////		UserDetails admin=User.withUsername("admin").password("{noop}1122").roles("ADMIN"). build();
////       return new InMemoryUserDetailsManager(user,admin);
//		
//		JdbcUserDetailsManager manager=new JdbcUserDetailsManager(dataSource);
//		if(!manager.userExists("user")) {
//			manager.createUser(
//					User.withUsername("user")
//					.password("{noop}1122")
//					.roles("USER")
//					.build());
//		}
//			if(!manager.userExists("admin")) {
//				manager.createUser(
//				User.withUsername("admin")
//				.password("{noop}1122")
//				.roles("ADMIN")
//				. build());
//			}
//			return manager;
//		
//		
//	}
//	@Bean
//	public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
//		return args -> {
//			Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
//					.orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
//
//			Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//					.orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
//
//			if (!userRepository.existsByUserName("user1")) {
////                User user1 = new User("user1", "user1@example.com", "{noop}password1");
//				User user1 = new User("user1", "user1@example.com", encoder().encode("password1"));
//
//				user1.setAccountNonLocked(false);
//				user1.setAccountNonExpired(true);
//				user1.setCredentialsNonExpired(true);
//				user1.setEnabled(true);
//				user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//				user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
//				user1.setTwoFactorEnabled(false);
//				user1.setSignUpMethod("email");
//				user1.setRole(userRole);
//				userRepository.save(user1);
//			}
//
//			if (!userRepository.existsByUserName("admin")) {
//				// User admin = new User("admin", "admin@example.com", "{noop}adminPass");
//				User admin = new User("admin", "admin@example.com", encoder().encode("adminPass"));
//
//				admin.setAccountNonLocked(true);
//				admin.setAccountNonExpired(true);
//				admin.setCredentialsNonExpired(true);
//				admin.setEnabled(true);
//				admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//				admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
//				admin.setTwoFactorEnabled(false);
//				admin.setSignUpMethod("email");
//				admin.setRole(adminRole);
//				userRepository.save(admin);
//			}
//		};
//	}
}
