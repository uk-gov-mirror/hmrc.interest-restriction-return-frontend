package pages.$section;format="decap"$
import pages.QuestionPage
import play.api.libs.json.JsPath

case object $className;format="cap"$Page {
  
  override def path: JsPath = JsPath \ toString
  
  override def toString: String = "$className;format="decap"$"
}
