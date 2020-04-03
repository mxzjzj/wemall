package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.CardItem;
import com.wemall.foundation.service.ICardItemService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CardItemServiceImpl implements ICardItemService {
    @Resource(name = "cardItemDao")
    private IGenericDAO<CardItem> cardItemDao;

    @Override
    public boolean save(CardItem cardItem) {
        try {
            this.cardItemDao.save(cardItem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public CardItem getObjById(Long id) {
        CardItem cardItem = (CardItem) this.cardItemDao.get(id);
        if (cardItem!= null) {
            return cardItem;
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.cardItemDao.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean batchDelete(List<Serializable> cardItemIds) {
        for (Serializable id : cardItemIds) {
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
        GenericPageList pList = new GenericPageList(CardItem.class, query, params, this.cardItemDao);
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
    public boolean update(CardItem cardItem) {
        try {
            this.cardItemDao.update(cardItem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<CardItem> query(String query, Map params, int begin, int max) {
        return this.cardItemDao.query(query, params, begin, max);
    }

    @Override
    public CardItem getObjByProperty(String propertyName, Object value) {
        return (CardItem) this.cardItemDao.getBy(propertyName, value);
    }

    @Override
    public long getCountByCard(long cardId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validFlag", true);
        params.put("cardId", cardId);

        String hql = "select count(obj) from CardItem obj where obj.validFlag=:validFlag and obj.card.id=:cardId";
        Long count = (Long)cardItemDao.queryObj(hql, params);

        if ((count != null) && (count > 0))
        {
            return count;
        }
        return 0;
    }

    @Override
    public String getNumberByPrefixMax(String prefix)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("prefix", prefix + "%");

        String hql = "select max(obj.itemNumber) from CardItem obj " +
            "where obj.itemNumber like :prefix";
        return (String)cardItemDao.queryObj(hql, params);
    }

    @Override
    public int removeObjsByCardNoOwner(List<Serializable> cardIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cardIds", cardIds);
        params.put("validFlag", false);
        params.put("updateDate", new Date());

        String hql = "update CardItem obj " +
            "set obj.validFlag=:validFlag, obj.updateDate=:updateDate " +
            "where obj.card.id in (:cardIds)";
        return cardItemDao.update(hql, params);
    }


}
