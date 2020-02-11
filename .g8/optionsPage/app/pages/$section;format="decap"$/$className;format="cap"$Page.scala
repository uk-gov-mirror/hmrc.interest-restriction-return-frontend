package pages

import models.$className;format="cap"$
import play.api.libs.json.JsPath

case object $className;format="cap"$Page extends QuestionPage[$className;format="cap"$] {
  
  override def path: JsPath = JsPath \ toString
  
  override def toString: String = "$className;format="decap"$"
}
