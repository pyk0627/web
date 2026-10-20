package com.keshe.crop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 管理员新增/编辑作物时使用的表单。
 */
public class CropForm {

    private Long id;

    @NotBlank(message = "中文名不能为空")
    @Size(max = 100, message = "中文名不能超过 100 个字符")
    private String chineseName;

    @Size(max = 150, message = "英文名不能超过 150 个字符")
    private String englishName;

    @Size(max = 150, message = "拉丁学名不能超过 150 个字符")
    private String latinName;

    @Size(max = 200, message = "别名不能超过 200 个字符")
    private String alias;

    @NotBlank(message = "科不能为空")
    @Size(max = 100, message = "科名不能超过 100 个字符")
    private String family;

    @NotBlank(message = "属不能为空")
    @Size(max = 100, message = "属名不能超过 100 个字符")
    private String genus;

    @Size(max = 4000, message = "特征描述不能超过 4000 个字符")
    private String features;

    @Size(max = 4000, message = "分布描述不能超过 4000 个字符")
    private String distribution;

    @Size(max = 4000, message = "栽培描述不能超过 4000 个字符")
    private String cultivation;

    @Size(max = 4000, message = "用途描述不能超过 4000 个字符")
    private String uses;

    @Pattern(regexp = "^$|^https?://.+|^/images/.+", message = "图片地址应为 http(s) 链接或 /images/ 开头的本地路径")
    @Size(max = 255, message = "图片地址不能超过 255 个字符")
    private String imageUrl;

    @Size(max = 200, message = "参考资料标题不能超过 200 个字符")
    private String referenceTitle;

    @Pattern(regexp = "^$|^https?://.+", message = "参考资料链接应以 http:// 或 https:// 开头")
    @Size(max = 500, message = "参考资料链接不能超过 500 个字符")
    private String referenceUrl;

    public static CropForm from(Crop crop) {
        CropForm form = new CropForm();
        form.setId(crop.getId());
        form.setChineseName(crop.getChineseName());
        form.setEnglishName(crop.getEnglishName());
        form.setLatinName(crop.getLatinName());
        form.setAlias(crop.getAlias());
        form.setFamily(crop.getFamily());
        form.setGenus(crop.getGenus());
        form.setFeatures(crop.getFeatures());
        form.setDistribution(crop.getDistribution());
        form.setCultivation(crop.getCultivation());
        form.setUses(crop.getUses());
        form.setImageUrl(crop.getImageUrl());
        form.setReferenceTitle(crop.getReferenceTitle());
        form.setReferenceUrl(crop.getReferenceUrl());
        return form;
    }

    public Crop toCrop() {
        Crop crop = new Crop();
        crop.setId(id);
        crop.setChineseName(trim(chineseName));
        crop.setEnglishName(trim(englishName));
        crop.setLatinName(trim(latinName));
        crop.setAlias(trim(alias));
        crop.setFamily(trim(family));
        crop.setGenus(trim(genus));
        crop.setFeatures(trim(features));
        crop.setDistribution(trim(distribution));
        crop.setCultivation(trim(cultivation));
        crop.setUses(trim(uses));
        String image = trim(imageUrl);
        crop.setImageUrl(image == null || image.isBlank() ? "/images/crops/placeholder.svg" : image);
        crop.setReferenceTitle(trim(referenceTitle));
        crop.setReferenceUrl(trim(referenceUrl));
        return crop;
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
}
