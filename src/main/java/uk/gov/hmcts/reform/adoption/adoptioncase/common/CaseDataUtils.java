package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import uk.gov.hmcts.ccd.sdk.type.ListValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.util.CollectionUtils.isEmpty;

public final class CaseDataUtils {

    private CaseDataUtils() {

    }

    public static <T> List<ListValue<T>> archiveListHelper(List<ListValue<T>> list, T object) {
        if (isEmpty(list)) {
            List<ListValue<T>> listValues = new ArrayList<>();
            var listValue = ListValue
                .<T>builder()
                .id("1")
                .value(object)
                .build();

            listValues.add(listValue);
            return listValues;
        } else {
            AtomicInteger listValueIndex = new AtomicInteger(0);
            var listValue = ListValue
                .<T>builder()
                .value(object)
                .build();
            // always add new Adoption Document as first element so that it is displayed on top
            list.add(
                0,
                listValue
            );
            list.forEach(listValueObj -> listValueObj
                .setId(String.valueOf(listValueIndex.incrementAndGet())));
        }
        return list;
    }

}
