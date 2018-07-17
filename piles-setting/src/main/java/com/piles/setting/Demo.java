package com.piles.setting;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        String keySplit = " ";
        String requestBody = "68 01 00 00 00 1D 10 00 02 54 84 56 18 35 02 02 00 00 00 00 00 00 00 01 00 01 02 00 00 00 04 00 00 00 01 2B D9";
        if(StringUtils.isBlank(requestBody)){
        }

        String[] requestArray = StringUtils.split(requestBody, keySplit);
        List<String> requestList = Lists.newArrayList(requestArray);
    }


}
