package eu.fbk.dslab.playful.engine.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Value("${spring.security.oauth2.client.provider.oauthprovider.issuer-uri}")
    private String jwtIssuerUri;

    @Value("${spring.security.oauth2.client.registration.oauthprovider.client-id}")
    private String jwtAudience;
    
    @Value("${security.x-auth-token}")
    private String xAuthToken;
    
    @Bean
    @Order(1)
    public SecurityFilterChain xauthFilterChain(HttpSecurity http) throws Exception {
    	http.securityMatcher("/api/ext/**")
        	.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        	.addFilterAfter(xauthRequestHeaderAuthenticationFilter(), HeaderWriterFilter.class)
        	.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	    	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        	.csrf(csfr -> csfr.disable());
        return http.build();
    }
    
    @Bean
    public AuthenticationManager xauthAuthenticationManager() {
        return new ProviderManager(Collections.singletonList(xauthRequestHeaderAuthenticationProvider()));
    }
    
    @Bean
    public RequestHeaderAuthenticationFilter xauthRequestHeaderAuthenticationFilter() {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setPrincipalRequestHeader("x-auth");
        filter.setExceptionIfHeaderMissing(false);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/ext/**"));
        filter.setAuthenticationManager(xauthAuthenticationManager());
        return filter;
    }
    
    @Bean
    public AuthenticationProvider xauthRequestHeaderAuthenticationProvider() {
    	return new XAuthRequestHeaderAuthenticationProvider();
    }
    
    public class XAuthRequestHeaderAuthenticationProvider implements AuthenticationProvider {    	
    	@Override
    	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    		String authSecretKey = String.valueOf(authentication.getPrincipal());
    		if(StringUtils.isBlank(authSecretKey) || !authSecretKey.equals(xAuthToken)) {
    			throw new BadCredentialsException("Bad Request Header Credentials");
    		}
            return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), null, new ArrayList<>());	
    	}

    	@Override
    	public boolean supports(Class<?> authentication) {
    		return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    	}
    }
    
    @Bean
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
    	http.securityMatcher("/api/**")
    		.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
    		.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
    		// disable request cache, we override redirects but still better enforce it
    		.requestCache((requestCache) -> requestCache.disable())
	    	// we don't want a session for a REST backend
	    	// each request should be evaluated
	    	.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	    	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	    	.csrf(csfr -> csfr.disable());
        return http.build();
    }
    
    /*
     * JWT decoder with audience validation
     */

    @Bean
    public JwtDecoder jwtDecoder() {
        // build validators for issuer + timestamp (default) and audience
        OAuth2TokenValidator<Jwt> audienceValidator = new JwtAudienceValidator(jwtAudience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(jwtIssuerUri);
        
        // build default decoder and then use custom validators
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(jwtIssuerUri);
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator));

        return jwtDecoder;
    }
    
	class JwtAudienceValidator implements OAuth2TokenValidator<Jwt> {
	
	    public final OAuth2Error error = new OAuth2Error("invalid_token", "The required audience is missing", null);
	
	    private final String aud;
	
	    public JwtAudienceValidator(String audience) {
	        Assert.hasText(audience, "A non-empty audience is required");
	        this.aud = audience;
	    }
	
	    public OAuth2TokenValidatorResult validate(Jwt jwt) {
	        if (jwt.getAudience().contains(aud)) {
	            return OAuth2TokenValidatorResult.success();
	        } else {
	            return OAuth2TokenValidatorResult.failure(error);
	        }
	    }
	}
	
    @Bean
    @Order(3) 
	public SecurityFilterChain filterChainApp3(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
        	auth.anyRequest().permitAll();	
        })
        .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }
    
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE")); 
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }    
}
