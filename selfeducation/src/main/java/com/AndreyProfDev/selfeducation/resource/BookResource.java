package com.AndreyProfDev.selfeducation.resource;

import com.AndreyProfDev.selfeducation.data.Book;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/rest")
public class BookResource {

    @RequestMapping(value = "/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Book loadBook(HttpServletRequest request,
                                HttpServletResponse response){
        return new Book.Builder().setName("test").setId(1).build();
    }
}
