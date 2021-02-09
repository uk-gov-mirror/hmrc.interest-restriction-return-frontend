package pages.$section;format="decap"$
import pages.QuestionPage
import java.time.LocalDate

import play.api.libs.json.JsPath

case object $className;format="cap"$Page extends QuestionPage[LocalDate] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "$className;format="decap"$"

}