package com.easyfitness.DAO;

import android.content.Context;

import com.easyfitness.DAO.program.DAOProgram;
import com.easyfitness.DAO.record.DAORecord;

public class DatabaseAccess {

    static private Context makeContextSafe(Context context) {
        return context.getApplicationContext();
    }

    static private DAORecord daoRecord;
    static private DAOProgram daoProgram;

    static public DAORecord getRecordDAO(Context leakyContext) {

        if(daoRecord == null) {
            daoRecord = new DAORecord(makeContextSafe(leakyContext));
        }
        return daoRecord;
    }

    static public DAOProgram getProgramDAO(Context leakyContext) {
        if(daoProgram == null) {
            daoProgram = new DAOProgram(makeContextSafe(leakyContext));
        }
        return daoProgram;
    }
}
