package models

import play.api._
import play.api.libs.ws._
import play.api.libs.concurrent._
import scala.concurrent._
import com.top10.redis._
import play.api.libs.json._
import ExecutionContext.Implicits.global

case class Comic(id: String, name: String) {
  var lastUpdate = "Barbar"
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

//  def json: JsValue = Json.toJson(this)  
def json: Future[String] = {  
  val resultPromise = promise[String]

  future {
    if (false) {
      WS.url("http://www.hs.fi").get().map { response =>
        resultPromise success response.body
      }
    } else {
      resultPromise success "Testing"
    }
  }

  resultPromise.future 
}

  

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

object Comics {
  implicit val comicListWriter = new Writes[Comic] {
    def writes(c: Comic): JsValue = {
      Json.obj(
        "id" -> c.id,
        "name" -> c.name
      )
    }
  }

  private val comicList = List(Comic("wv", "Viivi ja Wagner"),
                               Comic("sf", "Sinfest"),
                               Comic("fp", "Fingerpori")
                              )

  val listjson = Json.toJson(Json.obj("comics" -> comicList))

  def comicJson(id: String): Future[String] = comicList.find(c => c.id == id).get.json
}


//    val redis = new SingleRedis("localhost", 6379)
//    redis.set("foo", "bar")
//    Ok(redis.get("foo").get)
