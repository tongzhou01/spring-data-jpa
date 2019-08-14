package com.tz.jpa.entity;

import java.net.URLDecoder;
import java.util.Date;

/**
 * FilmEntity
 *
 * @author:tongzhou
 * @date: 2019/07/09
 * @description: 电影实体类
 */
public class FilmEntity {

    private Long id;

    private String fileName;

    private String filmScore;

    private Date filmReleaseDate;

    private Date filmLowerDate;

    private String filmAlias;

    private String filmCover;

    private String filmCarousel;

    private String filmHot;

    private String filmLength;

    private String viewTimes;

    private String filmStatus;

    private Date createTime;

    private Date modifyTime;


    public static void main(String[] args) {
        System.out.println(URLDecoder.decode("%E6%9D%AD%E5%B7%9E%E8%B5%9B%E4%BC%AF%E4%B9%90"));
    }
}
