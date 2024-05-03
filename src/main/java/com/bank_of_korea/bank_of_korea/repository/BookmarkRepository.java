package com.bank_of_korea.bank_of_korea.repository;

import com.bank_of_korea.bank_of_korea.entity.BookmarkData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkData, Integer> {

    BookmarkData findByEmail(String email);
}
