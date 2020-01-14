package views

import views.behaviours.ViewBehaviours
import views.html.SavedReturnView
import nunjucks.SavedReturnTemplate
import views.{Twirl, Nunjucks}

class SavedReturnViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[SavedReturnView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)
  lazy val nunjucksView = await(nunjucksRenderer.render(SavedReturnTemplate)(fakeRequest))

  Seq(twirlView -> Twirl, nunjucksView -> Nunjucks).foreach {
    case (html, templatingSystem) =>
      s"SavedReturnView ($templatingSystem)" must {

        behave like normalPage(html, "savedReturn")

        behave like pageWithBackLink(html)
      }
  }
}
