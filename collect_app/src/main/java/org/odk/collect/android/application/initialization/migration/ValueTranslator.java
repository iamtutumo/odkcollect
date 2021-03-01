package org.odk.collect.android.application.initialization.migration;

import org.odk.collect.android.preferences.PreferencesDataSource;

import java.util.Map;

import static org.odk.collect.android.utilities.SharedPreferencesUtils.put;

public class ValueTranslator implements Migration {

    private final String oldValue;
    private String newValue;
    private String key;

    public ValueTranslator(String oldValue) {
        this.oldValue = oldValue;
    }

    public ValueTranslator toValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public ValueTranslator forKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public void apply(PreferencesDataSource prefs) {
        if (!prefs.contains(key)) {
            return;
        }

        Map<String, ?> all = prefs.getAll();
        Object prefValue = all.get(key);

        if (prefValue.equals(oldValue)) {
            put(prefs, key, newValue);
        }
    }
}
