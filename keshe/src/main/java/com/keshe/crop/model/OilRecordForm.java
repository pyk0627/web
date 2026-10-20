package com.keshe.crop.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

/**
 * 管理员新增/编辑含油率记录时使用的表单。
 */
public class OilRecordForm {

    private Long id;

    @NotNull(message = "请选择作物")
    private Long cropId;

    @Size(max = 100, message = "采集单位不能超过 100 个字符")
    private String collectionUnit;

    @Size(max = 100, message = "测试单位不能超过 100 个字符")
    private String testUnit;

    @Size(max = 100, message = "测试部位不能超过 100 个字符")
    private String testPart;

    @Size(max = 150, message = "产地不能超过 150 个字符")
    private String origin;

    @DecimalMin(value = "0.0", message = "含油率不能小于 0")
    @DecimalMax(value = "100.0", message = "含油率不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "含油率格式不正确")
    @NumberFormat(pattern = "#0.###")
    private BigDecimal oilContent;

    @DecimalMin(value = "0.0", message = "碘值不能小于 0")
    @Digits(integer = 5, fraction = 3, message = "碘值格式不正确")
    private BigDecimal iodineValue;

    @DecimalMin(value = "0.0", message = "酸值不能小于 0")
    @Digits(integer = 5, fraction = 3, message = "酸值格式不正确")
    private BigDecimal acidValue;

    @DecimalMin(value = "0.0", message = "皂化值不能小于 0")
    @Digits(integer = 5, fraction = 3, message = "皂化值格式不正确")
    private BigDecimal saponificationValue;

    @DecimalMin(value = "0.0", message = "C12:0 不能小于 0")
    @DecimalMax(value = "100.0", message = "C12:0 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C12:0 格式不正确")
    private BigDecimal c12;

    @DecimalMin(value = "0.0", message = "C14:0 不能小于 0")
    @DecimalMax(value = "100.0", message = "C14:0 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C14:0 格式不正确")
    private BigDecimal c14;

    @DecimalMin(value = "0.0", message = "C16:0 不能小于 0")
    @DecimalMax(value = "100.0", message = "C16:0 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C16:0 格式不正确")
    private BigDecimal c16;

    @DecimalMin(value = "0.0", message = "C16:1 不能小于 0")
    @DecimalMax(value = "100.0", message = "C16:1 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C16:1 格式不正确")
    private BigDecimal c16_1;

    @DecimalMin(value = "0.0", message = "C18:0 不能小于 0")
    @DecimalMax(value = "100.0", message = "C18:0 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C18:0 格式不正确")
    private BigDecimal c18;

    @DecimalMin(value = "0.0", message = "C18:1 不能小于 0")
    @DecimalMax(value = "100.0", message = "C18:1 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C18:1 格式不正确")
    private BigDecimal c18_1;

    @DecimalMin(value = "0.0", message = "C18:2 不能小于 0")
    @DecimalMax(value = "100.0", message = "C18:2 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C18:2 格式不正确")
    private BigDecimal c18_2;

    @DecimalMin(value = "0.0", message = "C18:3 不能小于 0")
    @DecimalMax(value = "100.0", message = "C18:3 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C18:3 格式不正确")
    private BigDecimal c18_3;

    @DecimalMin(value = "0.0", message = "C20:0 不能小于 0")
    @DecimalMax(value = "100.0", message = "C20:0 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C20:0 格式不正确")
    private BigDecimal c20;

    @DecimalMin(value = "0.0", message = "C20:1 不能小于 0")
    @DecimalMax(value = "100.0", message = "C20:1 不能大于 100")
    @Digits(integer = 4, fraction = 3, message = "C20:1 格式不正确")
    private BigDecimal c20_1;

    @Size(max = 200, message = "备注不能超过 200 个字符")
    private String remark;

    public static OilRecordForm from(OilRecord record) {
        OilRecordForm form = new OilRecordForm();
        form.setId(record.getId());
        form.setCropId(record.getCropId());
        form.setCollectionUnit(record.getCollectionUnit());
        form.setTestUnit(record.getTestUnit());
        form.setTestPart(record.getTestPart());
        form.setOrigin(record.getOrigin());
        form.setOilContent(record.getOilContent());
        form.setIodineValue(record.getIodineValue());
        form.setAcidValue(record.getAcidValue());
        form.setSaponificationValue(record.getSaponificationValue());
        form.setC12(record.getC12());
        form.setC14(record.getC14());
        form.setC16(record.getC16());
        form.setC16_1(record.getC16_1());
        form.setC18(record.getC18());
        form.setC18_1(record.getC18_1());
        form.setC18_2(record.getC18_2());
        form.setC18_3(record.getC18_3());
        form.setC20(record.getC20());
        form.setC20_1(record.getC20_1());
        form.setRemark(record.getRemark());
        return form;
    }

    public OilRecord toOilRecord() {
        OilRecord record = new OilRecord();
        record.setId(id);
        record.setCropId(cropId);
        record.setCollectionUnit(trim(collectionUnit));
        record.setTestUnit(trim(testUnit));
        record.setTestPart(trim(testPart));
        record.setOrigin(trim(origin));
        record.setOilContent(oilContent);
        record.setIodineValue(iodineValue);
        record.setAcidValue(acidValue);
        record.setSaponificationValue(saponificationValue);
        record.setC12(c12);
        record.setC14(c14);
        record.setC16(c16);
        record.setC16_1(c16_1);
        record.setC18(c18);
        record.setC18_1(c18_1);
        record.setC18_2(c18_2);
        record.setC18_3(c18_3);
        record.setC20(c20);
        record.setC20_1(c20_1);
        record.setRemark(trim(remark));
        return record;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

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
