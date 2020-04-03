package com.wemall.core.helper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.number.NumberUtil;
import org.apache.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wemall.core.base.Constant;
import com.wemall.foundation.domain.Card;
import com.wemall.foundation.domain.CardItem;
import com.wemall.foundation.service.ICardItemService;
import com.wemall.foundation.service.ICardService;

@Component
public class CardHelper {
    @Autowired
    private ICardItemService cardItemService;
    @Autowired
    private ICardService cardService;

    public long getItemCount(long cardId)
    {
        return cardItemService.getCountByCard(cardId);
    }

    public CardItem addItem(long cardId, boolean passwordFlag) throws UnsupportedEncodingException
    {
        return addItem(null, cardId, passwordFlag, false, 0, 0);
    }

    public CardItem addItem(HttpServletRequest request, long cardId, boolean passwordFlag, boolean qrcodeFlag, long cartItemId, long ownerId) throws UnsupportedEncodingException
    {
        CardItem cardItem = new CardItem();
        Card card = cardService.getObjById(cardId);
        if (card.getId() == Constant.CARD_USER)
        {
            String itemNumber = card.getId() + StringUtil.space(ownerId + "", '0', 9);
            cardItem.setItemNumber(itemNumber);
            cardItem.setItemBalance(BigDecimal.valueOf(0));
        }
        else
        {
            long subNumber = 1;
            String prefix = card.getSystemModule().getId() + "" + card.getCardPublishWay().getId() + StringUtil.space(card.getId() + "", '0', 6);
            String itemNumber = cardItemService.getNumberByPrefixMax(prefix);
            if (!StringUtil.toString(itemNumber).equals(""))
            {
                subNumber = NumberUtil.toLong(itemNumber.substring(8)) + 1;
            }
            itemNumber = prefix + StringUtil.space(subNumber + "", '0', 6);
            cardItem.setItemNumber(itemNumber);
            cardItem.setItemBalance(card.getCardAmount());
        }

        if (passwordFlag)
        {
            String itemPassword = StringUtil.random(6, StringUtil.NUMBER_CHARS);
            cardItem.setItemPassword(itemPassword);
        }

       /* if (cartItemId > 0)
        {
             cartItem = cartItemService.getObj(cartItemId);
            cardItem.setCartItem(cartItem);
        }*/

        /*if (ownerId > 0)
        {
            User user = userService.getObj(ownerId);
            cardItem.setOwner(user);
        }*/

        cardItem.setFrozenFlag(false);
        cardItem.setCard(card);
        cardItemService.save(cardItem);

      /*  if (qrcodeFlag)
        {
            String qrContent = Location.getUrl(request) + "/" + card.getSystemModule().getSystemCode() + "/client/buyer/user_card_recharge_add.htm?cardItemIdCode=" + Coder.base64Encode2(cardItem.getId() + "");
            String qrPath = FileTool.getFilePath(request, card.getSystemModule().getId(), Constant.VISIT_TYPE_ADMIN, FileTool.FOLDER_CARD, FileTool.FOLDER_ITEM, cardId, cardItem.getItemNumber() + ".png");
            QrcodeEncoder encoder = new QrcodeEncoder();
            try
            {
                encoder.encode(qrContent, qrPath);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            String itemQrcode = FileTool.getFileUrl(card.getSystemModule().getId(), Constant.VISIT_TYPE_ADMIN, FileTool.FOLDER_CARD, FileTool.FOLDER_ITEM, cardId, cardItem.getItemNumber()  + ".png");
            cardItem.setItemQrcode(itemQrcode);
            cardItemService.updateObj(cardItem);
        }*/
        return cardItem;
    }
}
