package syl.gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by QYF on 2016/5/6.
 */
public final class resultModel {
    private static StringProperty result = new SimpleStringProperty();

    public static String getResult() {
        return result.get();
    }

    public static StringProperty resultProperty() {
        return result;
    }

    public static void setResult(String result) {
        resultModel.result.set(result);
    }
}
