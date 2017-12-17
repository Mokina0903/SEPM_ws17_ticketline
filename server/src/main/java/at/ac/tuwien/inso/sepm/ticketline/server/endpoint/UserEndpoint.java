package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalValueException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.AnnotationValue;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
@Api(value = "user")
public class UserEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;
    private final NewsMapper newsMapper;
    private final Integer LOGIN_ATTEMPTS = 5;

    @Autowired
    UserRepository userRepository;

    public UserEndpoint(UserService userService, UserMapper userMapper, NewsMapper newsMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.newsMapper = newsMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get list of simple user entries")
    public List<SimpleUserDTO> findAll() {
        return userMapper.userToSimpleUserDTO(userService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific user entry")
    public DetailedUserDTO find(@PathVariable Long id) {
        return userMapper.userToDetailedUserDTO(userService.findOne(id));
    }

    @RequestMapping(value = "/find/{userName}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific user entry by name")
    public DetailedUserDTO findByName(@PathVariable("userName") String userName) {
        User user=userService.findOneByName(userName);
        DetailedUserDTO userDTO = userMapper.userToDetailedUserDTO(user);
        userDTO.setNotSeen(newsMapper.newsToSimpleNewsDTO(user.getNotSeen()));
        return userDTO;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create a new user entry")
    public SimpleUserDTO createUser(@RequestBody SimpleUserDTO simpleUserDTO) {
        User user = null;
        try {
            user = userMapper.simpleUserDTOToUser(simpleUserDTO);
        } catch (NullPointerException e) {
            throw new EmptyFieldException("");
        }
        user = userService.createUser(user);
        return userMapper.userToSimpleUserDTO(user);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific user entry")
    public SimpleUserDTO find(@PathVariable String userName) {
        return userMapper.userToSimpleUserDTO(userService.findByUsername(userName));
    }


    @RequestMapping(value = "/{username}/getAttempts", method = RequestMethod.GET)
    @ApiOperation(value = "Get left login attempts of a specific user entry")
    public Integer getLoginAttemptsLeft(@PathVariable String username) {

        User user = userService.findByUsername(username);
        return user.getAttempts();
    }

    @RequestMapping(value = "/{userId}/{newsId}", method = RequestMethod.POST)
    @ApiOperation(value = "remove news from notSeen")
    public void removeFromUserNotSeen(@PathVariable Long userId, @PathVariable Long newsId) {

        User user = userService.findOne(userId);
        for(News news:user.getNotSeen()){
            if(news.getId().equals(newsId) || news.getId() == newsId){
                List<News> notSeen= user.getNotSeen();
                notSeen.remove(news);
                user.setNotSeen(notSeen);

                userService.save(user);
                return;
            }

        }

       // userService.updateNotSeen(userId,newsId);
       // userService.save(user);
    }

    /*
    @RequestMapping(value = "/decreaseAttempts", method = RequestMethod.POST)
    @ApiOperation(value = "Decrease login attempts of a specific user entry")
    public void decreaseLoginAttemptsOfUser(@RequestBody String username) {

        User user = userService.findByUsername(username);
        user.setAttempts(user.getAttempts()-1);
        userService.save(user);
    }
    */

    /*
    @RequestMapping(value = "/resetAttempts", method = RequestMethod.POST)
    @ApiOperation(value = "Decrease login attempts of a specific user entry")
    public void resetAttempts(@RequestBody String username) {

        User user = userService.findByUsername(username);
        user.setAttempts(LOGIN_ATTEMPTS);
        userService.save(user);
    }
    */

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Reset a specific users password")
    public DetailedUserDTO resetUserPassword(@RequestBody SimpleUserDTO simpleUserDTO) {
        User user = userService.findByUsername(simpleUserDTO.getUserName());
        user.setPassword(simpleUserDTO.getPassword());
        user = userService.resetPassword(user);
        return userMapper.userToDetailedUserDTO(user);
    }

    @RequestMapping(value = "/block", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Block a specific user entry")
    public void blockUser(@RequestBody String username) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String asked = ((UserDetails)principal).getUsername();

        if (asked.trim().toUpperCase().equals(username.trim().toUpperCase())) {
            throw new IllegalValueException("querist == username");
        }

        User user = userService.findByUsername(username);
        userService.blockUser(user);
    }


    @RequestMapping(value = "/unblock", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Unblock a specific user entry")
    public void unblockUser(@RequestBody String username) {
        User user = userService.findByUsername(username);
        userService.unblockUser(user);
    }

    @RequestMapping(value = "/{username}/isBlocked", method = RequestMethod.GET)
    @ApiOperation(value = "Get left login attempts of a specific user entry")
    public boolean isBlocked(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return user.isBlocked();
    }

}
