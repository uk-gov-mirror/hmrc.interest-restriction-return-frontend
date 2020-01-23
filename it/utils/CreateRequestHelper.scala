package utils

import org.scalatestplus.play.ServerProvider
import play.api.Application
import play.api.libs.json.JsValue
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSCookie, WSResponse}

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}


trait CreateRequestHelper extends ServerProvider {

  val defaultSeconds = 5
  val defaultDuration: FiniteDuration = Duration.apply(defaultSeconds, SECONDS)

  val app: Application

  lazy val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

  val defaultCookie = DefaultWSCookie("CSRF-Token","nocheck")

  def getRequest(path: String, follow: Boolean = false): Future[WSResponse] = {
    ws.url(s"http://localhost:$port/interest-restriction-return$path")
      .withFollowRedirects(follow)
      .get()
  }

  def postRequest(path: String, formJson: JsValue, follow: Boolean = false): Future[WSResponse] = {
    ws.url(s"http://localhost:$port/interest-restriction-return$path")
      .withHttpHeaders("Csrf-Token" -> "nocheck")
      .withCookies(defaultCookie)
      .withFollowRedirects(follow)
      .post(formJson)
  }

}
