# Light-Control

## What is it for?

The main target of this project is to be able to take control of a given server from any device that can open a simple
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

So to launch it, simply unzip *light-control-${version}.zip*, go to the bin directory and launch *./jetty.sh*. On Windows 
environment, you will need to launch it manually by launching from the root directory of the project 
*java -Djetty.logs=./logs -Djetty.home=. -Djetty.base=. -Djava.io.tmpdir=/tmp -jar start.jar jetty.state=./jetty.state jetty-logging.xml jetty-started.xml* 

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
* In case you want to enter some content, simply enter the content in the input text that has been added for this purpose,
everything that you will enter in this field will be propagated over the target server. Clicking on the button *OK* is similar 
to pressing *Enter* in the text field. 
* To execute specific actions at the place of your choice in the screen capture, you will need to *triple click / tap* 
on the screen capture to show the menu then click on the action of your choice, the available item menus are:
    * *The right click item menu* is similar to the right click described before except that you don't rely on the checkbox anymore.
    * *The enter content item menu* is similar to the right click described before except that you will make the keyboard appear near the place you want to input some content which is much better in term of user experience as you could not see what you're actually typing with the other mode. Please note that the text field will disappear when you use this mode, it will only reappear when you will click anywhere else.
    * *The page up item menu* allows you to emulate what you can do with a wheel mouse to display the content of the previous page.
    * *The page down item menu* allows you to emulate what you can do with a wheel mouse to display the content of the next page.
    * *The move pointer item menu* allows you to move the pointer of the mouse to a specific location without performing a click which
can be interesting in case you need to interact with components that perform some actions when the mouse is over them.

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

## In which maven repositories can I found the binaries of the project?

So far the binaries are compiled with *Java 8* and published on *maven central* from http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22light-control%22
and *sonatype* from https://oss.sonatype.org/content/groups/public/com/github/essobedo/light-control/
