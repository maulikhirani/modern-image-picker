
# Modern Image Picker
Sample app to demonstrate image picker using Gallery and Camera

Selecting image/Taking a photo is a very common feature that many apps need these days (i.e Profile Picture). 

Even though this is a very basic feature, many developers have misconceptions about permissions, APIs and the ways to handle the Photo Taking and Selecting an Image from Gallery.

Here are some common misconceptions that some have:

Misconception 1:

**CAMERA** permission is required for taking a photo.

Reality:

You don't need CAMERA permission if your purpose is just to take a photo and use it for some purpose like uploading it to cloud. You can use [ACTION_IMAGE_CAPTURE](https://developer.android.com/training/camera/photobasics#TaskCaptureIntent) intent to use existing camera app to take photo, without the need to requesting the CAMERA permission.

Misconception 2:

**READ_EXTERNAL_STORAGE** and/or **WRITE_EXTERNAL_STORAGE** permissions are required for taking a photo or for selecting image from Gallery/File Managers.

Reality:

If the purpose of taking a photo or selecting the image from Gallery is private to your app, you don't need these permissions. For example, if you just want to take a picture an upload it to cloud or select existing image from Gallery, you can use **ACTION_IMAGE_CAPTURE** and **ACTION_GET_CONTENT** intents. 
For taking a photo from camera, you can temporariry save the photo to file your app-specific private directory which will be removed when your app is uninstalled.

This app demonstartes the image selection functionality through Camera and Gallery without declaring any permission and with only the required code in less number of lines.

