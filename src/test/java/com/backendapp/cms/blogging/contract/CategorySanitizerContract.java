package com.backendapp.cms.blogging.contract;

public class CategorySanitizerContract {

    public static String UNSANITIZED_NAME;
    public static String UNSANITIZED_SLUG;
    public static String SANITIZED_NAME;
    public static String SANITIZED_SLUG;

    static {
        UNSANITIZED_NAME = "<script>alert('XSS')</script><b>Hello</b> World & <img src='x' onerror='alert(1)'>";
        SANITIZED_NAME = "Hello World &amp; ";

        UNSANITIZED_SLUG = "Hello World &amp; ";
        SANITIZED_SLUG = "hello-world-amp-";
    }
}
