package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException, BadRequestException, AlreadyTakenException {
        if (req.username() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (req.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (req.email() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData existing = userDAO.getUser(req.username());
        if (existing != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        UserData u = new UserData(req.username(), req.password(), req.email());
        userDAO.createUser(u);

        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, req.username());
        authDAO.createAuth(authData);

        RegisterResult result = new RegisterResult(req.username(), token);
        return result;
    }

    public LoginResult login(LoginRequest req) throws DataAccessException, UnauthorizedException, BadRequestException {
        if (req.username() == null || req.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData user = userDAO.getUser(req.username());
        if (user == null || !user.password().equals(req.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, req.username()));

        return new LoginResult(req.username(), authToken);
    }

    public void logout(LogoutRequest req) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDAO.getAuth(req.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(req.authToken());
    }
}