package com.example.denisproject.config;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.extractor.IasXsuaaExchangeBroker;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;
import com.sap.cloud.security.xsuaa.tokenflows.XsuaaTokenFlows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final XsuaaServiceConfiguration xsuaaServiceConfiguration;
    private final XsuaaTokenFlows xsuaaTokenFlows;

    @Autowired
    public SecurityConfig(XsuaaServiceConfiguration xsuaaServiceConfiguration, XsuaaTokenFlows xsuaaTokenFlows) {
        this.xsuaaServiceConfiguration = xsuaaServiceConfiguration;
        this.xsuaaTokenFlows = xsuaaTokenFlows;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS) // session is created by approuter
                .and()
                .authorizeRequests()
                .antMatchers(GET, "/goodbye").hasAuthority("Read")
                .antMatchers(GET, "/hello").hasAuthority("Admin")
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .bearerTokenResolver(new IasXsuaaExchangeBroker(xsuaaTokenFlows))
                .jwt()
                .jwtAuthenticationConverter(getJwtAuthenticationConverter());
    }

    /**
     * Customizes how GrantedAuthority are derived from a Jwt
     */
    Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
        TokenAuthenticationConverter converter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
        converter.setLocalScopeAsAuthorities(true);
        return converter;
    }
}
