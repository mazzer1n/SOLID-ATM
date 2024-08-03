package ru.malyarov.maksim;

import java.util.Map;

public interface MachineInterface {

    //Выдача
    public Operation withdrawal(long total);

    //Пополнение
    public void replenish(Operation operation);

    //Выдача остатка
    public Operation issueResidue();

    public long getSum();

    public Map<Integer, Integer> getVault();

}
