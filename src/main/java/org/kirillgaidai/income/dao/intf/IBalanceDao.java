package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.BalanceEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Balance DAO
 *
 * @author Kirill Gaidai
 */
public interface IBalanceDao extends IGenericDao<BalanceEntity> {

    /**
     * Returns balances for specified accounts at or before specified day
     *
     * @param accountIds accounts ids
     * @param lastDay    day to return balance at or before
     * @return list of balances
     */
    List<BalanceEntity> getList(Set<Integer> accountIds, LocalDate lastDay);

    /**
     * Returns balances for specified accounts within specified period
     *
     * @param accountIds accounts ids
     * @param firstDay   first day of period to return balances
     * @param lastDay    last day of period to return balances
     * @return list of balances
     */
    List<BalanceEntity> getList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay);

    /**
     * Returns balance for specified account and date
     *
     * @param accountId account id
     * @param day       day to return balance
     * @return balance or null if not found
     */
    BalanceEntity get(Integer accountId, LocalDate day);

    /**
     * Returns balance for specified account before specified date
     *
     * @param accountId account id
     * @param day       day to return balance before
     * @return balance or null if not found
     */
    BalanceEntity getBefore(Integer accountId, LocalDate day);

    /**
     * Returns balance for specified account after specified date
     *
     * @param accountId account id
     * @param day       day to return balance after
     * @return balance or null if not found
     */
    BalanceEntity getAfter(Integer accountId, LocalDate day);

    /**
     * Deletes balance for specified account and day
     *
     * @param accountId account id
     * @param day       day
     * @return number of deleted rows
     */
    int delete(Integer accountId, LocalDate day);

}
