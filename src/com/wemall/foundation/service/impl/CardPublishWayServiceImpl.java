package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.foundation.dao.CardPublishWayDao;
import com.wemall.foundation.domain.CardPublishWay;
import com.wemall.foundation.service.ICardPublishWayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class CardPublishWayServiceImpl implements ICardPublishWayService {

    @Resource(name = "cardPublishWayDao")
    private IGenericDAO<CardPublishWay> cardPublishWayDao;

    @Override
    public CardPublishWay getObjById(Long id) {
        CardPublishWay card = (CardPublishWay) this.cardPublishWayDao.get(id);
        if (card != null) {
            return card;
        }
        return null;
    }
}
