package com.keshe.crop.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CropTest {

    @Test
    void formatsSingleOilValue() {
        Crop crop = cropWithOil("10.650", "10.650", 1);

        assertEquals("10.65%", crop.getOilSummary());
        assertTrue(crop.isHasOilData());
    }

    @Test
    void formatsOilRange() {
        Crop crop = cropWithOil("6.150", "25.320", 4);

        assertEquals("6.15% ~ 25.32%", crop.getOilSummary());
    }

    @Test
    void showsPlaceholderWhenNoOilData() {
        Crop crop = new Crop();
        crop.setOilCount(0);

        assertEquals("暂无数据", crop.getOilSummary());
        assertFalse(crop.isHasOilData());
    }

    @Test
    void buildsInitialAndTaxonomyText() {
        Crop crop = new Crop();
        crop.setChineseName("苏铁");
        crop.setFamily("苏铁科");
        crop.setGenus("苏铁属");

        assertEquals("苏", crop.getInitial());
        assertEquals("苏铁科 · 苏铁属", crop.getFamilyGenus());
    }

    private Crop cropWithOil(String min, String max, int count) {
        Crop crop = new Crop();
        crop.setOilMin(new BigDecimal(min));
        crop.setOilMax(new BigDecimal(max));
        crop.setOilCount(count);
        return crop;
    }
}
