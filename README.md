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

<hr />

## Test Cases

**MainActivityTest**
tests that the button for navigating to GalleryActivity is triggered
on click and that the Activity is launched.

<img src="./images/MainActivityTest.png" />

<br />

**GalleryActivityTest**
tests adding a image, validating that the image is added to the UI,
then deletes it and validates the UI again.

<img src="./images/GalleryActivityTest.png" />

<br />

**QuizActivityTest**
tests that clicking on a button gives the correct score by matching
the score on the screen.

<img src="./images/QuizActivityTest.png" />
