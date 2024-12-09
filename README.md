
# Secure Shelled Messenger

This android application provides a private messaging platform for users. Using symmetric end to end encryption. The application allows a user to create an account as well as create contacts with the intended recievers' usernames. When creating a contact the user will assign a private key to that contact. This key can be changed in the future; however, switching this key will not allow the decryption of previous messages. If the user switches back to the original key they will be able to see the decrypted messages once again. This allows for another layer of privacy because it allows each message or groups of messages to be encrypted and decrypted using different keys. The user is able to edit a contact's name and key; however, the contact's username is not changeable. Another ability that the user has is to change the theme of the application to selections like Dark Mode, High Contrast, etc. 

### Running the application


To run this application, clone this repository to an Android Studio environment. After successfully cloning, wait for Gradle to finish building the application. If you have an emulator ready, click the green arrow (run) at the top of Android Studio to install and run the application on the Android emulator. If you want to have the application on an android device seperate from a computer, Android Studio gives you the option to install the application on a connected device. 

Included in the repository are the server files that make up the API that the application uses to store and gather user information. To run the application connected to a private server, these files can be changed to suit your needs. 

