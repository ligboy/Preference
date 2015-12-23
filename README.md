# Preference
Preference library extends Preference support library

### Features
 * Attributable EditTextPreference  
 * NumberPickerPreference (sdk >= 14 now)  
 * DropDownPreference  
 * SeekBarPreference
 * SeekBarDialogPreference
 
### Usage

#### Gradle Dependency  
```gradle
dependencies {
    compile "org.ligboy.library:preference:0.2.0@aar"
}
```   

### Usage
Extend org.ligboy.preference.PreferenceFragment or org.ligboy.preference.PreferenceFragmentCompat, 
implement onCreatePreferences(Bundle, String)
```java
public class SettingsCompatFragment extends PreferenceFragmentCompat {

    public static SettingsCompatFragment newInstance() {
        return new SettingsCompatFragment();
    }

    public SettingsCompatFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_main);
    }

}
```

#### Style & Theme
``` 
      <item name="preferenceTheme">@style/PreferencePlusThemeOverlay.v14.Material</item>
```  

### LICENSE
```
Copyright 2015 Ligboy Liu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```