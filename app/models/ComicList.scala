package models

import play.api._
import play.api.libs.ws._
import scala.concurrent._
import com.top10.redis._
import play.api.libs.json._
import ExecutionContext.Implicits.global
import com.github.nscala_time.time.Imports._

object Comics {
  implicit val comicListWriter = new Writes[Comic] {
    def writes(c: Comic): JsValue = {
      Json.obj(
        "id" -> c.id,
        "name" -> c.name
      )
    }
  }

  private val comicList = List(new Fingerpori,
                               new ViiviJaWagner,
                               new Sinfest)
  val listjson = Json.toJson(Json.obj("comics" -> comicList))

  def comicJson(id: String): Future[String] = comicList.find(c => c.id == id).get.json
}


//    val redis = new SingleRedis("localhost", 6379)
//    redis.set("foo", "bar")
//    Ok(redis.get("foo").get)
