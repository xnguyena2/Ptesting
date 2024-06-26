package com.example.heroku.services;

import com.example.heroku.model.Users;
import com.example.heroku.model.joinwith.UserJoinUserInfo;
import com.example.heroku.model.repository.JoinUsersWithUsersInfoRepository;
import com.example.heroku.model.repository.ResultWithCountRepository;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.beer.SearchResult;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

@Component
public class UserAccount {

    @Autowired
    ResultWithCountRepository resultWithCountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersInfo usersInfo;

    @Autowired
    JoinUsersWithUsersInfoRepository joinUsersWithUsersInfoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Mono<Users> createAccount(String createBy, UpdatePassword newAccount) {
        return
                this.userRepository.getRegisteredAccountInTime(newAccount.getGroup_id(), newAccount.getUsername(), newAccount.getRegister_code())
                        .switchIfEmpty(
                                this.userRepository.save(Users.builder()
                                        .group_id(newAccount.getGroup_id())
                                        .username(newAccount.getUsername())
                                        .password(this.passwordEncoder.encode(newAccount.getNewpassword()))
                                        .roles(Collections.singletonList(Util.ROLE.get(newAccount.getRoles().get(0)).getName()))
                                        .createat(Util.getInstance().Now())
                                        .status(Users.Status.ACTIVE)
                                        .active(true)
                                        .createby(createBy)
                                        .phone_number(newAccount.getPhone_number())
                                        .register_code(newAccount.getRegister_code())
                                        .build()
                                        .AutoFill()
                                )
                        )
                        .flatMap(users ->
                                usersInfo.createUserInfo(com.example.heroku.model.UsersInfo.builder()
                                                .username(users.getUsername())
                                                .phone(users.getPhone_number_clean())
                                                .group_id(users.getGroup_id())
                                                .title(newAccount.getTitle())
                                                .roles(newAccount.getRoles_in_group())
                                                .user_fullname(newAccount.getFull_name())
                                                .build())
                                        .then(Mono.just(users))
                        )
                        .map(Users::Clean);
    }

    public Mono<Users> createAccountByAdmin(String createBy, UpdatePassword newAccount) {
        return
                this.userRepository.deleteByUserName(newAccount.getUsername())
                        .then(
                                this.userRepository.save(Users.builder()
                                        .group_id(newAccount.getGroup_id())
                                        .username(newAccount.getUsername())
                                        .password(this.passwordEncoder.encode(newAccount.getNewpassword()))
                                        .roles(Collections.singletonList(Util.ROLE.get(newAccount.getRoles().get(0)).getName()))
                                        .createat(Util.getInstance().Now())
                                        .status(Users.Status.ACTIVE)
                                        .active(true)
                                        .createby(createBy)
                                        .phone_number(newAccount.getPhone_number())
                                        .register_code(newAccount.getRegister_code())
                                        .build()
                                        .AutoFill()
                                )
                        )
                        .flatMap(users ->
                                usersInfo.createUserInfo(com.example.heroku.model.UsersInfo.builder()
                                                .username(users.getUsername())
                                                .phone(users.getPhone_number_clean())
                                                .group_id(users.getGroup_id())
                                                .title(newAccount.getTitle())
                                                .roles(newAccount.getRoles_in_group())
                                                .user_fullname(newAccount.getFull_name())
                                                .build())
                                        .then(Mono.just(users))
                        )
                        .map(Users::Clean);
    }

    public Mono<SearchResult<Users>> getAll(SearchQuery query) {
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.userCountAll(query.getGroup_id())
                .map(resultWithCount -> {
                    SearchResult<Users> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        this.userRepository.findByIdNotNull(query.getGroup_id(), PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createat")))
                                .map(users -> {
                                    users.setPassword(null);
                                    return users;
                                })
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<Users> updatePassword(String username, String password) {
        return userRepository.updatePassword(username, this.passwordEncoder.encode(password));
    }

    public Mono<Users> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<UserJoinUserInfo> getUserClean(String username) {
        return joinUsersWithUsersInfoRepository.findByUserName(username).map(UserJoinUserInfo::Clean);
    }

    public Flux<UserJoinUserInfo> getAllUserClean(SearchQuery searchQuery) {
        return joinUsersWithUsersInfoRepository.getAllUserFullInfo(searchQuery.getGroup_id(), searchQuery.getPage(), searchQuery.getSize()).map(UserJoinUserInfo::Clean);
    }

    public Mono<ResponseEntity> deleteAccount(Mono<Users> principal, UpdatePassword update) {

        return principal
                .flatMap(authentication ->
                        {
                            if (authentication.getGroup_id().endsWith("_" + authentication.getUsername()) && !authentication.getUsername().equals(update.getUsername())) {
                                return Mono.just(true);
                            }
                            return userRepository.isPermissionAllow(authentication.getUsername(), update.getUsername());
                        }
                )
                .filter(x -> x)
                .switchIfEmpty(Mono.error(new AccessDeniedException("403 returned2")))
                .flatMap(verify ->
                        seftDelete(update.getUsername())
                )
                .map(ResponseEntity::ok);
    }

    public Mono<Users> seftDelete(String username) {
        return usersInfo.deleteByUsername(username)
                .then(userRepository.deleteByUserName(
                        username
                ));
    }
}
