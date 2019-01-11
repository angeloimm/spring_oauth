package it.olegna.template.application.auth.svr.configuration;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer
{

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		WebMvcConfigurer.super.configureMessageConverters(converters);
	}


}