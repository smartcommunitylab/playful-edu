package eu.fbk.dslab.playful.engine.conf;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/*
 * extend WebMvcConfigurerAdapter and not use annotation @EnableMvc to permit correct static
 * resources publishing and restController functionalities
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(25000);
        return new RestTemplate(factory);        
    }
    
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public GroupedOpenApi extOpenApi() {
       String paths[] = {"/api/ext/**"};
       return GroupedOpenApi.builder().group("external")
    		   .pathsToMatch(paths)
    		   .packagesToScan("eu.fbk.dslab.playful.engine.rest")    		   
    		   .addOpenApiCustomizer(extApiCustomizer()).build();
    }
    
    @Bean
    public GroupedOpenApi jwtOpenApi() {
       String paths[] = {"/api/**"};
       return GroupedOpenApi.builder().group("jwt")
    		   .pathsToMatch(paths)
    		   .pathsToExclude("/api/ext/**")
    		   .packagesToScan("eu.fbk.dslab.playful.engine.rest")
    		   .addOpenApiCustomizer(jwtApiCustomizer()).build();
    }
    
    public OpenApiCustomizer extApiCustomizer() {
		return openApi -> {
			final String securitySchemeName = "apiKey";
			openApi.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
			openApi.getComponents().addSecuritySchemes(securitySchemeName,
					new SecurityScheme()
							.type(SecurityScheme.Type.APIKEY)
							.in(SecurityScheme.In.HEADER)
							.name("x-auth"));
		};
    }
    
    public OpenApiCustomizer jwtApiCustomizer() {
    	return openApi -> {
			final String securitySchemeName = "bearerAuth";
			openApi.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
			openApi.getComponents().addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                    		.name(securitySchemeName)
                    		.type(SecurityScheme.Type.HTTP)
                    		.scheme("bearer")
                    		.bearerFormat("JWT"));
    	};
    }
    
	@Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Playful Education Project")
                .version("1.0.0")
                .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                .description("DSLab")
                .url("https://www.smartcommunitylab.it/"));
    }

}
