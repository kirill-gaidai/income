package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.OperationDao;
import org.kirillgaidai.income.service.OperationService;
import org.springframework.stereotype.Repository;

@Repository
public class OperationServiceImpl implements OperationService {

    private OperationDao operationDao;

    public OperationServiceImpl(final OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    @Override
    public void saveDto() {

    }

}
