package views.ukCompanies

import assets.messages.{BaseMessages, SectionHeaderMessages}
import views.behaviours.ViewBehaviours
import views.html.ukCompanies.CheckAnswersUkCompanyView
import views.{Twirl}

class CheckAnswersUkCompanyViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[CheckAnswersUkCompanyView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)

  val messageKeyPrefix = "checkAnswersUkCompany"
  val section = Some(messages("section.ukCompanies"))

      "CheckAnswersUkCompanyView" must {

        behave like normalPage(twirlView, messageKeyPrefix, section = section)
        behave like pageWithBackLink(twirlView)
      }
  }
