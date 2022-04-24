package org.temkarus0070.application.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.temkarus0070.AppConfig;
import org.temkarus0070.WebConfig;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;


public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    public static final Charset DEFAULT_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    public SecurityWebApplicationInitializer() {
        super(WebApplicationConfig.class, WebSecurityConfig.class);
    }

    static CharacterEncodingFilter createCharacterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(DEFAULT_CHARACTER_ENCODING.name());
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    static ResourceUrlEncodingFilter createResourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        insertFilters(servletContext,
                createCharacterEncodingFilter(),
                createResourceUrlEncodingFilter());
    }

    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        servletContext.getFilterRegistration(DEFAULT_FILTER_NAME)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD), true, "/authenticate");
    }

    @Configuration
    @Import({WebConfig.class, AppConfig.class, WebSecurityConfig.class})
    public static class WebApplicationConfig {
    }
}