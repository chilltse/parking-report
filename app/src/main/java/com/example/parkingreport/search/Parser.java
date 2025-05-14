package com.example.parkingreport.search;
import android.util.Log;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.repository.ReportRepository;
import com.example.parkingreport.data.local.repository.UserRepository;

import java.util.*;

public class Parser {

    /**
     * @author @u7807744 Larry Wang
     * Converts a TokenPair into a QueryResult.
     * This is essentially a wrapper that assigns parsed tokens
     * into a structured result object.
     */
    public static List<Report> evaluateTokens(List<Token> tokens, boolean isWaitStatus, String role, int userID, ReportRepository reportRepository, UserRepository userRepository) {
        List<Report> userNameResult = new ArrayList<>();
        List<Report> carPlateResult = new ArrayList<>();
        Boolean userNameFlag = false; //Check if user has query userName
        Boolean carPlateFlag = false; //Check if user has query carPlate

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
                    //Between same type use 'OR' to search
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
                    //Between same type use 'OR' to search
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
                //Between same type use 'OR' to search
                if(!userNameResult.contains(find) && find != null){
                    userNameResult.add(find);
                }
            }
        }

        // if role is admin and tokens is empty, then return all record.
        if(role.equals(User.ADMIN) && tokens.size() == 0){
            userNameFlag = true;
            if(isWaitStatus){
                userNameResult = reportRepository.getAllWaitingReportsLive();
            }else{
                userNameResult = reportRepository.getAllReportsLive();
            }
        }

        // Between different type us "OR" for searching, between same type use "AND" for searching
        // "OR" searching, use retainALL() method to implement
        if(!userNameFlag || !carPlateFlag){
            // User only searched one type, no need to check
            if(userNameFlag){
                return userNameResult;
            }else {
                return carPlateResult;
            }
        }else {
            // User searched two types, need to check
            userNameResult.retainAll(carPlateResult);
            return userNameResult;
        }
    }
}

