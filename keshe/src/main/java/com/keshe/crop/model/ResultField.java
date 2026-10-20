package com.keshe.crop.model;

import java.util.Arrays;
import java.util.List;

/**
 * 查询结果中可以选择显示的项目。
 */
public enum ResultField {

    CHINESE_NAME("chineseName", "中文名"),
    ENGLISH_NAME("englishName", "英文名"),
    LATIN_NAME("latinName", "拉丁学名"),
    ALIAS("alias", "别名"),
    FAMILY("family", "科"),
    GENUS("genus", "属"),
    FEATURES("features", "特征"),
    DISTRIBUTION("distribution", "分布"),
    CULTIVATION("cultivation", "栽培"),
    USES("uses", "用途"),
    OIL("oil", "含油率");

    private final String code;
    private final String label;

    ResultField(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static List<ResultField> all() {
        return Arrays.asList(values());
    }

    public static List<ResultField> defaults() {
        return List.of(CHINESE_NAME, ENGLISH_NAME, FAMILY, GENUS, OIL, DISTRIBUTION, USES);
    }

    public static boolean isValidCode(String code) {
        return all().stream().anyMatch(f -> f.code.equals(code));
    }

    public static ResultField fromCode(String code) {
        return all().stream()
                .filter(f -> f.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
