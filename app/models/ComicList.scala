package models

import com.top10.redis._
import play.api.libs.json._

case class Comic(id: String, name: String) {
  var lastUpdate = None
  var stripUrl = None

/*  implicit object ComicFormat extends Format[Comic] {
//    def reads(json: JSValue) = 
    def writes(c: Comic) = JsObject(Seq
      (
        "id" -> JsString(id),
        "name" -> JsString(name),
        "url" -> JsString(url)
      ))
  }
*/
}

case class ComicList()

object ComicList {
  implicit val comicFormat = Json.format[Comic]
  private val comicList = List(Comic("wv", "Viivi ja Wagner"),
                               Comic("sf", "Sinfest"),
                               Comic("fp", "Fingerpori")
                              )

  val json = Json.toJson(Json.obj("comics" -> comicList))
}


//    val redis = new SingleRedis("localhost", 6379)
//    redis.set("foo", "bar")
//    Ok(redis.get("foo").get)
