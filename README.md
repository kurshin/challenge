# TastyTrade Take-Home Coding Challenge Solution<br/><br/>

### This document describes how to set up and run the project:
<br/>
1. Clone or download the sources from this repository to any convenient location. You should have a folder named /challenge.<br/><br/>
2. Open Android Studio (Jellyfish is recommended but not mandatory).<br/><br/>
3. In the Android Studio (AS) welcome screen, press Open and choose the /challenge folder from Step 1.<br/><br/>
4. When the project is opened and loaded in AS, you will see an error in the Build tab: "localProperties.getProperty("apiKey") must not be null". This is expected. The project uses the IEX Cloud API to retrieve information about stocks. The API requires an apiKey for authentication. For security purposes, the apiKey is stored locally and is not committed within the project sources.<br/><br/>
5. To set up the apiKey, go to the local.properties file in the project root and add the "apiKey" property. Set your value as an IEX API public key. The proper local.properties setup should look like this:<br/><br/>
sdk.dir=some_path<br/>
apiKey=pk_your_key<br/><br/>
6. Press Try Again to sync the project with the Gradle files.<br/><br/>
7. When the Gradle sync is finished, you will be able to run the application either on a simulator or on a real device.<br/><br/><br/>

### Notice regarding IEX Cloud API
Although the project requires an apiKey setup for successful compilation, it is configured to use an offline response generator by default (StockSimulationRepositoryImpl). This is because IEX suddenly stopped offering free access on June 1st. The free trial was canceled, and new subscriptions were no longer allowed. Since all HTTP requests were ready and tested by this time, I decided to simulate server responses with some delays and move forward. <br/><br/>
If you have a valid subscription, feel free to set up real IEX server usage by replacing StockSimulationRepositoryImpl with StockRepositoryImpl in AppModule.kt (you will find detailed instructions there).<br/><br/>
Otherwise, just run the application and observe randomized prices and charts.<br/><br/><br/>

### Unit Tests
The unit tests can be found under the "com.tastytrade.kurshin" package marked as "(test)".<br/><br/>
Feel free to run the whole package with coverage in AS and enjoy the numbers.<br/><br/><br/>

### UI Tests
The UI tests can be found under the "com.tastytrade.kurshin.presentation" package marked as "(androidTest)".<br/><br/>
You will need a device or simulator to run those. Unlike unit tests, UI tests are provided more as examples rather than for actual use. This is because the UI is more like a sketch in this case. For a commercial application, these tests can be expanded and supplemented.<br/><br/><br/>

### Theming
The application supports a native night mode switch. To observe it in action, simply turn on/off the dark mode in the settings of your device (usually accessible from the quick settings top bar).<br/><br/><br/>

I wish you enjoy the app and the code. Happy reviewing! 😎
