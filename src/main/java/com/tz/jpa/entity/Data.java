package com.tz.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data
 *
 * @author:tongzhou
 * @date: 2019/08/15
 * @description:
 */
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data<T> {

    private List<String> links;
    private List<T> results;
}
