package com.endava.endabank.utils;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailServiceTest {
    private final String templateId = "d-432fd12818ba40b280f5f8ceeeea5063";
    private final String fromEmail = "bmgabi95@hotmail.com";
    private final String emailTo = "alejandrosuaza.1022@gmail.com";
    private final String asName = "test";
    private final String name = "testUser";
    private final String link = "http://test.com";

    @Test
    void sendEmailTries() throws IOException {
        try (MockedStatic<MailService> utilities = Mockito.mockStatic(MailService.class)) {
            SendGrid sg = Mockito.mock(SendGrid.class);
            Mail mail = new Mail();
            Email fromEmail = new Email();
            fromEmail.setName(asName);
            fromEmail.setEmail(this.fromEmail);
            mail.setFrom(fromEmail);
            mail.setTemplateId(templateId);
            Personalization personalization = new Personalization();
            personalization.addDynamicTemplateData("name", name);
            personalization.addDynamicTemplateData("link", link);
            personalization.addTo(new Email(emailTo));
            mail.addPersonalization(personalization);
            when(sg.api(any())).thenReturn(TestUtils.getSendGridResponse());
            utilities.when(MailService::getSendGrid).thenReturn(sg);
            utilities.when(() -> MailService.configureMail(templateId, asName, emailTo, name, link)).thenReturn(mail);
        }
        Response response = MailService.sendEmail(emailTo, name, link, templateId, asName);
        assertNotNull(response);
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