package com.varkaikin.passwordwallet.service;

import com.varkaikin.passwordwallet.config.HMACAlgorithm;
import com.varkaikin.passwordwallet.config.SHAAlgorithm;
import com.varkaikin.passwordwallet.dto.ChangeUserPasswordRequest;
import com.varkaikin.passwordwallet.dto.UserDTO;
import com.varkaikin.passwordwallet.dto.UserDTORegister;
import com.varkaikin.passwordwallet.dto.UserResponse;
import com.varkaikin.passwordwallet.exceptions.LoginExistsException;
import com.varkaikin.passwordwallet.model.ERole;
import com.varkaikin.passwordwallet.model.FunctionRun;
import com.varkaikin.passwordwallet.model.Role;
import com.varkaikin.passwordwallet.model.User;
import com.varkaikin.passwordwallet.repository.FunctionRunRepository;
import com.varkaikin.passwordwallet.repository.RoleRepository;
import com.varkaikin.passwordwallet.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserServiceImplementation implements UserService {


    private static final String PEPPER = "qweqwf&678798sd6f7sd67cysd";
    private static final String KEY = "Welcome1";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;
    private final LogService logService;
    private final FunctionRunRepository functionRunRepository;

    public UserServiceImplementation(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     PasswordService passwordService, LogService logService, FunctionRunRepository functionRunRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordService = passwordService;
        this.logService = logService;
        this.functionRunRepository = functionRunRepository;
    }


    @Override
    public Boolean login(UserDTO userDTO) {
        try {
            User user = userRepository.findByLogin(userDTO.getLogin());

            //FunctionRun
            FunctionRun functionRun = new FunctionRun();
            functionRun.setFunctionName("login");
            functionRun.setTime(LocalDateTime.now());
            user.addFunctionRun(functionRun);
            functionRunRepository.save(functionRun);

            return checkIfPasswordsMatch(user.getIsPasswordKeptAsHash(),
                    user.getSalt(),
                    userDTO.getPassword(),
                    user.getPassword_hash());

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    @Override
    public Boolean existsByLogin(String login) {
        return userRepository.findByLogin(login) != null;
    }


    private Boolean checkIfPasswordsSHA513Match(String salt, String password, String password_hash) {
        String inputPassHashSHA513 = SHAAlgorithm.calculateHashSHA512(PEPPER, salt, password);
        return checkIfPasswordsHashMatch(inputPassHashSHA513, password_hash);

    }

    private Boolean checkIfPasswordsHMASMatch(String password, String password_hash) {
        String inputPassHMAC = HMACAlgorithm.calculateHMAC(password, KEY);
        return checkIfPasswordsHashMatch(inputPassHMAC, password_hash);

    }

    private Boolean checkIfPasswordsHashMatch(String calculatedPaswordHash, String dbPasswordHash) {
        return calculatedPaswordHash.equals(dbPasswordHash);

    }

    private Boolean checkIfPasswordsMatch(Boolean isPasswordKeptAsHash, String salt, String requestPass, String dbPassHash) {
        if (isPasswordKeptAsHash) {
            return checkIfPasswordsSHA513Match(salt, requestPass, dbPassHash);
        } else {
            return checkIfPasswordsHMASMatch(requestPass, dbPassHash);
        }
    }

    @Transactional
    @Override
    public Boolean changePassword(ChangeUserPasswordRequest userRequest) throws Exception {

        User user = userRepository.findByLogin(userRequest.getLogin());
        String oldPasswordHash = user.getPassword_hash();
        if (checkIfPasswordsMatch(user.getIsPasswordKeptAsHash(),
                user.getSalt(),
                userRequest.getOldPassword(),
                user.getPassword_hash())
        ) {
            user.setIsPasswordKeptAsHash(userRequest.getIsPasswordSavedAsHash());
            String newPasswordHash = generateNewHashForUser(
                    user,
                    userRequest.getIsPasswordSavedAsHash(),
                    userRequest.getNewPassword());
            user.setPassword_hash(newPasswordHash);
            //recrypt all user's passwords here
            return passwordService.changeAllUsersPasswords(userRequest.getLogin(),
                   // userRequest.getOldPassword(), userRequest.getNewPassword());
                    oldPasswordHash, newPasswordHash);
        } else return false;

    }

    private String generateNewHashForUser(User user, Boolean isPasswordSavedAsHash, String password) {
        if (isPasswordSavedAsHash) {
            String salt = SHAAlgorithm.generateSalt();
            user.setSalt(salt);
            return SHAAlgorithm.calculateHashSHA512(PEPPER, salt, password);
        } else return HMACAlgorithm.calculateHMAC(password, KEY);

    }

    //не з бази
    @Override
    public String getPasswordHashValueByPassword(String password, Boolean isPasswordSavedAsHash, String salt) {
        if (isPasswordSavedAsHash) {
            return SHAAlgorithm.calculateHashSHA512(PEPPER, salt, password);
        } else return HMACAlgorithm.calculateHMAC(password, KEY);


    }

    @Transactional
    @Override
    public UserResponse add(UserDTORegister userDTORegister) throws LoginExistsException {

        User user = new User();
        user.setLogin(userDTORegister.getLogin());
        user.setIsPasswordKeptAsHash(userDTORegister.getIsPasswordSavedAsHash());
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.save(new Role(ERole.ROLE_USER));
        roles.add(userRole);
        user.setRoles(roles);


        if (userDTORegister.getIsPasswordSavedAsHash()) {
            String salt = SHAAlgorithm.generateSalt();
            user.setSalt(salt);
            user.setPassword_hash(SHAAlgorithm.calculateHashSHA512(PEPPER, salt, userDTORegister.getPassword()));
        } else {

            user.setPassword_hash(HMACAlgorithm.calculateHMAC(userDTORegister.getPassword(), KEY));
        }


        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getId().toString());
        userResponse.setUserLogin(user.getLogin());
        return userResponse;


    }


}
