package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Card;
import com.wemall.foundation.service.ICardService;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CardServiceImpl implements ICardService {

    @Resource(name="cardDao")
    private IGenericDAO<Card> cardDao;

    @Override
    public boolean save(Card card) {
        try {
            this.cardDao.save(card);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Card getObjById(Long id) {
        Card card = (Card) this.cardDao.get(id);
        if (card != null) {
            return card;
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.cardDao.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean batchDelete(List<Serializable> cardIds) {
        for (Serializable id : cardIds) {
            delete((Long) id);
        }

        return true;
    }

    @Override
    public IPageList list(IQueryObject properties) {
        if (properties == null) {
            return null;
        }
        String query = properties.getQuery();
        Map params = properties.getParameters();
        GenericPageList pList = new GenericPageList(Card.class, query, params, this.cardDao);
        if (properties != null) {
            PageObject pageObj = properties.getPageObj();
            if (pageObj != null)
                pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj.getCurrentPage().intValue(), pageObj.getPageSize() == null ? 0 : pageObj.getPageSize().intValue());
        } else {
            pList.doList(0, -1);
        }

        return pList;
    }

    @Override
    public boolean update(Card card) {
        try {
            this.cardDao.update(card);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Card> query(String query, Map params, int begin, int max) {
        return this.cardDao.query(query, params, begin, max);
    }

    @Override
    public Card getObjByProperty(String propertyName, Object value) {
        return (Card) this.cardDao.getBy(propertyName, value);
    }


    @Override
    public int removeObjs(List<Serializable> ids)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        params.put("validFlag", false);
        params.put("updateDate", new Date());

        String hql = "update Card obj " +
            "set obj.validFlag=:validFlag, obj.updateDate=:updateDate " +
            "where obj.id in (:ids)";
        return cardDao.update(hql, params);
    }
}
