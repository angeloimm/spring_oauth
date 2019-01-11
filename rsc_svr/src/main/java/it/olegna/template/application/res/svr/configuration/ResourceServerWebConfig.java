package it.olegna.template.application.res.svr.configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan({ "it.olegna.template.application.resource.server.rest" })
public class ResourceServerWebConfig implements WebMvcConfigurer {

	public ResourceServerWebConfig() {
		super();
	}
}