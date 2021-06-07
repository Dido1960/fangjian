package com.ejiaoyi.admin.controller;
import com.ejiaoyi.common.service.IWordbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wordbook")
public class WordBookController {

    @Autowired
    private IWordbookService wordbookService;
}
