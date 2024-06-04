package com.example.autocashsys.converter;

import com.example.autocashsys.dto.DefaultProductDto;
import com.example.autocashsys.entity.Product;
import com.example.autocashsys.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DefaultProductConverter implements Converter<Product, DefaultProductDto> {

    @Value("${auto-cash-sys.default-img-src.product}")
    private String defaultImageSrc;

    @NonNull
    @Override
    public DefaultProductDto convert(Product source) {
        DefaultProductDto target = new DefaultProductDto();
        target.setId(source.getId());   // It's not required. The barcode is enough (it is not advisable to take the internal ID outside (to the frontend)).
        String imageSrc = source.getImageSrc();
        if (StringUtils.isEmpty(imageSrc)) {
            target.setImageSrc(defaultImageSrc);
        } else {
            target.setImageSrc(imageSrc);
        }
        target.setName(source.getName());
        target.setManufacturer(source.getManufacturer());
        target.setPrice(source.getPrice());
        target.setBarcode(source.getBarcode());
        target.setIsRequiredVerification(source.getIsRequiredVerification());
        return target;
    }
}
