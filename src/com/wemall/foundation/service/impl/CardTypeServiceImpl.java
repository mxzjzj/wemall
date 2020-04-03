package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.foundation.CardType;
import com.wemall.foundation.dao.CardTypeDao;
import com.wemall.foundation.service.ICardTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CardTypeServiceImpl implements ICardTypeService {

    @Resource(name = "cardTypeDao")
    private IGenericDAO<CardType> cardTypeDao;

    @Override
    public CardType getObjById(Long id) {
        CardType cardType = (CardType) this.cardTypeDao.get(id);
        if (cardType != null) {
            return cardType;
        }
        return null;
    }

    @Override
    public List<CardType> getObjs() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validFlag", true);
        String hql = "obj.validFlag=:validFlag";
        return cardTypeDao.find(hql, params);
    }

    @Override
    public List<CardType> query(String query, Map params, int begin, int max) {
        return this.cardTypeDao.query(query, params, begin, max);
    }
}
