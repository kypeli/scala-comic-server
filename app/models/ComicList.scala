package models

import play.api._
import play.api.libs.ws._
import scala.concurrent._
import com.top10.redis._
import play.api.libs.json._
import ExecutionContext.Implicits.global
import com.github.nscala_time.time.Imports._

object Comics {
  private val IPINFODB_API_KEY = "a779ad1639f0a3bbaec16755bd2afb33132070b4a5a8542761e7100b606dfb3d"
  private val IPINFODB_URL     = """http://api.ipinfodb.com/v3/ip-city/?format=json&key=""" + IPINFODB_API_KEY + """&ip="""


  implicit val comicListWriter = new Writes[Comic] {
    def writes(c: Comic): JsValue = {
      Json.obj(
        "comicid" -> c.id,
        "name" -> c.name
      )
    }
  }

  private val comicList = List(new Dilbert,    // Play2 WS.Response can't handle the invalid MIME type that dilbert.com gives us. 
                               new Fingerpori,
                               new ViiviJaWagner,
                               new Sinfest,
                               new Userfriendly,
                               new XKCD,
                               new PhDComic,
                               new QuestionableContent,
                               new AnonyymitElaimet,
                               new FokIt,
                               new ElComicSans,
                               new Bunsen,
                               new AbstruseGoose,
                               new Scandinavian,
                               new NatalieD,
                               new ToothpasteForDinner,
                               new MarshallsLaw 
                             )

  def listjson(remoteIP: String): Future[JsValue] = {
    WS.url(IPINFODB_URL + remoteIP).get().map { response => 
        Logger.info("Request from: " + remoteIP + ", " + (response.json \ "countryCode") + ", City: " + (response.json \ "cityName"))
      val country = (response.json \ "countryCode").as[String].toLowerCase
      Json.toJson(Json.obj("comics" -> comicList.filter(c => (c.countryCodes == "" || c.countryCodes.contains(country)) )))
    }
  }

  def comicJson(id: String): Future[String] = comicList.find(c => c.id == id).get.json
}
