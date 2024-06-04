package com.example.autocashsys.converter;

import com.example.autocashsys.dto.DefaultProductDto;
import com.example.autocashsys.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class DefaultProductConverterTest {

    private static final String IMAGESRC_FIELD_NAME = "defaultImageSrc";

    private static final String DEFAULT_IMAGESRC = "defaultImageSrc";
    private static final String IMAGESRC = "imageSrc";
    private static final Integer ID = 1;
    private static final String NAME = "Name";
    private static final String MANUFACTURER = "Manufacturer";
    private static final BigDecimal PRICE = BigDecimal.valueOf(100.0);
    private static final String BARCODE = "Barcode";
    private static final boolean IS_REQUIRED_VERIFICATION_TRUE = true;

    @InjectMocks
    private DefaultProductConverter converter;

    @Mock
    private Product source;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(converter, IMAGESRC_FIELD_NAME, DEFAULT_IMAGESRC);
    }

    @Test
    public void convertTest_withImageSrc() {
        Mockito.when(source.getImageSrc()).thenReturn(IMAGESRC);

        DefaultProductDto result = converter.convert(source);

        Assertions.assertEquals(IMAGESRC, result.getImageSrc());
    }

    @Test
    public void convertTest_withoutImageSrc() {
        Mockito.when(source.getImageSrc()).thenReturn(null);

        DefaultProductDto result = converter.convert(source);

        Assertions.assertEquals(DEFAULT_IMAGESRC, result.getImageSrc());
    }

    @Test
    public void convertTest_withoutImageSrc_andDefaultImageSrcIsNull() {
        Mockito.when(source.getImageSrc()).thenReturn(null);

        ReflectionTestUtils.setField(converter, IMAGESRC_FIELD_NAME, null);

        DefaultProductDto result = converter.convert(source);

        Assertions.assertNull(result.getImageSrc());
    }

    @Test
    public void convertTest_withFieldValues() {
        Mockito.when(source.getId()).thenReturn(ID);
        Mockito.when(source.getName()).thenReturn(NAME);
        Mockito.when(source.getManufacturer()).thenReturn(MANUFACTURER);
        Mockito.when(source.getPrice()).thenReturn(PRICE);
        Mockito.when(source.getBarcode()).thenReturn(BARCODE);
        Mockito.when(source.getIsRequiredVerification()).thenReturn(IS_REQUIRED_VERIFICATION_TRUE);

        DefaultProductDto result = converter.convert(source);

        Assertions.assertEquals(ID, result.getId());
        Assertions.assertEquals(NAME, result.getName());
        Assertions.assertEquals(MANUFACTURER, result.getManufacturer());
        Assertions.assertEquals(PRICE, result.getPrice());
        Assertions.assertEquals(BARCODE, result.getBarcode());
        Assertions.assertTrue(result.getIsRequiredVerification());
    }
}