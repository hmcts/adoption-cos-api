package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanUtils;
import uk.gov.hmcts.ccd.sdk.type.ListValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.documentFrom;

public final class CaseDataUtil {

    private CaseDataUtil() {
    }

    @JsonIgnore
    public static <T> List<ListValue<T>> archiveManageOrdersHelper(List<ListValue<T>> list, T object) {
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

    public static ManageHearingTabDetails cloneToTabObject(ManageHearingDetails manageHearingDetails,
        ManageHearingTabDetails manageHearingTabDetails) {

        BeanUtils.copyProperties(manageHearingDetails, manageHearingTabDetails,
                                 "hearingA90Document", "hearingA91DocumentMother", "hearingA91DocumentFather");
        if (isNotEmpty(manageHearingDetails.getHearingA90Document())) {
            manageHearingTabDetails.setHearingA90Document(documentFrom(manageHearingDetails.getHearingA90Document()));
        }
        if (isNotEmpty(manageHearingDetails.getHearingA91DocumentMother())) {
            manageHearingTabDetails.setHearingA91DocumentMother(documentFrom(manageHearingDetails.getHearingA91DocumentMother()));
        }
        if (isNotEmpty(manageHearingDetails.getHearingA91DocumentFather())) {
            manageHearingTabDetails.setHearingA91DocumentFather(documentFrom(manageHearingDetails.getHearingA91DocumentFather()));
        }
        return manageHearingTabDetails;
    }
}
