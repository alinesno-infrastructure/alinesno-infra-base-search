package com.alinesno.infra.base.search.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public interface TextSplitter {
    List<String> splitText(String text);
}

