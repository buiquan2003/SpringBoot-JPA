package jpa.spring.config.utils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
/**
 * class FurnitureDateTimeUtils dùng setUp phần dateTime
 */
public class FurnitureDateTimeUtils {
    public static final FurnitureDateTimeUtils shared = new FurnitureDateTimeUtils();
    private static final ZoneId UTC_ZONED_ID = ZoneId.of("+00:00");
    private FurnitureDateTimeUtils() {
    }
    public Timestamp timestampFrom(ZonedDateTime zonedDateTime) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(zonedDateTime.toInstant(), UTC_ZONED_ID);
        return Timestamp.valueOf(zdt.toLocalDateTime());
    }
    public ZonedDateTime zonedDateTimeFrom(Timestamp timestamp) {
        return ZonedDateTime.of(timestamp.toLocalDateTime(), UTC_ZONED_ID);
    }
}