package com.gq.business.accountmanage.mappers;

import com.gq.business.accountmanage.model.TransactionBean;

/**
 * 事务管理bean
 *
 * @author zhixinchen
 * @ClassName: TransactionMapper
 * @Description: 事务管理bean
 * @date: Nov 22, 2013 1:06:53 PM
 */
public interface TransactionMapper {
    /**
     * 创建事务
     *
     * @param transactionBean <br />
     * @return <br />
     */
    int insert(TransactionBean transactionBean);

    /**
     * 更新事务
     *
     * @param transactionBean <br />
     * @return <br />
     */
    int update(TransactionBean transactionBean);

    /**
     * 销毁事务
     *
     * @param transactionBean <br />
     * @return <br />
     */
    int destory(TransactionBean transactionBean);

    /**
     * 校验事务
     *
     * @param transactionBean <br />
     * @return <br />
     */
    int checkTransaction(TransactionBean transactionBean);
    
    /**
     * 获取事务id
     *
     * @param userName <br />
     * @return <br />
     */
    String getTransidByUserName(String userName);

    /**
     * 删除用户的sesionid
     *
     * @param transactionBean <br />
     * @return <br />
     */
    int deleteByUsr(TransactionBean transactionBean);

    /**
     * 清除事务id表中数据
     *
     * @return <br />
     */
    int truncateTransation();
}
