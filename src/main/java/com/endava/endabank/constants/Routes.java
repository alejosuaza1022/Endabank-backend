package com.endava.endabank.constants;


public final class Routes {
    public final static String API_ROUTE = "/api/v1";
    public final static String USERS_ROUTE = "/users";
    public final static String LOGIN_ROUTE = "/login";
    public final static String FRONTEND_ROUTE = "http://localhost:3000";
    public final static String RESET_PASSWORD_ROUTE = "/reset-password";
    public final static String APPROVE_ACCOUNT_ROUTE = "/approve/{id}";
    public final static String RESET_PASSWORD_FRONTEND_ROUTE = FRONTEND_ROUTE + "/reset-password/?token=";
    public final static String UPDATE_PASSWORD = "/change-password";
    public final static String SWAGGER_IU = "/swagger-ui.html/**";
    public final static String SWAGGER_JSON = "/v2/api-docs/**";
    public final static String CONFIGURATION = "/configuration/**";
    public final static String SWAGGER = "/swagger*/**";
    public final static String WEB_JARS = "/webjars/**";
    public final static String RESOURCE_DETAILS = "/details";


}
