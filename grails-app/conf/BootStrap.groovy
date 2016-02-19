import com.vellkare.core.Role
import com.vellkare.oauth.OAuthClient
import org.hibernate.SessionFactory

class BootStrap {

  SessionFactory sessionFactory
  def customMarshallerRegistrar
  def grailsApplication
  def init = { servletContext ->
    customMarshallerRegistrar.registerMarshallers()

    def session = sessionFactory.getCurrentSession();
    def transaction = session.getTransaction()

    try {
      transaction.begin()
      def clientId = grailsApplication.config?.grails?.client?.config?.clientId ?: 'velkare-client'
      def clientSecret = grailsApplication.config?.grails?.client?.config?.clientSecret ?: 'velkare-client'
      if (!OAuthClient.findByClientId(clientId)) {
        new OAuthClient(
          clientId: clientId,
          clientSecret: clientSecret,
          authorizedGrantTypes: ['authorization_code', 'refresh_token', 'implicit', 'password', 'client_credentials'],
          authorities: ['ROLE_CLIENT'],
          scopes: ['read', 'write'],
          redirectUris: ['http://localhost:8080/vellkare']
        ).save(flush: true, failOnError: true)
      }

      Role.findOrSaveWhere(authority: Role.Authority.ROLE_USER)
      Role.findOrSaveWhere(authority: Role.Authority.ROLE_ADMIN)

      transaction.commit()

    } catch (x) {
      if (transaction.isActive()) transaction.rollback()
      throw x;
    }
  }

  def destroy = {
  }
}
