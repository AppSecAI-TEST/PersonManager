package person.tsln.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ControllerAdvice
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/login")
    public String showLoginPage() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered())
            return "redirect:redirect";
        return "auth/login";
    }

    @RequiresGuest
    @ResponseBody
    @PostMapping("/login")
    public LoginResult handleLogin(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean remember
    ) throws AuthenticationException {
        // 预处理
        if (remember == null) remember = false;
        if (code != null) code = code.trim().toUpperCase();
        email = email.trim();
        password = password.trim();
        // 保证输入不为空
        if (email.isEmpty() || password.isEmpty()) throw new UnknownAccountException();
        // 构造登陆Token
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        token.setRememberMe(remember);
        SecurityUtils.getSubject().login(token);
        // 登录成功
        return new LoginResult();
    }

    @RequiresUser
    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:login";
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    private LoginResult onLoginException(AuthenticationException e) {
        if (e instanceof UnknownAccountException)
            return new LoginResult(10, "用户不存在!");
        if (e instanceof IncorrectCredentialsException)
            return new LoginResult(20, "密码错误!");
        return new LoginResult(99, "系统发生未知错误! " + e.getClass().getSimpleName());
    }

    public static class LoginResult {
        private Integer code;
        private String message;

        LoginResult() {
            this(0, "");
        }

        LoginResult(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
