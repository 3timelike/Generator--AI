package com.zsy.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseConfig {
    private String url;
    private String port;
    private String username;
    private String password;
    private String databaseName;
}
