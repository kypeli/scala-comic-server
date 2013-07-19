package models

import play.api._
import play.api.libs.ws._
import scala.concurrent._
// import com.top10.redis._
import play.api.libs.json._
import ExecutionContext.Implicits.global
import com.github.nscala_time.time.Imports._

abstract class Comic {
  val comicRegex: scala.util.matching.Regex = null
  val id: String = ""
  val name: String = ""
  val siteUrl: String = ""

  var lastUpdated: DateTime = DateTime.yesterday
  var stripUrl = "someurl"
  
  implicit val comicWriter = new Writes[Comic] {
    def writes(c: Comic): JsValue = {
      Json.obj(
        "id" -> c.id,
        "name" -> c.name,
        "stripUrl" -> c.stripUrl
      )
    }
  }

  def json: Future[String] = {  
    val resultPromise = promise[String]

    future {
      if ((lastUpdated + 60.minutes) < DateTime.now) {
        Logger.info("Cache expired. Fetching new comic URL.")
        WS.url(siteUrl).get().map { response =>
          stripUrl = comicRegex.findFirstIn(response.body).get
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
}

class ViiviJaWagner extends Comic {
  override val id = "vw"
  override val name = "Viivi ja Wagner"
  override val siteUrl = "http://www.hs.fi/viivijawagner/"
  override val comicRegex = """http://[a-z\?=\.:/_0-9]*/sarjis/[a-z\?=\.:/_0-9]*""".r    
}

class Sinfest extends Comic {
  override val id = "sf"
  override val name = "Sinfest"
  override val siteUrl = "http://www.sinfest.net/"
  override val comicRegex = """http://sinfest.net/[a-z\?=\.:/_0-9]*/comics/[a-z\?=\.:/_0-9-]*.gif""".r    
}

