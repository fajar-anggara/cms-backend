package com.backendapp.cms.blogging.contract;

public class PostGeneratorContract {

    public static final String UNGENERATED_SLUG_FROM_TITLE;
    public static final String UNGENERATED_EXCERPT_FROM_CONTENT;

    public static final String GENERATED_SLUG;
    public static final String GENERATED_EXCERPT;

    static {
        UNGENERATED_SLUG_FROM_TITLE = "Ini adalah judul unik"; // Title that will result in a unique base slug
        GENERATED_SLUG = "ini-adalah-judul-unik";

        UNGENERATED_EXCERPT_FROM_CONTENT = "<p>Ini adalah <strong>konten utama</strong> dengan beberapa tag HTML. " +
                "Ini adalah kalimat kedua dari konten yang cukup panjang untuk diuji " +
                "pemotongan excerpt pada panjang default. Harusnya ini akan terpotong " +
                "dengan elipsis di akhir.</p>";
        GENERATED_EXCERPT = "Ini adalah konten utama dengan beberapa tag HTML. Ini adalah kalimat kedua dari konten yang cukup panjang untuk diuji pemotongan excerpt pada panjang default. Harusnya ini akan terpotong dengan...";
    }
}