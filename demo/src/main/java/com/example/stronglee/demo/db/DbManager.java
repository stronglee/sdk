package com.example.stronglee.demo.db;

import android.content.Context;
import android.text.TextUtils;

import com.example.stronglee.demo.MyApplication;

import java.util.List;

import greendao.DaoSession;
import greendao.Users;
import greendao.UsersDao;
import greendao.infoType;
import greendao.infoTypeDao;
import greendao.infos;
import greendao.infosDao;


public class DbManager {
    private static DbManager mInstance;
    private DaoSession mDaoSession;
    private UsersDao userDao;
    private infoTypeDao infoTypeDao;
    private infosDao infosDao;


    private DbManager() {

    }

    public static synchronized DbManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbManager();
            mInstance.mDaoSession = MyApplication.getDaoSession(context);
            mInstance.userDao = mInstance.mDaoSession.getUsersDao();
            mInstance.infoTypeDao = mInstance.mDaoSession.getInfoTypeDao();
            mInstance.infosDao = mInstance.mDaoSession.getInfosDao();
        }
        return mInstance;
    }

    /**
     * 根据用户id,取出用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    public Users loadNote(long id) {
        if (!TextUtils.isEmpty(id + "")) {
            return userDao.load(id);
        }
        return null;
    }

    /**
     * 取出所有数据
     *
     * @return 所有数据信息
     */
    public List<Users> loadAllNote() {
        return userDao.loadAll();
    }

    /**
     * 生成按id倒排序的列表
     *
     * @return 倒排数据
     */
    public List<Users> loadAllNoteByOrder() {
        return userDao.queryBuilder().orderDesc(UsersDao.Properties.Id).list();
    }

    /**
     * 根据查询条件,返回数据列表
     *
     * @param where  条件
     * @param params 参数
     * @return 数据列表
     */
    public List<Users> queryNote(String where, String... params) {
        return userDao.queryRaw(where, params);
    }


    /**
     * 根据用户信息,插件或修改信息
     *
     * @param user 用户信息
     * @return 插件或修改的用户id
     */
    public long saveNote(Users user) {
        return userDao.insertOrReplace(user);
    }


    /**
     * 批量插入或修改用户信息
     *
     * @param list 用户信息列表
     */
    public void saveNoteLists(final List<Users> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        userDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    Users user = list.get(i);
                    userDao.insertOrReplace(user);
                }
            }
        });

    }

    /**
     * 删除所有数据
     */
    public void deleteAllNote() {
        userDao.deleteAll();
    }

    /**
     * 根据id,删除数据
     *
     * @param id 用户id
     */
    public void deleteNote(long id) {
        userDao.deleteByKey(id);
    }

    /**
     * 根据用户类,删除信息
     *
     * @param user 用户信息类
     */
    public void deleteNote(Users user) {
        userDao.delete(user);
    }

    public Long SaveInfoType(infoType type) {
        return infoTypeDao.insertOrReplace(type);
    }

    public void deleteInfoType(long id) {
        infoTypeDao.load(id).delete();
    }

    public List<infoType> getAllInfoTypeList() {
        return infoTypeDao.queryBuilder().orderDesc(greendao.infoTypeDao.Properties.Id).list();
    }

    public infoType agetInfoType(long id) {
        return infoTypeDao.load(id);
    }

    public List<infos> getInfosByTypeId(long typeId) {
        return infoTypeDao.load(typeId).getInfoes();
    }

    public long saveInfo(infos info) {
        return infosDao.insertOrReplace(info);
    }

    public List<infos> getAllInfos() {
        return infosDao.loadAll();
    }

    public List<infos> getInfosByPageSize(long typeId, int pageNum, int pageSize) {
        return infosDao.queryBuilder().where(greendao.infosDao.Properties.TypeId.eq(typeId)).offset(pageNum - 1)
                .limit(pageSize).list();

    }
}
