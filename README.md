# Flutter with Android AAR File

This project demonstrates how to use Flutter to build Android project within an AAR file.

## References
* [accessing platform and third-party services in Flutter](https://flutter.io/platform-services/).
* [Example of embedding Flutter using FlutterView](https://github.com/flutter/flutter/tree/master/examples/hello_services)

## Configure Android AAR File
### Auto configuration with Android Studio
1. Import **android** folder to **Android Studio**.
2. Click **File > New > New Module** and choose **Import .JAR/.AAR Package**.
3. Press **F4** to open **Project Structure**, and then add the dependent module.

### Manual configuration

1. Create a new folder **android/DynamsoftBarcodeReader**. Create **android/DynamsoftBarcodeReader/build.gradle**:
  
   ```
   configurations.maybeCreate("default")
   artifacts.add("default", file('DynamsoftBarcodeReader.aar')) 
   ```
   Copy **DynamsoftBarcodeReader.aar** to this folder:

   ![flutter aar](http://www.codepool.biz/wp-content/uploads/2017/01/flutter-aar.png)

2. Edit **android/settings.gradle**:

   ```
   include ':app', ':DynamsoftBarcodeReader'
   ```

3. Add the dependency to **android\app\build.gradle**:

   ```
   dependencies {
    androidTestCompile 'com.android.support:support-annotations:22.0.0'
    androidTestCompile 'com.android.support.test:runner:0.5'
    androidTestCompile 'com.android.support.test:rules:0.5'
    compile project(':DynamsoftBarcodeReader')
   }
   ```

## Build the project

Import the project to **Intellij IDEA** and then select an Android device to run.
![flutter app with Android AAR](http://www.codepool.biz/wp-content/uploads/2017/01/flutter-barcode-reader-small.png)

