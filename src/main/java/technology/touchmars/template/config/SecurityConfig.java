/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package technology.touchmars.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.CorsFilter;

import technology.touchmars.feign.wechat.client.api.MiniProgramUnionApiClient;
import technology.touchmars.template.repository.AuthorityRepository;
import technology.touchmars.template.repository.UserConnectionRepository;
import technology.touchmars.template.repository.UserConnectionWechatRepository;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.*;

/**
 * Security Configuration.
 * @author Craig Walls
 */
//@EnableWebSecurity
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Environment environment;
//	@Autowired
//	private JwtSocialAuthenticationSuccessHandler jwtSocialAuthenticationSuccessHandler;
	
	@Autowired
	private UserRepository userRepository;	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private UserConnectionRepository userConnectionRepository;
	@Autowired
	private UserConnectionWechatRepository userConnectionWechatRepository;
	@Autowired
	private MiniProgramUnionApiClient miniProgramUnionApiClient;

	@Autowired
    private JwtTokenService jwtTokenService;    // from FirebaseConfig

	@Autowired(required=false)
	private RequestAuthorityService requestAuthorityService;
	@Autowired(required=false)
	private PostSignupService postSignupService;
	@Autowired(required=false)
	private JwtClaimService jwtClaimService;

	@Autowired
	private CorsFilter corsFilter;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.csrf().disable()
				.authorizeRequests()
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()		
					.antMatchers("/rest/signup").permitAll()
					.antMatchers("/rest/signin/*").permitAll()
					.antMatchers("/public/**").permitAll()
//					.antMatchers(HttpMethod.POST,"/public/**").permitAll()
					.antMatchers("/", "/webjars/**", "/admin/**", "/favicon.ico", "/resources/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
					.antMatchers("/**").authenticated()				
			.and()
				.addFilterBefore(new JwtAuthenticationFilter(userRepository, jwtTokenService), AbstractPreAuthenticatedProcessingFilter.class)
				.addFilterBefore(getWechatMiniAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.addFilterBefore(getGoogleServerSideAppAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.addFilterBefore(corsFilter, JwtAuthenticationFilter.class)
				.addFilterBefore(getJwtUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth
	    	.userDetailsService(userDetailsService)
	    	.passwordEncoder(passwordEncoder())
	    ;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

    //JWT Stateless

    public JwtUsernamePasswordAuthenticationFilter getJwtUsernamePasswordAuthenticationFilter() throws Exception{
	    	JwtUsernamePasswordAuthenticationFilter filter =
                    new JwtUsernamePasswordAuthenticationFilter(userRepository, jwtTokenService, jwtClaimService);
	    	filter.setAuthenticationManager(this.authenticationManager());
	    	return filter;
    }

    // Wechat
    
    @Bean
    public WechatMiniProgramDiscoveryService getWechatMiniProgramDiscoveryService() {
    		return new WechatMiniDiscoveryServiceImpl(this.environment);
    }

	@Bean
	public WechatMiniAuthenticationFilter getWechatMiniAuthenticationFilter() throws Exception {
		WechatMiniAuthenticationFilter filter = 
			new WechatMiniAuthenticationFilter(getWechatMiniProgramDiscoveryService(),
				miniProgramUnionApiClient, requestAuthorityService, userRepository,
				userConnectionWechatRepository, authorityRepository, jwtTokenService, jwtClaimService, null);
		filter.setAuthenticationManager(this.authenticationManager());
		return filter;
	}
	
	// Google
	
	@Bean
	public GoogleAppDiscoveryService getGoogleAppDiscouveryService() {
		return new GoogleAppDiscoveryServiceImpl(this.environment);
	}
	
	@Bean
	public GoogleServerSideAppAuthenticationFilter getGoogleServerSideAppAuthenticationFilter() throws Exception {
		GoogleServerSideAppAuthenticationFilter filter =  
			new GoogleServerSideAppAuthenticationFilter(
				getGoogleAppDiscouveryService(),
				requestAuthorityService,
				postSignupService,
				userRepository,
				userConnectionRepository,
				authorityRepository,
                jwtTokenService,
                jwtClaimService,
            null
			);		
		filter.setAuthenticationManager(this.authenticationManager());
		return filter;
	}


}
