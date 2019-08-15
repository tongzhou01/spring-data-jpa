package com.tz.jpa.controller;

import com.tz.jpa.util.ReptileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ReptileController
 *
 * @author:tongzhou
 * @date: 2019/08/14
 * @description:
 */
@RestController
@RequestMapping("/reptile")
public class ReptileController {

    @PostMapping
    public void downloadImage(@RequestParam String code) {
//        String code = "1048026";
        String url = "https://movie.douban.com/celebrity/" + code + "/";
        String html = ReptileUtil.download(url);
        ReptileUtil.parse(html);
    }
}
