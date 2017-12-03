package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
@Api(value = "user")
public class UserEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    public UserEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple user entries")
    public List<SimpleUserDTO> findAll() {
        return userMapper.userToSimpleUserDTO(userService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific user entry")
    public DetailedUserDTO find(@PathVariable Long id) {
        return userMapper.userToDetailedUserDTO(userService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create a new user entry")
    public DetailedUserDTO createUser(@RequestBody DetailedUserDTO detailedUserDTO) {
        User user = userMapper.detailedUserDTOToUser(detailedUserDTO);
        user = userService.createUser(user);
        return userMapper.userToDetailedUserDTO(user);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific user entry")
    public SimpleUserDTO find(@PathVariable String userName) {
        return userMapper.userToSimpleUserDTO(userService.findByUsername(userName));
    }


    @RequestMapping(value = "/{username}/loginAttemptsLeft", method = RequestMethod.GET)
    @ApiOperation(value = "Get left login attempts of a specific user entry")
    public Integer getLoginAttemptsLeft(@PathVariable String username) {

        User user = userService.findByUsername(username);
        return user.getAttempts();
        }

}
