package technology.touchmars.template.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class TouchMarsAuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
//		if (authentication instanceof UsernamePasswordAuthenticationToken) {
//		    return ((UsernamePasswordAuthenticationToken)authentication).getName();
//        }
//		return ((TouchUser)authentication.getPrincipal()).getHashId();
        return Optional.of(authentication.getName());
	}

}
