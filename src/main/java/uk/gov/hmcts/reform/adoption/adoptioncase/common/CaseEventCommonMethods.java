package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class CaseEventCommonMethods {
    private CaseEventCommonMethods() {

    }

    public static List<MessageDocumentList> prepareDocumentList(CaseData caseData) {
        List<MessageDocumentList> messageDocumentLists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseData.getAdditionalDocumentsCategory())) {
            caseData.getAdditionalDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getCorrespondenceDocumentCategory())) {
            caseData.getCorrespondenceDocumentCategory().forEach(item -> {
                if (item.getValue().getName() != null) {
                    UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                    messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
                }
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getReportsDocumentCategory())) {
            caseData.getReportsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getStatementsDocumentCategory())) {
            caseData.getStatementsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getCourtOrdersDocumentCategory())) {
            caseData.getCourtOrdersDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getApplicationDocumentsCategory())) {
            caseData.getApplicationDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }
        return messageDocumentLists;
    }

    public static List<DynamicListElement> prepareMessageReasonList(User caseworkerUser) {
        List<DynamicListElement> reasonList = new ArrayList<>();
        if(caseworkerUser.getUserDetails().getRoles().contains(UserRole.DISTRICT_JUDGE.getRole())) {
            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.LIST_A_HEARING.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.REQUEST_DOCUMENT.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.REQUEST_IFNORMATION.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.CREATE_ORDER.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.RETURN_ORDER.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.SERVE_DOCUMENT.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.SEND_A_LETTER.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReasonJudge.GENERAL_QUERY.getLabel()).code(
                    UUID.randomUUID()).build());

        } else {
            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.REFER_FOR_GATEKEEPING.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.LOCAL_AUTHORITY_APPLICATION.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.ANNEX_A.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.CORRESPONDANCE_FOR_REVIEW.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.DOCUMENT_FOR_REVIEW.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.ORDER_FOR_APPROVAL.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.LEAVE_TO_OPPOSE.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.GENERAL_QUERY.getLabel()).code(
                    UUID.randomUUID()).build());

            reasonList.add(DynamicListElement.builder()
                               .label(MessageSendDetails.MessageReason.REQUEST_HEARING_DATE.getLabel()).code(
                    UUID.randomUUID()).build());
        }
        return reasonList;
    }
}
