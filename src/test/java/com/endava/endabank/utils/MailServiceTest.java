package com.endava.endabank.utils;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MailServiceTest {
    private final String templateId = System.getenv("SEND_GRID_TEMPLATE_ID");
    private final String fromEmail = System.getenv("SENDGRID_MAIL_FROM");
    private final String emailTo = "alejandrosuaza.1022@gmail.com";
    private final String asName = "test";
    private final String name = "testUser";
    private final String link = "http://test.com";

    @Test
    @Disabled
    void sendEmailShouldWorkTest() throws IOException {
        Response response1 = MailService.sendEmail(emailTo, name, link, templateId, asName);
        try (MockedStatic<MailService> utilities = Mockito.mockStatic(MailService.class)) {
            Response response = TestUtils.getSendGridResponse();
//            utilities.when(() ->
//                            MailService.invokeServiceEmail(any(), any()))
//                    .thenReturn(response);
            assertEquals(response, response1);
            ArgumentCaptor<Mail> argumentCaptor = ArgumentCaptor.forClass(Mail.class);
            utilities.verify(() ->
                            MailService.invokeServiceEmail(argumentCaptor.capture(), any(SendGrid.class)),
                    times(1));
            Mail mail = argumentCaptor.getValue();
            assertEquals(fromEmail, mail.getFrom().getEmail());
            assertEquals(asName, mail.getFrom().getName());
            assertEquals(templateId, mail.getTemplateId());
        }
    }

    @Test
    void sendEmailShouldFailOnNullParametersTest() {
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(null, null, null, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(null, name, link, templateId, asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, null, link, templateId, asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, name, null, templateId, asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, name, link, null, asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, name, link, templateId, null));
    }

    @Test
    void sendEmailShouldFailOnEmptyParametersTest() {
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail("", "", "", "", ""));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail("", name, link, templateId, asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, "", link, templateId, asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, name, "", templateId, asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, name, link, "", asName));
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail(emailTo, name, link, templateId, ""));

    }


    @Test
    void getPersonalizationTest() {
        Personalization personalization = MailService.getPersonalization(emailTo, name, link);
        assertEquals(emailTo, personalization.getTos().get(0).getEmail());
        assertEquals(name, personalization.getDynamicTemplateData().get("name"));
        assertEquals(link, personalization.getDynamicTemplateData().get("link"));
    }

    @Test
    void configureMailTest() {
        Mail mail = MailService.configureMail(templateId, asName, emailTo, name, link);
        assertEquals(fromEmail, mail.getFrom().getEmail());
        assertEquals(asName, mail.getFrom().getName());
        assertEquals(templateId, mail.getTemplateId());
    }

    @Test
    void invokeServiceEmailTest() throws IOException {
        SendGrid sg = Mockito.mock(SendGrid.class);
        Mail mail = MailService.configureMail(templateId, asName, emailTo, name, link);
        MailService.invokeServiceEmail(mail, sg);
        ArgumentCaptor<Request> argumentCaptor
                = ArgumentCaptor.forClass(Request.class);
        verify(sg, Mockito.times(1))
                .api(argumentCaptor.capture());
        Request request = argumentCaptor.getValue();
        assertEquals(mail.build(), request.getBody());
        assertEquals(Method.POST, request.getMethod());
        assertEquals("mail/send", request.getEndpoint());
    }

}