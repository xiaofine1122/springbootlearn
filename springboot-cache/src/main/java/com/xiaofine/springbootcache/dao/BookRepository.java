package com.xiaofine.springbootcache.dao;

import com.xiaofine.springbootcache.entity.Book;

public interface BookRepository  {
    Book getByIsbn(String isbn);
}
