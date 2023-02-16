# Google oauth plugin for GoCD

## Requirements

* GoCD server version v17.5.0 or above
* Google [API credentials](https://console.developers.google.com/apis/credentials)

## Installation

Copy the file `build/libs/google-iap-authorization-plugin-VERSION.jar` to the GoCD server under `${GO_SERVER_DIR}/plugins/external`
and restart the server. The `GO_SERVER_DIR` is usually `/var/lib/go-server` on Linux and `C:\Program Files\Go Server`
on Windows.

## Configuration

### Configure Google API credentials

1. Login to [API credentials](https://console.developers.google.com/apis/credentials)
1. Click on **_Create credentials_** and select `OAuth Client Id`
1. Select `Web application` as a Application type
1. Provide appropriate **_Name_** to your api credentials
1. Specify `http://<<your.go.server.hostname.or.ip>>/go/plugin/cd.go.authorization.google/authenticate` in **_Authorized redirect URIs_**. Click on **_Create_**
1. Yay!! You have created google api credentials
1. Note **_Client ID_** and **_Client Secret_**, you will need it to configure plugin in next step

### Configure Plugin

1. Login to `GoCD server` as admin and navigate to **_Admin_** _>_ **_Security_** _>_ **_Authorization Configuration_**
1. Click on **_Add_** to create new authorization configuration
    1. Specify `id` for auth config
    1. Select `Google oauth authorization plugin for GoCD` for **_Plugin id_**
    1. Specify **_Client ID_** and **_Client Secret_**
    1. Optionally, you can specify `Allowed Domains` settings to restrict user login from specified domains
    1. Save your configuration
