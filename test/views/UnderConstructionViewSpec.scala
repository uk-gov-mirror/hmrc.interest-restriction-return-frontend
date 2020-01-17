package views

import views.behaviours.ViewBehaviours
import views.html.UnderConstructionView
import nunjucks.UnderConstructionTemplate
import views.{Twirl, Nunjucks}

class UnderConstructionViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[UnderConstructionView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)
  lazy val nunjucksView = await(nunjucksRenderer.render(UnderConstructionTemplate)(fakeRequest))

  Seq(twirlView -> Twirl, nunjucksView -> Nunjucks).foreach {
    case (html, templatingSystem) =>
      s"UnderConstructionView ($templatingSystem)" must {

        behave like normalPage(html, "underConstruction")

        behave like pageWithBackLink(html)
      }
  }
}
