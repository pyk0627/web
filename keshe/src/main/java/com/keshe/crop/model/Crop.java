package com.keshe.crop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 作物（非粮生物柴油植物）JavaBean。
 * oilMin/oilMax/oilCount 是联表查询出来的统计值，不是数据库表中的字段。
 */
public class Crop {

    private Long id;
    private String chineseName;
    private String englishName;
    private String latinName;
    private String alias;
    private String family;
    private String genus;
    private String features;
    private String distribution;
    private String cultivation;
    private String uses;
    private String imageUrl;
    private String referenceTitle;
    private String referenceUrl;
    private BigDecimal oilMin;
    private BigDecimal oilMax;
    private Integer oilCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 用于卡片和列表显示：只有一个值时显示单值，多个值时显示区间。
     */
    public String getOilSummary() {
        if (oilCount == null || oilCount == 0 || oilMin == null) {
            return "暂无数据";
        }
        String min = trimNumber(oilMin);
        String max = trimNumber(oilMax);
        if (min.equals(max)) {
            return min + "%";
        }
        return min + "% ~ " + max + "%";
    }

    public boolean isHasOilData() {
        return oilCount != null && oilCount > 0;
    }

    public String getInitial() {
        if (chineseName == null || chineseName.isBlank()) {
            return "?";
        }
        return chineseName.substring(0, 1);
    }

    public String getFamilyGenus() {
        String f = family == null ? "" : family;
        String g = genus == null ? "" : genus;
        if (f.isBlank() && g.isBlank()) {
            return "";
        }
        return f + " · " + g;
    }

    public String getUpdatedDate() {
        if (updatedAt == null) {
            return "";
        }
        return updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String trimNumber(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getCultivation() {
        return cultivation;
    }

    public void setCultivation(String cultivation) {
        this.cultivation = cultivation;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReferenceTitle() {
        return referenceTitle;
    }

    public void setReferenceTitle(String referenceTitle) {
        this.referenceTitle = referenceTitle;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

    public BigDecimal getOilMin() {
        return oilMin;
    }

    public void setOilMin(BigDecimal oilMin) {
        this.oilMin = oilMin;
    }

    public BigDecimal getOilMax() {
        return oilMax;
    }

    public void setOilMax(BigDecimal oilMax) {
        this.oilMax = oilMax;
    }

    public Integer getOilCount() {
        return oilCount;
    }

    public void setOilCount(Integer oilCount) {
        this.oilCount = oilCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
