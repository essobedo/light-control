## v 1.0

* Initial version with a text field to enter content and a check box to simulate the right click.
* It is also possible to use the triple click/tap to enter content at a given position of the screen capture.
* The refresh frequency and the quality of the screen capture can be set manually thanks to a dedicated HTTP parameter

## v 1.1

* Introduces the mechanism allowing to auto adjust the frequency and the quality of the screen capture.
* Allows to generate the Jetty bundle directly at build time.
* Adds a mechanism allowing to keep in cache the latest screen capture and its corresponding hash

## v 1.2

* Introduces the special actions menu that appears on a triple click/tap
* Rewrite the code managing the input content by reducing the total amount of round trip when some content is entered and by managing the mapping between the char and the key codes thanks to resource bundles and to the way to enter unicode characters in the different OS.
* Added the version id and the build date at the end of the page

## v 1.3

* Fixed the issue with IE that provides non integer coordinates

## v 1.4

* Upgraded to jetty 9.2.10.v20150310
* Added the release note into the generated bundle
* Fixed the bugs found by FindBugs
* Removed the remaining System.out.println