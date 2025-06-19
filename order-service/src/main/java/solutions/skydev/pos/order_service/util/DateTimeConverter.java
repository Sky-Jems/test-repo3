package solutions.skydev.pos.order_service.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class DateTimeConverter {
    public static OffsetDateTime ToUtc(OffsetDateTime currentDate){
        return currentDate.atZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
    }
}
