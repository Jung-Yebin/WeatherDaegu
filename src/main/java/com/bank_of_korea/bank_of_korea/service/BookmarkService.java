package com.bank_of_korea.bank_of_korea.service;

import com.bank_of_korea.bank_of_korea.entity.BookmarkData;
import com.bank_of_korea.bank_of_korea.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    public String findLocation(String email){
        BookmarkData data = bookmarkRepository.findByEmail(email);
        System.out.println("findLocation" + data);
        return data.getLocations();
    }

    public void update(BookmarkData bookmark){
        System.out.println(bookmark.getEmail());

        BookmarkData data = bookmarkRepository.findByEmail(bookmark.getEmail());
        System.out.println("data" + data);
        if(data!= null){
            data.setLocations(bookmark.getLocations());
            bookmarkRepository.save(data);
            System.out.println("레코드 업데이트");

        }
        else {
            System.out.println("레코드 생성");
            bookmarkRepository.save(bookmark);
        }
    }
}
