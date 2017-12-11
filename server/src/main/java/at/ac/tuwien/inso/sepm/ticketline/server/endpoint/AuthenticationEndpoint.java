package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/authentication")
@Api(value = "authentication")
public class AuthenticationEndpoint {

    private final HeaderTokenAuthenticationService authenticationService;
    private final UserService userService;
    private final UserMapper userMapper;


    public AuthenticationEndpoint(SimpleHeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService, UserService userService, UserMapper userMapper) {
        authenticationService = simpleHeaderTokenAuthenticationService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Get an authentication token with your username and password")
    public AuthenticationToken authenticate(@RequestBody final AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get some valid authentication tokens")
    public AuthenticationToken authenticate(@ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.renewAuthentication(authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }

    @RequestMapping(value = "/info/{token}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific authentication token")
    public AuthenticationTokenInfo tokenInfoAny(@PathVariable String token) {
        return authenticationService.authenticationTokenInfo(token);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about the current users authentication token")
    public AuthenticationTokenInfo tokenInfoCurrent(@ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.PUT)
    @ApiOperation(value = "Delete current users authentication token")
    public void AuthenticationLogout(@ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

    }

}
