package ru.mosolov.gku.other;

import java.time.LocalDateTime;

public class CheckUtils {

    public static boolean checkAbilityChangeDoc (final LocalDateTime startTime, final LocalDateTime endTime){

        return !(LocalDateTime.now().isAfter(startTime)
                && LocalDateTime.now().isBefore(endTime));
    }
}
