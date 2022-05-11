package com.endava.endabank.constants;

public interface Strings {
    String ROLE_NOT_FOUND = "The role was not found on the database.";
    String IDENTIFIER_TYPE_NOT_FOUND = "The identifier type was not found on the database.";
    String USER_NOT_FOUND = "The user was not found on the database.";
    String MAIL_SENT = "Mail Sent";
    String MAIL_FAIL = "There was an error sending the mail.";
    String OLD_PASSWORD_NOT_MATCH = "The old password does not match.";
    String PASSWORDS_DOES_NOT_MATCH = "The passwords do not match.";
    String TOKEN_RESET_PASSWORD_INVALID = "The token is not valid for this user.";
    String USER_TOKEN_NOT_FOUND = "The token for this user was not found on the database.";
    String PASSWORD_UPDATED = "Password updated.";
    String CONSTRAINT_IDENTIFIER_VIOLATED = "There is already a user registered with this identifier.";
    String CONSTRAINT_EMAIL_VIOLATED = "There is already a user registered with this email.";
    String SECRET_JWT = System.getenv("JWT_ENDABANK_SECRET");
    String MESSAGE_RESPONSE = "message";
    String STATUS_CODE_RESPONSE = "statusCode";
    String BAD_DATA_FOR_TOKEN_GENERATION = "The data required for token generation not provided.";
    String EMAIL_VERIFIED = "All set, the email was successfully verified.";
    String EMAIL_NOT_VERIFIED = "The user email has not been verified yet.";
    String EMAIL_FOR_VERIFICATION_SENT = "User registered, Check your email for the link for verification.";
    String EMAIL_ALREADY_VERIFIED = "The email was already verified.";
    String EMAIL_AS_VERIFY_EMAIL = "Email Verification";
    String EMAIL_AS_RESET_PASSWORD = "Reset Password";
    String BAD_DATA_FOR_TOKEN_VERIFICATION = "The data required for the verification is missing.";
    String ACCESS_TOKEN = "accessToken";
    String ROL = "rol";
    String IS_APPROVED = "isApproved";
    String USER_BODY = "user";
    String AUTHORITIES_REQUIRED = "Authorities are required";
}
