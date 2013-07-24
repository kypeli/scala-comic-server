Scala Comic Server
==================

The Scala Comic Server is written in [Scala](http://www.scala-lang.org/) [duh] using [Play 2.1](http://www.playframework.com/) to serve meta data in JSON format about comics from around the web. It's intended to be used as a backend server for a client application. 

Supported comics
----------------
The following comics are currently supported:

*   [Dilbert](http://dilbert.com)
*   [Sinfest](http://sinfest.net)
*   [UserFriendly](http://www.userfriendly.org)
*   [XKCD](http://xkcd.com)
*   [PhD Comics](http://www.phdcomics.com/comics.php)
*   [Questionable Content](http://questionablecontent.net/)
*   [Abstruse Goose](http://abstrusegoose.com/)
*   [Scandinavia and the World](http://satwcomic.com/)
*   [Natalie Dee](http://www.nataliedee.com)
*   [Toothpaste for Dinner](http://www.toothpastefordinner.com)
*   [Marshall's Law](https://www.facebook.com/marshallscomics)
*   [El Cómic Sans](http://www.elcomicsans.com/) 
    * Regions: Spain, Mexico, Argentina, Brazil
*   [Bunsen](http://http://www.heroeslocales.com/bunsen/) 
    * Regions: Spain, Mexico, Argentina, Brazil
*   [Fingerpori](http://www.hs.fi/fingerpori) 
    * Regions: Finland
*   [Viivi ja Wagner](http://www.hs.fi/viivijawagner/) 
    * Regions: Finland
*   [Fok_It](http://nyt.fi/tag/fok_it-kaikki) 
    * Regions: Finland
*   [Anonyymit Eläimet](http://nyt.fi/category/sarjakuvat/) 
    * Regions: Finland

How it works
------------
The server will serve data about the comics in JSON format so that a client can show the most recent strip of a given comic. **The server itself does not serve images nor store any images**. 

The JSON data will be cached for 60 minutes on the Scala Comic Server after which the comic information is fetched again and updated from the remote comic web server. This way the most recent comic strip is available to the client within 60 minutes after it's being published on the website.

As specified above, some comics are regional and will only be listed for people coming from those countries. By default, and if not specified above, the comic is served to everyone. Thus, the server does a reverse geolocation lookup on the client's IP address to find out from which country the request originates from. 

REST APIs
---------
The server has the following REST APIs.

* */list* - returns a list of available comics for the client to show to the user. JSON response: 

```json
      {
         "comics": [
            {
                "comicid": "sf",
                "name": "Sinfest"
            },
            ...
            {
                "comicid": "uf",
                "name": "UserFriendly"
            }
         ]
      }
```    

* */comic?id=[comicid]* - returns information about a specified comic. JSON response for comicid=sf 

```json
      {
         "id": "sf",
         "name": "Sinfest",
         "url": "http://sinfest.net/comikaze/comics/2013-07-20.gif"
      }
```

Wanna try it right away?
------------------------
The server is up and running without authentication, so go ahead and use the following REST APIs now:

*   http://scala-comic-server.herokuapp.com/list
*   http://scala-comic-server.herokuapp.com/comic?id=[comicid]

**NOTE:** The server is for evaluation purposes only and is operating on a best effort principle. It's not guaranteed that the server will be up at any specifiec moment. 

Clients
-------
The following clients use this server already:

*   [Daily Comics for Windows Phone](http://www.windowsphone.com/en-gb/store/app/daily-comics/c0d9a840-8463-4c5d-b881-f2022552f9c4)

TODO
----
The server is not complete. I am also looking forward to contributions if you find that some comic should be supported but it's not, or if you can improve the code in some other ways. 

Here are some things that I should do at some point:

*   Make fetching of the 'stripUrl' parameter for each comic a bit more generic. Not all comics can be fetched with a simple regexp (like Something*Positive). 
*   Add more information about the comics, like when they were published.
*   PhD Comics is actually still served from my old server because issues in the Windows Phone client that I have. 

License
-------
The code for the server is licensed under the [GPLv2 license](http://www.gnu.org/licenses/gpl-2.0.html). 
