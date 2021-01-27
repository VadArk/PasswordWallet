package com.varkaikin.passwordwallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.varkaikin.passwordwallet.config.PassAlgor;
import com.varkaikin.passwordwallet.model.DataChange;
import com.varkaikin.passwordwallet.model.FunctionRun;
import com.varkaikin.passwordwallet.model.Password;
import com.varkaikin.passwordwallet.model.User;
import com.varkaikin.passwordwallet.repository.DataChangeRepository;
import com.varkaikin.passwordwallet.repository.FunctionRunRepository;
import com.varkaikin.passwordwallet.repository.PasswordRepository;
import com.varkaikin.passwordwallet.repository.UserRepository;
import com.varkaikin.passwordwallet.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordServiceImplementation implements PasswordService {

    private final UserRepository userService;
    private final PasswordRepository passwordRepository;
    private final ModelMapper modelMapper;
    private final DataChangeRepository dataChangeRepository;
    private final FunctionRunRepository functionRunRepository;

    public PasswordServiceImplementation(ModelMapper modelMapper,
                                         UserRepository userService,
                                         PasswordRepository passwordRepository,
                                         DataChangeRepository dataChangeRepository,
                                         FunctionRunRepository functionRunRepository) {

        this.modelMapper = modelMapper;
        this.userService = userService;
        this.passwordRepository = passwordRepository;
        this.dataChangeRepository = dataChangeRepository;
        this.functionRunRepository = functionRunRepository;
    }

    @Override
    public PasswordResponse findAllByUserLogin(String login, Pageable pageable) {
        Integer totalElements = passwordRepository.findAllByUser_LoginAndIsDeletedIsFalse(login).size();
        Integer totalPages;
        if (totalElements % pageable.getPageSize() == 0)
            totalPages = totalElements / pageable.getPageSize();
        else totalPages = totalElements / pageable.getPageSize() + 1;


        List<Password> allByUser_login = passwordRepository.findAllByUser_LoginAndIsDeletedIsFalse(login, pageable);
        PasswordResponse passwordRespons = new PasswordResponse();
        List<PasswordsContent> content = new ArrayList<>();
        for (Password pass : allByUser_login) {
            PasswordsContent map = modelMapper.map(pass, PasswordsContent.class);
            content.add(map);
        }
        passwordRespons.setContent(content);
        passwordRespons.setTotalElements(totalElements.toString());
        passwordRespons.setTotalPages(totalPages.toString());
        passwordRespons.setNumber(String.valueOf(pageable.getPageNumber()));
        return passwordRespons;
    }

    @Override
    public String showPassword(ShowPasswordRequest showPasswordRequest) throws Exception {

//        Password password = passwordRepository.findById(Long.parseLong(showPasswordRequest.getPasswordId())).get();
//        Key key = PassAlgor.generateKey(showPasswordRequest.getMasterPassword());

        Password password = passwordRepository.findById(Long.parseLong(showPasswordRequest.getPasswordId())).get();
        User byLogin = userService.findByLogin(password.getUser().getLogin());
        //FunctionRun
        FunctionRun functionRun = new FunctionRun();
        functionRun.setFunctionName("view password");
        functionRun.setTime(LocalDateTime.now());
        byLogin.addFunctionRun(functionRun);
        functionRunRepository.save(functionRun);

        String password_hash = password.getUser().getPassword_hash();
        Key key = PassAlgor.generateKey(password_hash);
        return PassAlgor.decrypt(password.getPassword(), key);

    }

    @Override
    public Boolean changeAllUsersPasswords(String login, String oldMasterPass, String NewMasterPass) throws Exception {

        List<Password> allByUser_login = passwordRepository.findAllByUser_Login(login);

        for (Password p : allByUser_login) {
            String decrypt = PassAlgor.decrypt(p.getPassword(), PassAlgor.generateKey(oldMasterPass));
            p.setPassword(PassAlgor.encrypt(decrypt, PassAlgor.generateKey(NewMasterPass)));
            passwordRepository.save(p);
        }
        return true;


    }

    @Override
    public PasswordRequest add(AddPasswordRequest addPasswordRequest) throws Exception {
        User userByLogin = userService.findByLogin(addPasswordRequest.getUserLogin());

        Password password = new Password();
        password.setLogin(addPasswordRequest.getLogin());

        Key key = PassAlgor.generateKey(userByLogin.getPassword_hash());
        password.setPassword(PassAlgor.encrypt(addPasswordRequest.getPassword(), key));

        password.setWeb_address(addPasswordRequest.getWeb_address());
        password.setDescription(addPasswordRequest.getDescription());
        password.setOwnerId(null);
        userByLogin.addPassword(password);

        passwordRepository.save(password);


        //записать data change

        DataChange dataChange = new DataChange();
        dataChange.setPreviousValueOfRecord(null);
        dataChange.setPresentValueOfRecord(password.toString());
        dataChange.setTime(LocalDateTime.now());
        dataChange.setActionType("create password");
        userByLogin.addDataChange(dataChange);
        password.addDataChange(dataChange);
        dataChangeRepository.save(dataChange);

        //FunctionRun
        FunctionRun functionRun = new FunctionRun();
        functionRun.setFunctionName("create password");
        functionRun.setTime(LocalDateTime.now());
        userByLogin.addFunctionRun(functionRun);
        functionRunRepository.save(functionRun);


        return modelMapper.map(addPasswordRequest, PasswordRequest.class);
    }

    @Override
    public Boolean share(SharePassRequest sharePassRequest) throws Exception {

        User ownerByLogin = userService.findByLogin(sharePassRequest.getUserLogin());
        User sharedUserByLogin = userService.findByLogin(sharePassRequest.getSharedLogin());
        Password passwordById = passwordRepository.findById(Long.parseLong(sharePassRequest.getPasswordId())).get();
        //если в списке паролей userID и ownerId ==null

        Password firstByUser_loginAndIsOwnerTrue =
                passwordRepository.findByUser_LoginAndIdAndOwnerIdIsNull(sharePassRequest.getUserLogin(),
                        Long.valueOf(sharePassRequest.getPasswordId()));
        //chek if user is owner
        if (firstByUser_loginAndIsOwnerTrue != null) {
            //робимо новий запис до бази
            Password password = new Password();
            password.setOwnerId(passwordById.getId());
            password.setLogin(passwordById.getLogin());
            password.setDescription(passwordById.getDescription());
            password.setWeb_address(passwordById.getWeb_address());

            //розшифрувати пароль
            String password_hash = ownerByLogin.getPassword_hash();
            Key key = PassAlgor.generateKey(password_hash);
            String decryptedPass = PassAlgor.decrypt(passwordById.getPassword(), key);

            //зашифрувати пароль з новим Password_hash
            Key new_key = PassAlgor.generateKey(sharedUserByLogin.getPassword_hash());
            password.setPassword(PassAlgor.encrypt(decryptedPass, new_key));

            sharedUserByLogin.addPassword(password);
            passwordRepository.save(password);
            //FunctionRun
            FunctionRun functionRun = new FunctionRun();
            functionRun.setFunctionName("share password");
            functionRun.setTime(LocalDateTime.now());
            ownerByLogin.addFunctionRun(functionRun);
            functionRunRepository.save(functionRun);

            return true;
        } else return false;
    }

    @Override
    public Boolean edit(EditRequest editPassRequest) throws Exception {


        //chek if user is owner
        Password firstByUser_loginAndIsOwnerTrue =
                passwordRepository.findByUser_LoginAndIdAndOwnerIdIsNull(editPassRequest.getUserLogin(),
                        Long.valueOf(editPassRequest.getPasswordId()));
        //chek if user is owner
        if (firstByUser_loginAndIsOwnerTrue != null) {

            User ownerByLogin = userService.findByLogin(editPassRequest.getUserLogin());

            //оригінальний пароль
            Password passwordById = passwordRepository.findById(Long.parseLong(editPassRequest.getPasswordId())).get();
            //записать data change
            DataChange dataChange = new DataChange();
            dataChange.setPreviousValueOfRecord(passwordById.toString());
            //список паролів таких самих паролів, що були розшарені юзерам
            List<Password> allByOwnerId = passwordRepository.findAllByOwnerId(Long.parseLong(editPassRequest.getPasswordId()));


            if (editPassRequest.getNewWeb() != null) {
                passwordById.setWeb_address(editPassRequest.getNewWeb());
                allByOwnerId.forEach(password -> password.setWeb_address(editPassRequest.getNewWeb()));
            }
            if (editPassRequest.getNewDescription() != null) {
                passwordById.setDescription(editPassRequest.getNewDescription());
                allByOwnerId.forEach(password -> password.setDescription(editPassRequest.getNewDescription()));
            }

            if (editPassRequest.getNewPassword() != null) {

                Key new_key = PassAlgor.generateKey(ownerByLogin.getPassword_hash());
                passwordById.setPassword(PassAlgor.encrypt(editPassRequest.getNewPassword(), new_key));

                //для кожного юзера з нвим ключем кодуємо цей пароль
                for (Password pass : allByOwnerId) {
                    Key keyForEachUser = PassAlgor.generateKey(pass.getUser().getPassword_hash());
                    pass.setPassword(PassAlgor.encrypt(editPassRequest.getNewPassword(), keyForEachUser));
                }
            }
            if (editPassRequest.getNewLogin() != null) {
                passwordById.setLogin(editPassRequest.getNewLogin());
                allByOwnerId.forEach(password -> password.setLogin(editPassRequest.getNewLogin()));
            }

            allByOwnerId.forEach(password -> passwordRepository.save(password));
            passwordRepository.save(passwordById);

            //записать data change

            dataChange.setPresentValueOfRecord(passwordById.toString());
            dataChange.setTime(LocalDateTime.now());
            dataChange.setActionType("update password");
            ownerByLogin.addDataChange(dataChange);
            passwordById.addDataChange(dataChange);
            dataChangeRepository.save(dataChange);

            //FunctionRun
            FunctionRun functionRun = new FunctionRun();
            functionRun.setFunctionName("update password");
            functionRun.setTime(LocalDateTime.now());
            ownerByLogin.addFunctionRun(functionRun);
            functionRunRepository.save(functionRun);

            return true;
        } else return false;
    }

    @Override
    public Boolean delete(String passwordId, String userLogin) {

        User byLogin = userService.findByLogin(userLogin);
        Password firstByUser_loginAndIsOwnerTrue =
                passwordRepository.findByUser_LoginAndIdAndOwnerIdIsNull(userLogin,
                        Long.valueOf(passwordId));
        //chek if user is owner
        if (firstByUser_loginAndIsOwnerTrue != null) {
            Password passwordById = passwordRepository.findById(Long.parseLong(passwordId)).get();
            //записать data change 1
            DataChange dataChange = new DataChange();
            dataChange.setPreviousValueOfRecord(passwordById.toString());
            //
            //passwordRepository.delete(passwordById);
            passwordById.setIsDeleted(true);
            passwordRepository.save(passwordById);
            //найти список таких же паролей у других юзеров
            List<Password> passwordsList = passwordRepository.findAllByOwnerId(Long.parseLong(passwordId));
            //и удалить их
            passwordsList.forEach(
                    //password -> passwordRepository.delete(password)
                    password -> password.setIsDeleted(true)
            );

            //записать data change 2
            dataChange.setPresentValueOfRecord(passwordById.toString());
            dataChange.setTime(LocalDateTime.now());
            dataChange.setActionType("delete password");
            byLogin.addDataChange(dataChange);
            passwordById.addDataChange(dataChange);
            dataChangeRepository.save(dataChange);

            //FunctionRun
            FunctionRun functionRun = new FunctionRun();
            functionRun.setFunctionName("delete password");
            functionRun.setTime(LocalDateTime.now());
            byLogin.addFunctionRun(functionRun);
            functionRunRepository.save(functionRun);

            return true;
        } else return false;
    }

    @Override
    public OnePasswordResponse fidPasswordById(String id, String login) throws Exception {
        Password password = passwordRepository.findById(Long.valueOf(id)).get();
        User ownerByLogin = userService.findByLogin(login);
        //нада пароль розшифрований присилати

        //розшифрувати пароль
        String password_hash = ownerByLogin.getPassword_hash();
        Key key = PassAlgor.generateKey(password_hash);
        String decryptedPass = PassAlgor.decrypt(password.getPassword(), key);


        OnePasswordResponse map = modelMapper.map(password, OnePasswordResponse.class);
        map.setPassword(decryptedPass);
        return map;
    }

    @Override
    public Boolean restore(String changeId, String userLogin) throws JsonProcessingException {

        User byLogin = userService.findByLogin(userLogin);

        DataChange dataChange = dataChangeRepository.findById(Long.valueOf(changeId)).get();
        Password password ;
        if( passwordRepository.findById(Long.valueOf(dataChange.getPassword().getId())).get()!= null)
        {
            password=passwordRepository.findById(Long.valueOf(dataChange.getPassword().getId())).get();
        }
        else
        { password = new Password();}
        //
        //записать data change 1
        DataChange dataChangeToWrite = new DataChange();
        dataChangeToWrite.setPreviousValueOfRecord(password.toString());
        //


        ObjectMapper mapper = new ObjectMapper();
        Password restoreToPassword = mapper.readValue(dataChange.getPresentValueOfRecord(), Password.class);
        password.setDescription(restoreToPassword.getDescription());
        password.setIsDeleted(restoreToPassword.getIsDeleted());
        password.setLogin(restoreToPassword.getLogin());
        password.setOwnerId(restoreToPassword.getOwnerId());
        password.setPassword(restoreToPassword.getPassword());
        password.setWeb_address(restoreToPassword.getWeb_address());
        byLogin.addPassword(password);

        passwordRepository.save(password);

        //записать data change 2
        dataChangeToWrite.setPresentValueOfRecord(password.toString());
        dataChangeToWrite.setTime(LocalDateTime.now());
        dataChangeToWrite.setActionType("restore password");
        byLogin.addDataChange(dataChangeToWrite);
        password.addDataChange(dataChangeToWrite);
        dataChangeRepository.save(dataChangeToWrite);

        //FunctionRun
        FunctionRun functionRun = new FunctionRun();
        functionRun.setFunctionName("restore password");
        functionRun.setTime(LocalDateTime.now());
        byLogin.addFunctionRun(functionRun);
        functionRunRepository.save(functionRun);

        return true;
    }
}
