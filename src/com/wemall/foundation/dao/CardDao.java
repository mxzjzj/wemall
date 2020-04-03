package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Card;
import org.springframework.stereotype.Repository;

@Repository("cardDao")
public class CardDao extends GenericDAO<Card> {
}
