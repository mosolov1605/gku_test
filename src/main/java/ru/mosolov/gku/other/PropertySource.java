package ru.mosolov.gku.other;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Getter
@Setter
public class PropertySource {

    @Value("${start.lock.change.doc: 20:00}")
    private String startLockChangeDoc;

    @Value("${end.lock.change.doc: 07:00}")
    private String endLockChangeDoc;

    @Value("${total.doc.workflow: 10}")
    private String totalDocWorkflow;

    @Value("${between.doc.workflow: 2}")
    private String betweenDocWorkflow;

    @Value("${minute.restriction.create.doc: 60}")
    private String minuteRestrictionCreateDoc;

    @Value("${count.restriction.create.doc: 5}")
    private String countRestrictionCreateDoc;

    public LocalDateTime getStartLockTime (){
        return getLockTime(startLockChangeDoc, 0);
    }

    public LocalDateTime getEndLockTime (){
        return getLockTime(startLockChangeDoc, 1);
    }

    private LocalDateTime getLockTime(final String strTime, final int countDay) {
        List<String> listStr = Arrays.asList(StringUtils.trimAllWhitespace(strTime).split(":"));
        final LocalDateTime time;
        time = LocalDateTime.now().plusDays(countDay);

        if (listStr.size() == 0) {
            return time;
        }

        if (listStr.size() == 1) {
            return time
                    .withHour(Integer.parseInt(listStr.get(0)));
        }

        if (listStr.size() == 2) {
            return time
                    .withHour(Integer.parseInt(listStr.get(0)))
                    .withMinute(Integer.parseInt(listStr.get(1)));
        }

        return time
                .withHour(Integer.parseInt(listStr.get(0)))
                .withMinute(Integer.parseInt(listStr.get(1)))
                .withSecond(Integer.parseInt(listStr.get(3)));
    }
}
