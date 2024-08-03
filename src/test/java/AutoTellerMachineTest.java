import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.malyarov.maksim.AutoTellerMachine;
import ru.malyarov.maksim.MachineInterface;
import ru.malyarov.maksim.Operation;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutoTellerMachineTest {

    private MachineInterface atm;
    private Operation operation;

    @BeforeEach
    public void setUp() {
        atm = new AutoTellerMachine();

        Map<Integer, Integer> banknotes = new HashMap<>(); //sum = 187
        banknotes.put(100, 1);
        banknotes.put(50, 1);
        banknotes.put(20, 1);
        banknotes.put(10, 1);
        banknotes.put(5, 1);
        banknotes.put(2, 1);
        banknotes.put(1, 1);

        operation = new Operation(banknotes);
    }

    @Test
    public void replenishWhenVaultIsEmpty() {
        atm.replenish(operation);
        assertEquals(atm.getVault(), operation.getBanknotes());
    }

    @Test
    public void replenishWhenVaultIsNonEmpty() {
        atm.replenish(operation);
        atm.replenish(operation);
        operation.getBanknotes().replaceAll((k, v) -> v + 1);
        assertEquals(atm.getVault(), operation.getBanknotes());
    }

    @Test
    public void withdrawalFirstDenomination() {
        atm.replenish(operation);
        Operation op = atm.withdrawal(10);
        assertEquals(atm.getVault().get(10), 0);
        assertEquals(op.getBanknotes().get(10), 1);
    }

    @Test
    public void withdrawalLastDenomination() {
        atm.replenish(operation);
        Operation op = atm.withdrawal(1);
        assertEquals(atm.getVault().get(1), 0);
        assertEquals(op.getBanknotes().get(1), 1);
    }

    @Test
    public void regularWithdrawal() {
        Map<Integer, Integer> muchBanknotes = new HashMap<>(); //sum = 18700
        muchBanknotes.put(100, 100); //10000 - 10000 (100)
        muchBanknotes.put(50, 100); //5000 - 3400 (68)
        muchBanknotes.put(20, 100); //2000 - 20 (1)
        muchBanknotes.put(10, 100); //1000 - 0 (0)
        muchBanknotes.put(5, 100); //500 - 0 (0)
        muchBanknotes.put(2, 100); //200 - 2 (1)
        muchBanknotes.put(1, 100); //100 - 1 (1)

        atm.replenish(new Operation(muchBanknotes));
        atm.withdrawal(13423);

        assertEquals(atm.getVault().get(100), 0);
        assertEquals(atm.getVault().get(50), 32);
        assertEquals(atm.getVault().get(20), 99);
        assertEquals(atm.getVault().get(10), 100);
        assertEquals(atm.getVault().get(5), 100);
        assertEquals(atm.getVault().get(2), 99);
        assertEquals(atm.getVault().get(1), 99);

    }

    @Test
    public void withdrawalOfALargerAmount() {
        atm.replenish(operation);
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> atm.withdrawal(200));
        assertEquals("Ошибка выдачи: Недостаточно средств в банкомате", exception.getMessage());
    }

    @Test
    public void withdrawingAnAmountThatTheAtmCannotDispense() {
        atm.replenish(operation);
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> atm.withdrawal(184));
        assertEquals("Ошибка выдачи: Недостаточно купюр определенного номинала для выдачи суммы",
                exception.getMessage());
    }
}
