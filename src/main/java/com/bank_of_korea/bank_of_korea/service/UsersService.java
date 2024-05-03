package com.bank_of_korea.bank_of_korea.service;

import com.bank_of_korea.bank_of_korea.entity.Users;
import com.bank_of_korea.bank_of_korea.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    //회원가입
    public void write(Users users){
        usersRepository.save(users);
    }

    //기존에 아이디가 존재하는지 확인하는 메서드
    public boolean isIdExists(String email){
        return usersRepository.existsByEmail(email);
    }

    public String checkPwd(String email){
        Users usersTemp = usersRepository.findByEmail(email);
        return usersTemp.getPassword();
    }

}
