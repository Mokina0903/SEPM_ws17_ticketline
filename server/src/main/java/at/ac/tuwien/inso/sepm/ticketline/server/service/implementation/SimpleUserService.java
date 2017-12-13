package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    // TODO: Check Merge
    @Override
    public User findByUsername(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(NotFoundException::new);
    }

    @Override
    public User findOneByName( String name ) {
        return userRepository.findOneByUserName(name);
    }

    @Override
    public User createUser(User user) {
        // TODO: Implement here
        return null;
    }

    @Override
    public void save( User user ) {
        userRepository.save(user);
    }


    @Override
    public void updateNotSeen( Long userId, Long newsId ) {
        userRepository.updateNotSeen(userId,newsId);
    }

    @Override
    public boolean blockUser(User user) {
        user.setBlocked(true);
        return false;
    }

    @Override
    public User resetPassword(User user) {
        // TODO: Implement
        return null;
    }
}
