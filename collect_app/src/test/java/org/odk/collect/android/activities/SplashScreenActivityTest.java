package org.odk.collect.android.activities;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.application.initialization.ApplicationInitializer;
import org.odk.collect.android.application.initialization.SettingsPreferenceMigrator;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.android.injection.config.AppDependencyComponent;
import org.odk.collect.android.injection.config.AppDependencyModule;
import org.odk.collect.android.logic.PropertyManager;
import org.odk.collect.android.preferences.GeneralKeys;
import org.odk.collect.android.preferences.PreferencesRepository;
import org.odk.collect.android.storage.StorageInitializer;
import org.odk.collect.android.support.RobolectricHelpers;
import org.odk.collect.utilities.UserAgentProvider;
import org.robolectric.annotation.LooperMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class SplashScreenActivityTest {

    private ApplicationInitializer applicationInitializer;
    private PreferencesRepository preferencesRepository;

    @Before
    public void setup() {
        applicationInitializer = mock(ApplicationInitializer.class);

        RobolectricHelpers.mountExternalStorage();
        RobolectricHelpers.overrideAppDependencyModule(new AppDependencyModule() {
            @Override
            public ApplicationInitializer providesApplicationInitializer(Application application, UserAgentProvider userAgentProvider,
                                                                         SettingsPreferenceMigrator preferenceMigrator, PropertyManager propertyManager,
                                                                         Analytics analytics, StorageInitializer storageInitializer, PreferencesRepository preferencesRepository) {
                return applicationInitializer;
            }
        });
        AppDependencyComponent component = DaggerUtils.getComponent(ApplicationProvider.<Collect>getApplicationContext());
        preferencesRepository = component.preferencesRepository();
    }

    @Test
    public void whenShowSplashScreenEnabled_showSplashScreen() {
        preferencesRepository.getGeneralPreferences().save(GeneralKeys.KEY_SHOW_SPLASH, true);

        ActivityScenario<SplashScreenActivity> scenario1 = ActivityScenario.launch(SplashScreenActivity.class);
        assertThat(scenario1.getState(), is(Lifecycle.State.RESUMED));
        scenario1.onActivity(activity -> {
            assertThat(activity.findViewById(R.id.splash_default).getVisibility(), is(View.VISIBLE));
        });
    }
}
