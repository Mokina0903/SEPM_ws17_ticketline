package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

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

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public void updateNotSeen( User user ) {
        userRepository.updateNotSeen(user.getNotSeen(),user.getId());
    }

    @Override
    public boolean blockUser(User user) {
        user.setBlocked(true);
        return false;
    }
}
