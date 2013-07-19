package models

import play.api._
import play.api.libs.ws._
import scala.concurrent._
import play.api.libs.json._
import ExecutionContext.Implicits.global
import com.github.nscala_time.time.Imports._

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
        WS.url(siteUrl).get().map { response =>
          Logger.info("Have result.")
          try {
            val http = response.body
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
        }
      } else {
        Logger.info("Serving cached result: " + stripUrl)
        resultPromise success Json.toJson(this).toString()
      }
    }

    resultPromise.future 
  }
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

class PhDComic extends Comic {
  override val id = "phd"
  override val name = "PhD Comic"
  stripUrl = "http://kypeli.kapsi.fi/comics/phd.png"
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
