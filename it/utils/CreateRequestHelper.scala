package utils

import config.SessionKeys
import org.scalatestplus.play.ServerProvider
import play.api.Application
import play.api.http.HeaderNames
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSResponse}

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}


trait CreateRequestHelper extends ServerProvider {

  val defaultSeconds = 5
  implicit val defaultDuration: FiniteDuration = Duration.apply(defaultSeconds, SECONDS)

  val app: Application

  lazy val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

  val defaultCookie = DefaultWSCookie("CSRF-Token","nocheck")

  def getRequest(path: String, follow: Boolean = false)(sessionKvs: (String, String)*): Future[WSResponse] = {
    ws.url(s"http://localhost:$port/interest-restriction-return$path")
      .withHttpHeaders(HeaderNames.COOKIE -> SessionCookieBaker.bakeSessionCookie(sessionKvs.toMap))
      .withFollowRedirects(follow)
      .get()
  }

  def postRequest(path: String, formJson: JsValue, follow: Boolean = false)(sessionKvs: (String, String)*)(): Future[WSResponse] = {
    ws.url(s"http://localhost:$port/interest-restriction-return$path")
      .withHttpHeaders("Csrf-Token" -> "nocheck", HeaderNames.COOKIE -> SessionCookieBaker.bakeSessionCookie(sessionKvs.toMap))
      .withFollowRedirects(follow)
      .post(formJson)
  }

}
