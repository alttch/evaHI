What is evaHI?
--------------

evaHI is Android application template which allow you to quickly build custom
Android app viewer for your mobile web application.

Why do I need this?
-------------------

Of course, users can just open your web application via mobile web browser.

But evaHI provides additional features:

* Your web-app will look cool just as any native Android app and your custom
  viewer app can be added to Google Play Store

* You can hard-code connection parameters of your web-app

* The app can automatically pass basic authentication forms

* The app provides for user native navigation menu with custom icons and menu
  labels

* Icons, menu labels and target URLs can be changed dynamically on your web-app
  server so users don't need to download application updates again and again.

* The app can switch URLs depending is current device mode portrait or
  landscape.

Looks cool. How can I build my own app?
---------------------------------------

Firstly, let's customize app source
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* Download evaHI source (git clone https://github.com/alttch/evaHI)

* Edit prepare.ini file

* Run ./prepare -D <your_app_dir>

* Go to <your_app_dir>, customize app icons in ./app/src/main/res (optional)

* Build your app with Android Studio or other Android build tool

Then, create configuration file on your web server
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* Create .evahi directory in your web server http root

* Put there the following configuration file. The file should be named
  config.yml, config.yaml or config.json (if JSON format is used)

```yaml
serial: 1
index: /index.html
index_landscape: /landscape.html
home_icon: h.jpg
menu:
- {icon: pages/page1.png, name: Page 1, url: /page1.html}
- {icon: pages/contacts.png, name: Contacts, url: /contacts.html}
```

All icons should be placed in /.evahi/icons and have relative paths in
app configuration.

The app caches configuration settings and icons, but reloads them if **serial**
field is increased.

Any examples?
-------------

Our EVA ICS (https://www.eva-ics.com/) Control Center Android app is fully
evaHI-based. Check it out:

https://play.google.com/store/apps/details?id=com.altertech.evacc

