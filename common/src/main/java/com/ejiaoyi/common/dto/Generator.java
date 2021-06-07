package com.ejiaoyi.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成器DTO
 *
 * @author Z0001
 * @since 2020-5-8
 */
@Data
public class Generator implements Serializable {

    private static final long serialVersionUID = 1L;

    private String author;

    private String moduleName;

    private String dbName;

    private String dbUser;

    private String dbPwd;

    private List<String> generator;

    private List<String> module;

    private List<String> entity;
}
