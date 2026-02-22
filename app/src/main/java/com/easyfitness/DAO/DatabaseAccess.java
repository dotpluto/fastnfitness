package com.easyfitness.DAO;

import android.content.Context;

import com.easyfitness.DAO.program.DAOProgram;
import com.easyfitness.DAO.record.DAORecord;
import com.easyfitness.MyApplication;

public class DatabaseAccess {
    static private DAORecord daoRecord;
    static private DAOProgram daoProgram;

    static public DAORecord getRecordDAO() {

        if(daoRecord == null) {
            daoRecord = new DAORecord(MyApplication.getAppContext());
        }
        return daoRecord;
    }

    static public DAOProgram getProgramDAO() {
        if(daoProgram == null) {
            daoProgram = new DAOProgram(MyApplication.getAppContext());
        }
        return daoProgram;
    }
}
