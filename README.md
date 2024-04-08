# Oblig3_Dat153

## Test Results

After running

```sh
./gradlew connectedAndroidTest
```

we got the following results

<img src="./images/terminalResult.png" />

### What happends after we run the command

The command initiates a kind of process where your app and the test
cases are compiled into separate APKs. This is being done do the emulator
can run the test cases isolated in a way. So the APKs are installed on the
emulator. The command also starts the execution of the tests that interact
with the emulator and the app that was built. After command is finsihed
testing and building we get back some output that tells us about the tests
if they failed or was correct.
The APK files that we observed being installed upon running the tests were:
 - app-debug.apk
     - This file contains a source and a resource folder. Both of these folders
       has a lot of folders inside them, and it seems to be files and folders related
       to the setup and the environment for the project, in the form of decompiled
       Java code. It also contains all the XML resources for the project, including
       layouts, manifest, images, strings etc.  
 - app-debug-androidTest.apk
     - This file contains a folder of resources, which has the manifest file, three
       .dex files and also some other xml contents. The file also contains a folder
       called sources, which contains a lot of files for the testing environment,
       including annotations, intents, JUnit an so on. In addition to this, it contains
       The classes for the activites that are used in the tests, in the form of decompiled
       Java code.


<hr />

## Test Cases

**MainActivityTest**
- Tests that the main activity renders. Tests that hitting the button with the label Gallery, launches the GalleryActivity.
- Expected result: The application hits the button "Gallery" and launches a new Activity.
- The test is implemented in MainActivityTest.java

<img src="./images/MainActivityTest.png" />

<br />

**GalleryActivityTest**
- The test launches the GalleryActivity, then tries to upload an image with our imagepicker by using a intent stub. Then the test will try to get this item from the UI, validating that the image was added.
- Further, the test will delete the image the test just added, and then validate that the image is removed from the UI.
- We manually add the name for the image in the test, because espresso needs more implementation for accessing alerts.
- Expected result: The application adds a new image entry, and then deletes it successfully.
- The test is implemented in GalleryActivityTest.java

<img src="./images/GalleryActivityTest.png" />

<br />

**QuizActivityTest**
- The test launches the QuizActivity, then submits the correct answer to the first quiestion in the quiz. Then validates that the score was updated correctly by pattern matching the score in our toast message. After this, it submits a wrong answer and validates the score again.
- Expected result: The application starts a quiz, then submits a correct answer then a wrong one, and the score is updated accordingly. 
- The test is implemented in QuizActivityTets.java

<img src="./images/QuizActivityTest.png" />
