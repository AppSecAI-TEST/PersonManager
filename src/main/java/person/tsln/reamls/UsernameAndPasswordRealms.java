package person.tsln.reamls;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import person.tsln.beans.User;
import person.tsln.dao.UserDao;
import person.tsln.utils.PasswordUtils;

public class UsernameAndPasswordRealms extends AuthorizingRealm {
    @Autowired
    private UserDao dao;
    @Autowired
    private PasswordUtils utils;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (!(token instanceof UsernamePasswordToken)) throw new UnsupportedTokenException();

        // get username and password from token
        String email = ((UsernamePasswordToken) token).getUsername();
        String password = String.valueOf(((UsernamePasswordToken) token).getPassword());

        // find user from database ,if not exists then throw UnknownAccountException
        User user = dao.getUserByEmail(email);
        if (user == null) throw new UnknownAccountException();

        //val e = utils.getEncodedPassword(password)
        // compare input password and password for database
        // if the password is different from password for database, then throw IncorrectCredentialsException
        if (!utils.compare(password,user.getPassword())) throw new IncorrectCredentialsException();

        return new SimpleAuthenticationInfo(token.getPrincipal(),token.getCredentials(),this.getName());
    }
}
