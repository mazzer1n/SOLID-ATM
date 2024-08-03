package ru.malyarov.maksim;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Operation {

    //Номинал - Количество купюр
    private Map<Integer, Integer> banknotes;

}
