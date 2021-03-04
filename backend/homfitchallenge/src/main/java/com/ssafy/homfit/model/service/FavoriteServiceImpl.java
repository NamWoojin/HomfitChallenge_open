package com.ssafy.homfit.model.service;

import com.ssafy.homfit.model.Favorite;
import com.ssafy.homfit.model.dao.FavoriteDAO;
import com.ssafy.homfit.model.dao.FeedDAO;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    SqlSession sqlSession;
    
    @Autowired
    FavoriteDAO favoriteDao;
    

    @Override
    public boolean init(String uid) throws Exception {
        try {
            sqlSession.getMapper(FavoriteDAO.class).init(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateFavorite(Favorite favorite) throws Exception {
        try{
            sqlSession.getMapper(FavoriteDAO.class).updateFavorite(favorite);
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean isSetting(String uid) throws Exception {
        return sqlSession.getMapper(FavoriteDAO.class).isSetting(uid);
    }

	@Override
	public Favorite selectUserInfo(String uid) {
		return favoriteDao.selectUserInfo(uid);
	}

	@Override
	public List<Favorite> selectUserByAgeSex(int age, char sex) {
		return favoriteDao.selectUserByAgeSex(age, sex);
	}

    
}
