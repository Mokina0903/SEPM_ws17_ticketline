//not needed by now

/*
package at.ac.tuwien.inso.sepm.ticketline.server.handler;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private String DEFAULT_FAILURE_URL = "/401";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        setDefaultFailureUrl(DEFAULT_FAILURE_URL + "/" + request.getParameter("username"));
        super.onAuthenticationFailure(request, response, exception);

        if (exception instanceof BadCredentialsException) {
            lockUser(request.getParameter("username"));
        }

    }

    private void lockUser(String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            int leftAttempts = user.getAttempts() - 1;
            user.setAttempts(leftAttempts);

            if (leftAttempts <= 0) {
                user.setBlocked(true);
            }

            userRepository.save(user);
        }
    }

}*/
