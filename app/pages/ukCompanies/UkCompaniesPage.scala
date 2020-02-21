package pages.ukCompanies

import models.returnModels.fullReturn.UkCompanyModel
import pages.QuestionPage
import play.api.libs.json.JsPath

case object UkCompaniesPage extends QuestionPage[UkCompanyModel] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "ukCompanies"
}