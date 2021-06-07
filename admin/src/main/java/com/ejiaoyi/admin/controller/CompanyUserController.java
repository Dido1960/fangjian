package com.ejiaoyi.admin.controller;

import com.ejiaoyi.common.service.ICompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companyUser")
public class CompanyUserController {

    @Autowired
    private ICompanyUserService companyUserService;


    @RequestMapping("/listCompanyUser")
    public String listCompanyUser(){
        return companyUserService.listCompanyUser();
    }



}
