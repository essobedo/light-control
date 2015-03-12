# light-control

## What is it for?

The main target of this project is to be able to take control of a given server from any devices that can open a simple
and light web page within the same local network.

## What do I need?

The only thing to install is a *Java SE Runtime Environment* on the target server. It has been successfully tested with Java 8 but
it should also work with Java 6 and 7 without any issues.

## How to build it?

This project relies on *maven*, so you will need to install it with a JDK of your choice, then simply launch the famous
command *mvn clean install* and that's it!

## How to launch it?

As I wanted something as light as possible, I decided to rely on *Jetty* so the generated bundle is actually a *Jetty*
distribution that has been modified to configure the *JAAS* realm of the project called *LC Realm* and to deploy the web
application of the project.

So to launch it, simply unzip *light-control-${version}.zip*, go to the bin directory and launch *./jetty.sh*.

## How to access it?

Once the *Jetty* instance is properly launched, simply open a browser (tested with Firefox, Chrome, Safari) of the device of your choice then go to
*http://${ip-address-of-the-target-server}:8080*. The default account is *admin* / *@dmin@*, if you want to change it for
something else, you simply need to modify the file *etc/login.properties* knowing that the syntax is *${login}: MD5:${hash-of-your-password},admin*.
To know the hash of your password, you can use this web site http://md5.gromweb.com

## How to use it?

NB: What is described below as *tap* is actually a relatively slow click on a mobile device. This is needed to be able to
simulate a double click or more on a mobile device since a double click is already used to zoom on a picture.

* For a simple (left) click, simply *click or tap* (for mobile) on the screen capture.
* For a double click, simply *click twice or tap twice* on the screen capture.
* For a right click, check first the checkbox that has been added for this purpose then click or tap on the screen capture, the
checkbox will be automatically unchecked once done.

In case you want to enter some content, simply enter the content in the input text that has been added for this purpose,
everything that you will enter in this field will be propagated over the target sever.
Clicking on the button *OK* is similar to pressing *Enter* in the text field.

As it is not always convenient to use this text field to enter content as you could not see what you're actually typing,
I added the notion of *triple click / tap* to allow to activate the keyboard at the place of your choice in the screen capture.
If the *triple click / tap* has been detected, the text field should disappear, it will only reappear when you will click
anywhere else.

The last things to know are the *Refresh frequency* and the *Image quality*. The *Refresh frequency* is the time
after which it will check if a refresh is needed, it is expressed in milliseconds. The *Image quality* is the actually
the compression quality which is a float between 0 and 1 knowing that 0 is the worse quality and 1 the best quality.

You can manually adjust the:
* *Refresh frequency* using the HTTP parameter *f* as described by the corresponding info icon.
* *Image quality* using the HTTP parameter *q* as described by the corresponding info icon.

However they will be automatically adjusted according to the response time of the different requests.

## What else I need to know?

As you may have noticed, this project relies on the *BASIC* authentication which means that the password is easily accessible from
the HTTP headers so if you intend to use it in non secure network, I highly encourage you to configure SSL as described here
https://wiki.eclipse.org/Jetty/Howto/Configure_SSL
