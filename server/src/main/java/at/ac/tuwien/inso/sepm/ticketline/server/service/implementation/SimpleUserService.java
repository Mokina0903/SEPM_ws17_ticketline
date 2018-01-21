package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.AlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalValueException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    public SimpleUserService(UserRepository userRepository, NewsRepository newsRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public User findByUsername(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(NotFoundException::new);
    }

    @Override
    public User findOneByName( String name ) {
        return userRepository.findOneByUserNameIgnoreCase(name);
    }

    @Override
    public User createUser(User user) {
        String username = user.getUserName();
        String password = user.getPassword();
        Integer role = user.getRole();

        if (username == null || username.trim().isEmpty())
            throw new EmptyFieldException("username");

        if (password == null || password.trim().isEmpty())
            throw new EmptyFieldException("password");

        if (role == null) {
            throw new EmptyFieldException("role");
        }

        if (role < 1 || role > 2)
            throw new IllegalValueException("role (1 Admin / 2 User) = " + role);

        if (userRepository.findOneByUserNameIgnoreCase(user.getUserName()) != null)
            throw new AlreadyExistsException(user.getUserName());

        User newUser = User.builder()
            .userName(username)
            .password((new BCryptPasswordEncoder(10)).encode(password))
            .notSeen(newsRepository.findNewsforNewUser())
            .role(role)
            .build();

        save(newUser);

        return this.findByUsername(username);
    }

    @Override
    public void save(User user ) {
        userRepository.save(user);
    }


    @Override
    public void updateNotSeen( Long userId, Long newsId ) {
        userRepository.updateNotSeen(userId,newsId);
    }

    @Override
    public boolean blockUser(User user) {
        user.setBlocked(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean unblockUser(User user) {
        user.setBlocked(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public User resetPassword(User user) {
        user.setPassword((new BCryptPasswordEncoder(10)).encode(user.getPassword()));
        return userRepository.save(user);
    }
}
