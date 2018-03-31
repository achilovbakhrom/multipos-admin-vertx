package com.basicsteps.multipos.config

enum class EndpointUriOperationId(val endpoint: String) {

    //sign in & sign up
    SIGN_UP("sign-up"),
    SIGN_IN("sign-in"),
    CONFIRM_ACCESS_CODE("confirm"),
    IS_EMAIL_EXISTS("check-email"),
    GET_ACCESS_CODE("access-code"),
    VERIFICATION("verification"),
    UPLOAD_AVATAR("upload-avatar"),

    //units
    GET_UNITS("units"),
    GET_UNIT_CATEGORY_LIST("unit-category-list"),

    //company
    ADD_COMPANY("add-company"),
    ADD_POS("add-pos"),
    ADD_STOCK("add-stock"),
    LINK_POS_STOCK("link-pos-stock"),
    GET_COMPANIES_BY_USERNAME("company-list"),

    //currency
    GET_CURRENCY_LIST("currency-list"),

    //units
    DEACTIVATE_UNIT("deactivate-unit"),
    ACTIVATE_UNIT("activate-unit"),

    //account
    ADD_ACCOUNT("add-account"),
    GET_ACCOUNT_LIST("get-account-list"),
    UPDATE_ACCOUNT("udpdate-account"),
    GET_ACCOUNT_BY_ID("get-account"),
    REMOVE_ACCOUNT("remove-account"),
    REMOVE_ACCOUNT_LIST("remove-account-list"),
    DEACTIVATE_ACCOUNT("deactive-account"),
    DEACTIVATE_ACCOUNT_LIST("deactive-account-list"),
    ACTIVATE_ACCOUNT("activate-account"),
    ACTIVATE_ACCOUNTS_LIST("activate-accounts-list")



}