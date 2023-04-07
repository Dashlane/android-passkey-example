# Android Passkey Example

[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](https://github.com/username/repo/blob/master/LICENSE)

![Licence](showcase.gif)

The purpose of this demo application is to showcase the implementation of Passkey in a native Android application. It also serves
as an example for developers who want to incorporate Passkey authentication into their own Android apps. This code application require
to have Android 14 or higher installed to work, as this is the first version that supports Passkey.

## What is Passkey?

Passkey is a technology developed by the FIDO alliance, a consortium of tech companies including Google, Microsoft, and Apple. Passkeys are
a replacement for passwords that provide faster, easier, and more secure sign-ins to websites and apps across a user’s devices. Unlike
passwords, passkeys are always strong and phishing-resistant. Passkeys simplify account registration for apps and websites, are easy to use,
work across most of a user’s devices, and even work on other devices within physical proximity.

## Features

You can create a Passkey and log in with inside the demo application. It's possible thanks to
the [Credential Manager API](https://developer.android.com/jetpack/androidx/releases/credentials) made by Google, which bring Passkey
support to Android and follows the [WebAuthn API](https://w3c.github.io/webauthn/).

All accounts are stored locally (in shared preferences), which means that if you uninstall or clear data, your account will be lost. There
is no server communication, and therefore no internet permission is needed.

For each login, we generate a challenge (random string) and verify that this challenge has been correctly signed with the private key by the
authenticator (Google Password Manager, Dashlane, etc.). This step is usually done by a server, but as we keep all the data locally, we are
doing this step on the client-side (not recommended).

You can log in to a specific account by entering an email that already exists in the local database. By doing that, the `allowCredentials`
attribute of a Passkey request will be set with a user ID, and only this credential can be returned by the authenticator.

## How to Test the Application

You can clone and build this application to generate a debug APK, or you can directly download the APK from the GitHub "Release" section.

## Contributing

We are open to contributions. Feel free to submit a pull request with your changes. Here are some features that could be added to this
application:

- Add other supported algorithms from [this list](https://www.iana.org/assignments/cose/cose.xhtml#algorithms)
- Remove a specific local account without the need to uninstall or remove all data
- Upgrade the Credential Manager library to the latest version

## License

This project is licensed under the Apache 2.0 License.