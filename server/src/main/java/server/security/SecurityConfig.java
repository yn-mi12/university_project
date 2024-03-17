package server.security;

import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.Pointcuts;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = false)
public class SecurityConfig {

    private final AdminAuthProvider authenticationProvider;
    private final AdminAuthorizationManager adminAuthorizationManager;

    @Autowired
    public SecurityConfig(AdminAuthProvider authenticationProvider,
                          AdminAuthorizationManager adminAuthorizationManager) {
        this.authenticationProvider = authenticationProvider;
        this.adminAuthorizationManager = adminAuthorizationManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable defaults
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // Make the application stateless
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // disable csrf since the app is stateless
                .csrf(AbstractHttpConfigurer::disable)

                // Simple security chain which requires all requests to be authenticated
                .addFilterBefore(new AdminAuthFilter(authenticationManager()),
                        AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> {
                    authorize.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor adminAnnotationInterceptor() {
        Pointcut pointcut = Pointcuts.union(new AnnotationMatchingPointcut(null,
                        RequiresAdmin.class, true),
                new AnnotationMatchingPointcut(RequiresAdmin.class, true));
        return new AuthorizationManagerBeforeMethodInterceptor(pointcut, adminAuthorizationManager);
    }

}
