package com.jaboumal.services;

import com.jaboumal.util.ConfigUtil;
import com.jaboumal.util.DateUtil;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PDFServiceTest {

    private PDFService pdfService;

    @BeforeEach
    void setUp() {
        ConfigUtil.loadConfigFile();
        pdfService = new PDFService();
    }

    @Test
    void createPDF() {
        String barcode = "iVBORw0KGgoAAAANSUhEUgAAALQAAABGCAAAAABA/OH8AAAEe0lEQVR4Xu2V0U4bARDE+P+fTgvBN3YaQk6VUKk4qbvZiT3cQ0tfXn4/l8vrn9d5va6frvt17nt/U7Yk39trWgfSbc2c6rS8jcY0Ybbkyuc1rQPptmZOdVreRmOaMFty5fOa1oF0WzOnOi1vozFNmC258nlN60C6rZlTnZa30ZgmzJZc+bymdSDd1sypTsvbaEwTZkuufF7TOpBua+ZUp+VtNKYJsyVXPq9pHUi3NXOq0/I2GtOE2ZIrn9e0DqTbmjnVaXkbjWnCbMmVz2taB9JtzZzqtLyNxjRhtuTK5zWtA+m2Zk51Wt5GY5owW3Ll85rWgXRbM6c6LW+jMU2YLbnyeU3rQLqtmVOdlrfRmCbMllz5vKZ1IN3WzKlOy9toTBNmS658XtM6kG5r5lSn5W00pgmzJVc+r2kdSLc1c6rT8jYa04TZkiuf17QOpNuaOdVpeRuNacJsyZXPa1oH0m3NnOq0vI3GNGG25MrnNa0D6bZmTnVa3kZjmjBbcuXzmtaBdFszpzotb6MxTZgtufJ5TetAuq2ZU52Wt9GYJsyWXPm8pnUg3dbMqU7L22hME2ZLrnxe0zqQbmvmVKflbTSmCbMlVz6vaR1ItzVzqtPyNhrThNmSK5/XtA6k25o51Wl5G41pwmzJlc9rWgfSbc2c6rS8jcY0Ybbkyuc1rQPptmZOdVreRmOaMFty5fOa1oF0WzOnOi1vozFNmC258nlN60C6rZlTnZa30ZgmzJZc+bymdSDd1sypTsvbaEwTZkuufF7TOpBua+ZUp+VtNKYJsyVXPq9pHUi3NXOq0/I2GtOE2ZIrn9e0DqTbmjnVaXkbjWnCbMmVz2taB9JtzZzqtLyNxjRhtuTK5zWtA+m2Zk51Wt5GY5owW3Ll85rWgXRbM6c6LW+jMU2YLbnyeU3rQLqtmVOdlrfRmCbMllz5vKZ1IN3WzKlOy9toTBNmS658XtM6kG5r5lSn5W00pgmzJVc+r2kdSLc1c6rT8jYa04TZkiuf17QOpNuaOdVpeRuNacJsyZXPa1oH0m3NnOq0vI3GNGG25MrnNa0D6bZmTnVa3kZjmjBbcuXzmtaBdFszpzotb6MxTZgtufJ5TetAuq2ZU52Wt9GYJsyWXPm8pnUg3dbMqU7L22hME2ZLrnxe0zqQbmvmVKflbTSmCbMlVz6vaR1ItzVzqtPyNhrThNmSK5/XtA6k25o51Wl5G41pwmzJlc9rWgfSbc2c6rS8jcY0Ybbkyuc1rQPptmZO3+Pv9fy89Fc9Py/9Vc///9LH7xr92smvpdtv//z0hr2H97KnnjPoC/T1RyXbfH+RD4boe9lzzwn0ctD7mce+/cF9rRoPsueeE+jl0Uvffr73vse31w/3sueeM+ynL31E/KV9+0w08I9vj+y55wz7yUv7nxIgf+fvGse3yp56TsEPX7pNuz5+6Y+yT59T8KOXvik6zuuHe0Y+nnqPU/CDl970fdf463c+Q+v/gOOf1ZHdfHl8a5mPi/j2zGtcztL/yPPz0l/1fMuX/gU2ZU1ZsO8RKwAAAABJRU5ErkJggg==";
        String outputFile = pdfService.createPDF("Hans", "Peter", DateUtil.stringToDate("01.01.2000"), barcode, true);

        File file = new File(String.format(outputFile, 1));

        assertEquals("src/test/resources/output/Berchtoldschiessen_Hans_Peter_%d.png", outputFile);
        assertNotNull(file);
        assertTrue(file.exists());

        file.delete();
    }

}