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

  private val comicList = List(new Dilbert, 
                               new Fingerpori,
                               new ViiviJaWagner,
                               new Sinfest,
                               new Userfriendly,
                               new XKCD,
                               new PhDComic,
                               new QuestionableContent,
                               new AnonyymitElaimet,
                               new FokIt)

  def listjson(remoteIP: String): Future[JsValue] = {
    Logger.info("Got request from: " + remoteIP)
    WS.url(IPINFODB_URL + remoteIP).get().map { response => 
        Logger.info("Geo info: " + response.body)
      val country = (response.json \ "countryCode").as[String].toLowerCase
      Json.toJson(Json.obj("comics" -> comicList.filter(c => (c.countryCodes == "" || c.countryCodes.contains(country)) )))
    }
  }
//   val listjson = Json.toJson(Json.obj("comics" -> comicList))

  def comicJson(id: String): Future[String] = comicList.find(c => c.id == id).get.json
}


//    val redis = new SingleRedis("localhost", 6379)
//    redis.set("foo", "bar")
//    Ok(redis.get("foo").get)
