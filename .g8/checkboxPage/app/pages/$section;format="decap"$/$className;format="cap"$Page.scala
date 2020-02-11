package pages.$section;format="decap"$
import pages.QuestionPage
import models.$className;format="cap"$
import play.api.libs.json.JsPath

case object $className;format="cap"$Page extends QuestionPage[Set[$className;format="cap"$]] {
  
  override def path: JsPath = JsPath \ toString
  
  override def toString: String = "$className;format="decap"$"
}
