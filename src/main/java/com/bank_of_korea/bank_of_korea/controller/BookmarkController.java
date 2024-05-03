package com.bank_of_korea.bank_of_korea.controller;

import com.bank_of_korea.bank_of_korea.entity.BookmarkData;
import com.bank_of_korea.bank_of_korea.entity.WeatherData;
import com.bank_of_korea.bank_of_korea.repository.BookmarkRepository;
import com.bank_of_korea.bank_of_korea.service.BookmarkService;
import com.bank_of_korea.bank_of_korea.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

@Controller
public class BookmarkController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping("weather/main")
    public String main(@RequestParam(name="loginEmail", required = false)String userEmail,
                       @RequestParam(name="locationList[]", required = false) ArrayList<String> locationList, Model model){
        if(userEmail!=null && locationList!=null){
            System.out.println(userEmail + locationList);
            String location = String.join(",", locationList);
            System.out.println(location);

            BookmarkData bookmark = new BookmarkData();
            bookmark.setEmail(userEmail);
            bookmark.setLocations(location);

            System.out.println(bookmark);
            bookmarkService.update(bookmark);
        }

        return "main";
    }
}
