package oauth2

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication

class OAuth2ApiFilters {

  def springSecurityService

  def filters = {
    userCheck(controller: 'auth|user|media', action: 'profile|update|changePassword|logout|getMedia|saveMedia') {
      before = {
        return authenticated(response)
      }
    }
  }

  def authenticated(response) {
    AbstractAuthenticationToken auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth instanceof OAuth2Authentication) {
      def principal = auth.principal
      if (principal instanceof String) {
        springSecurityService.reauthenticate principal
      }
    } else {
      response.sendError(403)
      return false;
    }
    return true;
  }
}
