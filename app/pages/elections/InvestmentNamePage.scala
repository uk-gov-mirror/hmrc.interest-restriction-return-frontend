package pages.elections
import pages.QuestionPage
import play.api.libs.json.JsPath

case object InvestmentNamePage extends QuestionPage[String] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "investmentName"
}
