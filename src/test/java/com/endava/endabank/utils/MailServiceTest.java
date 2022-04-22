package com.endava.endabank.utils;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MailServiceTest {
    private final String templateId = System.getenv("SENDGRID_TEMPLATE_ID");
    private final String fromEmail = System.getenv("SENDGRID_MAIL_FROM");
    private final String emailTo = "test1@test.com";
    private final String asName = "test";
    private final String name = "testUser";
    private final String link = "http://test.com";

    @Test
    void sendEmail() {

    }
    @Test
    void sendEmailShouldFailOnNullParameters() {
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
    void sendEmailShouldFailOnEmptyParameters(){
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
        assertThrows(IllegalArgumentException.class, () ->
                MailService.sendEmail("", "", "", "", ""));
    }


    @Test
    void getPersonalization() {
        Personalization personalization = MailService.getPersonalization(emailTo, name, link);
        assertEquals(emailTo, personalization.getTos().get(0).getEmail());
        assertEquals(name, personalization.getDynamicTemplateData().get("name"));
        assertEquals(link, personalization.getDynamicTemplateData().get("link"));
    }

    @Test
    void configureMail() {
        Mail mail = MailService.configureMail(templateId, asName, emailTo, name, link);
        assertEquals(fromEmail, mail.getFrom().getEmail());
        assertEquals(asName, mail.getFrom().getName());
        assertEquals(templateId, mail.getTemplateId());
    }

    @Test
    void invokeServiceEmail() {
    }

}