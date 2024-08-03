package ru.malyarov.maksim;

import java.util.*;

public class AutoTellerMachine implements MachineInterface {

    CashVault cashVault = new CashVault();

    @Override
    public Operation withdrawal(long total) {
        return cashVault.withdrawal(total);
    }

    @Override
    public void replenish(Operation operation) {
        cashVault.replenish(operation);
    }

    @Override
    public Operation issueResidue() {
        return cashVault.issueResidue();
    }

    @Override
    public long getSum() {
        return cashVault.getSum();
    }

    @Override
    public Map<Integer, Integer> getVault() {
        return cashVault.getVault();
    }

}
