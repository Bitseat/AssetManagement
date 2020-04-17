package com.example.assetmanagement.activities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GtsrbModelConfig {
    public static String MODEL_FILENAME = "converted_model.tflite";

    public static final int INPUT_IMG_SIZE_WIDTH = 256;
    public static final int INPUT_IMG_SIZE_HEIGHT = 256;
    public static final int FLOAT_TYPE_SIZE = 4;
    public static final int PIXEL_SIZE = 3;
    public static final int MODEL_INPUT_SIZE = FLOAT_TYPE_SIZE * INPUT_IMG_SIZE_WIDTH * INPUT_IMG_SIZE_HEIGHT * PIXEL_SIZE;
    public static final int IMAGE_MEAN = 0;
    public static final float IMAGE_STD = 255.0f;

    //This list can be taken from notebooks/output/labels_readable.txt 
    public static final List<String> OUTPUT_LABELS = Collections.unmodifiableList(
            Arrays.asList(
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9"
            ));

    public static final int MAX_CLASSIFICATION_RESULTS = 3;
    public static final float CLASSIFICATION_THRESHOLD = 0.1f;
}
