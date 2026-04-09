package com.oxysystem.general.service.grab.partner.common;

import com.oxysystem.general.dto.grab.view.SellingTimeDTO;
import com.oxysystem.general.dto.grab.view.ServiceHoursDTO;
import com.oxysystem.general.enums.grab.OpenPeriodType;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class GrabSellingTimeCommon {
    public static SellingTimeDTO getSellingTimeDTO() {
        SellingTimeDTO sellingTime = new SellingTimeDTO();
        sellingTime.setStartTime(LocalDateTime.of(1000,1,1,0,0));
        sellingTime.setEndTime(LocalDateTime.of(9999,1,1,1,1));
        sellingTime.setId("SELLING-TIME-01");
        sellingTime.setName("All Day Selling Hours");

        Map<String, ServiceHoursDTO> map = new LinkedHashMap<>();
        ServiceHoursDTO serviceHours = new ServiceHoursDTO();
        serviceHours.setOpenPeriodType(OpenPeriodType.OpenAllDay.name());

//        List<PeriodDTO> periods = new ArrayList<>();
//        PeriodDTO period = new PeriodDTO();
//        period.setStartTime("00:00");
//        period.setEndTime("22:00");
//        periods.add(period);
//        serviceHours.setPeriods(periods);

        map.put("mon", serviceHours);
        map.put("tue", serviceHours);
        map.put("wed", serviceHours);
        map.put("thu", serviceHours);
        map.put("fri", serviceHours);
        map.put("sat", serviceHours);
        map.put("sun", serviceHours);
        sellingTime.setServiceHours(map);
        return sellingTime;
    }
}
