package eu.fbk.dslab.playful.engine.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
import org.springframework.util.Assert;

@Configuration
public class SecurityConfig {
    @Value("${spring.security.oauth2.client.provider.oauthprovider.issuer-uri}")
    private String jwtIssuerUri;

    @Value("${spring.security.oauth2.client.registration.oauthprovider.client-id}")
    private String jwtAudience;
    
    @Bean
    public SecurityFilterChain filterChainAPI(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
        	auth.requestMatchers("/api/**").authenticated();
        	auth.anyRequest().permitAll();
        });
    	http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
    	// disable request cache, we override redirects but still better enforce it
    	http.requestCache((requestCache) -> requestCache.disable());
    	// we don't want a session for a REST backend
    	// each request should be evaluated
    	http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    	http.csrf(csfr -> csfr.disable());
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
}
