package models

import play.api._
import scala.concurrent._
import play.api.libs.json._
import ExecutionContext.Implicits.global
import com.github.nscala_time.time.Imports._
import scalaj.http._

abstract class Comic {
  val RefreshInterval = 60 // Minutes.

  val comicRegex: scala.util.matching.Regex = null
  val id: String = ""
  val name: String = ""
  val siteUrl: String = ""
  val countryCodes: String = ""

  var lastUpdated: DateTime = DateTime.yesterday
  var stripUrl = ""
  
  implicit val comicWriter = new Writes[Comic] {
    def writes(c: Comic): JsValue = {
      Json.obj(
        "id" -> c.id,
        "name" -> c.name,
        "url" -> c.stripUrl
      )
    }
  }

  def json: Future[String] = {  
    val resultPromise = promise[String]

    future {
      if (comicRegex != null && (lastUpdated + RefreshInterval.minutes) < DateTime.now) {
        Logger.info("Cache expired. Fetching new comic URL.")
        try {
          val http = Http(siteUrl).header("User-Agent", "Scala Comic Server 1.0").option(HttpOptions.connTimeout(5000)).option(HttpOptions.readTimeout(10000)).asString
          comicRegex.findFirstIn(http) match {
            case Some(url) => stripUrl = url
            case None      => Logger.error("Could not find comic url for id: " + id)
          }
        } catch {
          case e: Exception => Logger.error("Error getting response from server: " + e.toString())
        }
        lastUpdated = DateTime.now
        
        resultPromise success Json.toJson(this).toString()
        Logger.info("Serving new result: " + stripUrl)          
        
      } else {
        Logger.info("Serving cached result: " + stripUrl)
        resultPromise success Json.toJson(this).toString()
      }
    }

    resultPromise.future 
  }
}

class MarshallsLaw extends Comic {
  override val id = "ml"
  override val name = "Marshall's Law"
  override val siteUrl = "https://www.facebook.com/marshallscomics"
  override val comicRegex = """https://fbcdn-sphotos-[a-z]-[a-z]\.akamaihd\.net/hphotos[0-9a-z\-]*/p480x480/[0-9_a-zA-Z]*.jpg""".r    
}

class Fingerpori extends Comic {
  override val id = "fp"
  override val name = "Fingerpori"
  override val siteUrl = "http://www.hs.fi/fingerpori"
  override val comicRegex = """http://[a-z\?=\.:/_0-9]*/sarjis/[a-z\?=\.:/_0-9]*""".r    
  override val countryCodes = "fi"
}

class ViiviJaWagner extends Comic {
  override val id = "vw"
  override val name = "Viivi ja Wagner"
  override val siteUrl = "http://www.hs.fi/viivijawagner/"
  override val comicRegex = """http://[a-z\?=\.:/_0-9]*/sarjis/[a-z\?=\.:/_0-9]*""".r    
  override val countryCodes = "fi"
}

class Sinfest extends Comic {
  override val id = "sf"
  override val name = "Sinfest"
  override val siteUrl = "http://www.sinfest.net/"
  override val comicRegex = """http://sinfest.net/[a-z\?=\.:/_0-9]*/comics/[a-z\?=\.:/_0-9-]*.gif""".r    
}

class Dilbert extends Comic {
  override val id = "dilbert"
  override val name = "Dilbert"
  override val siteUrl = "http://www.dilbert.com/strips/"
  override val comicRegex = """http://dilbert.com/dyn/str_strip[a-z./0-9].*.gif""".r    
}

class Userfriendly extends Comic {
  override val id = "uf"
  override val name = "UserFriendly"
  override val siteUrl = "http://www.userfriendly.org/"
  override val comicRegex = """http://www.userfriendly.org/cartoons/archives/[a-z0-9/]*.gif""".r    
}

class XKCD extends Comic {
  override val id = "x"
  override val name = "XKCD"
  override val siteUrl = "http://xkcd.com"
  override val comicRegex = """http://imgs.xkcd.com/comics/[a-z_./0-9]*""".r    
}

/**
* Windows Phone's ImageToolkit library cannot convert ths PhD Comic's GIF image to a 
* proper PNG image. Hence I am doing it on my server instead.
* Oh - and Windows Phone doesn't support GIF at all so...
*/
class PhDComic extends Comic {
  override val id = "phd"
  override val name = "PhD Comic"
  stripUrl = "http://kypeli.kapsi.fi/comics/phd.png"
//  override val siteUrl = "http://www.phdcomics.com/comics.php"
//  override val comicRegex = """http://www.phdcomics.com/comics/archive/[a-z_./0-9]*""".r
}

class QuestionableContent extends Comic {
  override val id = "qc"
  override val name = "Questionable Content"
  override val siteUrl = "http://questionablecontent.net/"
  override val comicRegex = """http://www.questionablecontent.net/comics/[0-9.a-z]*""".r 
}

class AnonyymitElaimet extends Comic {
  override val id = "ae"
  override val name = "Anonyymit Eläimet"
  override val siteUrl = "http://nyt.fi/category/sarjakuvat/"  
  override val comicRegex = """http://nyt.fi/wp-content/uploads/[a-z._\-/0-9]*ae[a-z._\-/0-9]*.jpg""".r
  override val countryCodes = "fi"
}

class FokIt extends Comic {
  override val id = "fi"
  override val name = "Fok_it"
  override val siteUrl = "http://nyt.fi/tag/fok_it-kaikki/"  
  override val comicRegex = """http://nyt.fi/wp-content/uploads/[a-z._\-/0-9]*fokit_[a-z._\-/0-9]*.jpg""".r
  override val countryCodes = "fi"
}

class ElComicSans extends Comic {
  override val id = "elcomic"
  override val name = "El Comic Sans"
  override val siteUrl = "http://www.elcomicsans.com/"  
  override val comicRegex = """http://www.elcomicsans.com/comics/[a-z.\-/0-9]*""".r
  override val countryCodes = "es,br,mx,ar"
}

class Bunsen extends Comic {
  override val id = "bunsen"
  override val name = "Bunsen"
  override val siteUrl = "http://www.heroeslocales.com/bunsen/"  
  override val comicRegex = """http://www.heroeslocales.com/bunsen/comics/[a-z\-./0-9]*""".r
  override val countryCodes = "es,br,mx,ar"
}

class AbstruseGoose extends Comic {
  override val id = "ag"
  override val name = "Abstruse Goose"
  override val siteUrl = "http://abstrusegoose.com/"  
  override val comicRegex = """http://abstrusegoose.com/strips/[A-Za-z._\-/0-9]*""".r
}

class Scandinavian extends Comic {
  override val id = "scan"
  override val name = "Scandinavia and the World"
  override val siteUrl = "http://satwcomic.com/"  
  override val comicRegex = """http://satwcomic.com/art/[A-Za-z._\-/0-9]*""".r
}

class NatalieD extends Comic {
  override val id = "nat"
  override val name = "Natalie Dee"
  override val siteUrl = "http://www.nataliedee.com"  
  override val comicRegex = """http://www\.nataliedee\.com/[0-9]*/[a-zA-Z0-9\-.]*.[jpg|png|gif]""".r
}

class ToothpasteForDinner extends Comic {
  override val id = "tpfd"
  override val name = "Toothpaste for Dinner"
  override val siteUrl = "http://www.toothpastefordinner.com"  
  override val comicRegex = """http://www\.toothpastefordinner\.com/[0-9]*/[a-zA-Z\-]*[.gif|.jpg|.png]*""".r
}

class GeekAndPoke extends Comic {
  override val id = "gap"
  override val name = "Geek & Poke"
  override val siteUrl = "http://www.geek-and-poke.com"  
  override val comicRegex = """http://static\.squarespace\.com/static/[0-9a-zA-Z\-/]*.jpg""".r
}

// We need to get the comic URL with something more than just a Regex since the complete URL is not available
// on the site's source.
class SomethingPositive extends Comic {
  override val id = "sp"
  override val name = "Something*Positive"
  override val siteUrl = "http://www.somethingpositive.net"  
  override val comicRegex = """""".r
}
