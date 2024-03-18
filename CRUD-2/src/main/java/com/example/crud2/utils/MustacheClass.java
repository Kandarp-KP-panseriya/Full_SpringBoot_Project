package com.example.crud2.utils;

import com.samskivert.mustache.Mustache;
import lombok.Data;

@Data
public class MustacheClass {
    public String getTemplate(String html, Object data) {
        return Mustache.compiler().escapeHTML(false).compile(html).execute(data);
    }

}
