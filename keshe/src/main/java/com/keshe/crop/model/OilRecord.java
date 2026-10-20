package com.keshe.crop.model;

import java.math.BigDecimal;

/**
 * 含油率及化学组分数据 JavaBean，一条记录对应一种测试结果。
 */
public class OilRecord {

    private Long id;
    private Long cropId;
    private String cropName; // 联表查询字段，不是数据库表字段
    private String collectionUnit;
    private String testUnit;
    private String testPart;
    private String origin;
    private BigDecimal oilContent;
    private BigDecimal iodineValue;
    private BigDecimal acidValue;
    private BigDecimal saponificationValue;
    private BigDecimal c12;
    private BigDecimal c14;
    private BigDecimal c16;
    private BigDecimal c16_1;
    private BigDecimal c18;
    private BigDecimal c18_1;
    private BigDecimal c18_2;
    private BigDecimal c18_3;
    private BigDecimal c20;
    private BigDecimal c20_1;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCropId() {
        return cropId;
    }

    public void setCropId(Long cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCollectionUnit() {
        return collectionUnit;
    }

    public void setCollectionUnit(String collectionUnit) {
        this.collectionUnit = collectionUnit;
    }

    public String getTestUnit() {
        return testUnit;
    }

    public void setTestUnit(String testUnit) {
        this.testUnit = testUnit;
    }

    public String getTestPart() {
        return testPart;
    }

    public void setTestPart(String testPart) {
        this.testPart = testPart;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public BigDecimal getOilContent() {
        return oilContent;
    }

    public void setOilContent(BigDecimal oilContent) {
        this.oilContent = oilContent;
    }

    public BigDecimal getIodineValue() {
        return iodineValue;
    }

    public void setIodineValue(BigDecimal iodineValue) {
        this.iodineValue = iodineValue;
    }

    public BigDecimal getAcidValue() {
        return acidValue;
    }

    public void setAcidValue(BigDecimal acidValue) {
        this.acidValue = acidValue;
    }

    public BigDecimal getSaponificationValue() {
        return saponificationValue;
    }

    public void setSaponificationValue(BigDecimal saponificationValue) {
        this.saponificationValue = saponificationValue;
    }

    public BigDecimal getC12() {
        return c12;
    }

    public void setC12(BigDecimal c12) {
        this.c12 = c12;
    }

    public BigDecimal getC14() {
        return c14;
    }

    public void setC14(BigDecimal c14) {
        this.c14 = c14;
    }

    public BigDecimal getC16() {
        return c16;
    }

    public void setC16(BigDecimal c16) {
        this.c16 = c16;
    }

    public BigDecimal getC16_1() {
        return c16_1;
    }

    public void setC16_1(BigDecimal c16_1) {
        this.c16_1 = c16_1;
    }

    public BigDecimal getC18() {
        return c18;
    }

    public void setC18(BigDecimal c18) {
        this.c18 = c18;
    }

    public BigDecimal getC18_1() {
        return c18_1;
    }

    public void setC18_1(BigDecimal c18_1) {
        this.c18_1 = c18_1;
    }

    public BigDecimal getC18_2() {
        return c18_2;
    }

    public void setC18_2(BigDecimal c18_2) {
        this.c18_2 = c18_2;
    }

    public BigDecimal getC18_3() {
        return c18_3;
    }

    public void setC18_3(BigDecimal c18_3) {
        this.c18_3 = c18_3;
    }

    public BigDecimal getC20() {
        return c20;
    }

    public void setC20(BigDecimal c20) {
        this.c20 = c20;
    }

    public BigDecimal getC20_1() {
        return c20_1;
    }

    public void setC20_1(BigDecimal c20_1) {
        this.c20_1 = c20_1;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
