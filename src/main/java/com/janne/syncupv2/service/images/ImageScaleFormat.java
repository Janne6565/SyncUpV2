package com.janne.syncupv2.service.images;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum ImageScaleFormat {
    FULL_SCREEN(1920, 1080),
    VERTICAL_BANNER(456, 100),
    HORIZONTAL_BANNER_V1(480, 1020),
    HORIZONTAL_BANNER_V2(348, 748),
    RECTANGLE_ICON(100, 100),
    RECTANGLE_LARGE(1024, 1024);

    private final int width;
    private final int height;

    public float getWidthHeightRelation() {
        return (float) width / height;
    }

    public static ImageScaleFormat detectScaleFromImage(BufferedImage image) {
        ImageScaleFormat[] imageScaleFormats = ImageScaleFormat.values();
        ImageScaleFormat[] smallestIndexFound = getResolutionsWithMatchingWidthHeightRelation(imageScaleFormats, (float) image.getWidth() / image.getHeight());
        Optional<ImageScaleFormat> formatWithSmallestWidthDifference = Arrays.stream(smallestIndexFound).min((o1, o2) -> Math.abs(o1.getWidth() - image.getWidth()) - Math.abs(o2.getWidth() - image.getWidth()));
        if (formatWithSmallestWidthDifference.isEmpty()) {
            throw new RuntimeException("Couldnt auto detect Image resolution for " + image.getWidth() + " " + image.getHeight());
        }
        return formatWithSmallestWidthDifference.get();
    }

    private static @NotNull ImageScaleFormat[] getResolutionsWithMatchingWidthHeightRelation(ImageScaleFormat[] imageScaleFormats, float widthHeightRelation) {
        Stream<Float> differences = Arrays.stream(imageScaleFormats).map(imageScaleFormat -> Math.abs(widthHeightRelation - (float) imageScaleFormat.width / imageScaleFormat.height));
        ImageScaleFormat[] matchingFormats = Arrays.stream(imageScaleFormats).filter(imageScaleFormat -> imageScaleFormat.getWidthHeightRelation() == widthHeightRelation).toArray(ImageScaleFormat[]::new);
        return matchingFormats;
    }
}
