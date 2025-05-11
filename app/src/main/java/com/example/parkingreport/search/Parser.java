package com.example.parkingreport.search;
import android.util.Log;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.repository.ReportRepository;
import com.example.parkingreport.data.local.repository.UserRepository;

import java.util.*;

public class Parser {

    /**
     * Converts a TokenPair into a QueryResult.
     * This is essentially a wrapper that assigns parsed tokens
     * into a structured result object.
     */
    public static List<Report> evaluateTokens(List<Token> tokens, boolean isWaitStatus, String role, int userID, ReportRepository reportRepository, UserRepository userRepository) {
        List<Report> userNameResult = new ArrayList<>();
        List<Report> carPlateResult = new ArrayList<>();
        Boolean userNameFlag = false; // 用于判断用户是否查询了userName
        Boolean carPlateFlag = false; // 用于判断用户是否查询了carPlate

        for (Token token : tokens) {
            String type = token.getType();
            String value = token.getValue();
            // userName
            if (type.equals(Token.USERNAME)) {
                userNameFlag = true;
                // find userId
                int userId = userRepository.findIdByName(value);
                Log.d("find_ids", String.valueOf(userId));
                // use userId to find report ids
                List<Integer> ids = reportRepository.getIdsByUser(userId);
                // find detail and add to results
                for (Integer id : ids) {
                    Report find = reportRepository.findReport(id, isWaitStatus);
                    // same type之间是“或”查询
                    if(!userNameResult.contains(find) && find != null){
                        userNameResult.add(find);
                    }
                }
            }
            // carPlate
            if (type.equals(Token.CARPLATE)) {
                carPlateFlag = true;
                // find report ids
                List<Integer> rids = reportRepository.getIdsByPlate(value);
                // find detail and add to results
                for (Integer id : rids) {
                    Report find = reportRepository.findReport(id, isWaitStatus);
                    // same type之间是“或”查询
                    if(!carPlateResult.contains(find) && find != null){
                        carPlateResult.add(find);
                    }
                }
            }
        }

        // if role is user, then userNameFlag is set, and userNameResult is result of current user.
        if(role.equals(User.USER)){
            userNameFlag = true;
            // use userId to find report ids
            List<Integer> ids = reportRepository.getIdsByUser(userID);
            // find detail and add to results
            for (Integer id : ids) {
                Report find = reportRepository.findReport(id, isWaitStatus);
                // same type之间是“或”查询
                if(!userNameResult.contains(find) && find != null){
                    userNameResult.add(find);
                }
            }
        }

        // if role is admin and tokens is empty, then return all record.
        if(role.equals(User.ADMIN) && tokens.size() == 0){
            userNameFlag = true;
            userNameResult = reportRepository.getAllReportsLive();
        }

        // different type 之间是“与”查询，same type之间是“或”查询
        // “与”查询，使用List里的retainAll()方法实现
        if(!userNameFlag || !carPlateFlag){
            // 用户只查询了一种，不需要与操作
            if(userNameFlag){
                return userNameResult;
            }else {
                return carPlateResult;
            }
        }else {
            // 查询了两者，需要与操作
            userNameResult.retainAll(carPlateResult);
            return userNameResult;
        }
    }
}

