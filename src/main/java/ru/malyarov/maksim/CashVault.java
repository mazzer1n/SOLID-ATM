package ru.malyarov.maksim;

import lombok.Getter;

import java.util.*;

@Getter
public class CashVault {

    //Номинал - Количество купюр
    private final SortedMap<Integer, Integer> vault = new TreeMap<>(Comparator.reverseOrder());

    //Общая сумма денег в банкомате
    private long sum = 0;

    public Operation withdrawal(long total) {
        Map<Integer, Integer> operation = new HashMap<>();
        //Временная переменная для отката, если банкнотами не получится отдать нужную сумму
        long temp = this.sum;

        if (this.sum < total) {
            throw new RuntimeException("Ошибка выдачи: Недостаточно средств в банкомате");
        }

        for (Map.Entry<Integer, Integer> entry : vault.entrySet()) {

            //Достаем кол-во валют номинала denomination
            Integer quantity = entry.getValue();

            //Достаем номинал
            Integer denomination = entry.getKey();

            //Делим сумму выдачи на номинал, если сумма выдачи больше номинала -
            //Мы можем взять целую часть от occurrences купюр этого номинала
            Integer occurrences = Math.toIntExact(total / denomination);

            //Это нужно для того чтобы последнее условие правильно высчитывало total
            //В зависимости от того, какой именно if сработал
            Integer occurrencesOrQuantity = 0;

            boolean flag = false;

            if (occurrences > 0 && occurrences <= quantity) {
                operation.put(denomination, occurrences);
                vault.computeIfPresent(denomination, (k, v) -> (v - occurrences));
                occurrencesOrQuantity = occurrences;
                flag = true;
            }

            if (occurrences > quantity && quantity > 0) {
                operation.put(denomination, quantity);
                vault.computeIfPresent(denomination, (k, v) -> (0));
                occurrencesOrQuantity = quantity;
                flag = true;
            }

            if (flag) {
                temp -= (long) occurrencesOrQuantity * denomination;
                total -= (long) occurrencesOrQuantity * denomination;
            }

        }

        if (total == 0) {
            this.sum = temp;
        } else {
            throw new RuntimeException("Ошибка выдачи: Недостаточно купюр определенного номинала для выдачи суммы");
        }

        return new Operation(operation);
    }

    public void replenish(Operation operation) {
        for (Map.Entry<Integer, Integer> entry : operation.getBanknotes().entrySet()) {
            Integer quantity = entry.getValue();
            Integer denomination = entry.getKey();
            vault.computeIfPresent(denomination, (k, v) -> v + quantity);
            vault.putIfAbsent(denomination, quantity);
            sum += (long) quantity * denomination;
        }

    }

    public Operation issueResidue() {
        return new Operation(vault);
    }

}
