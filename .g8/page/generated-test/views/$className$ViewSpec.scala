package views

import views.behaviours.ViewBehaviours
import views.html.$className$View
import nunjucks.$className$Template
import views.{Twirl, Nunjucks}

class $className$ViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[$className$View](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)
  lazy val nunjucksView = await(nunjucksRenderer.render($className$Template)(fakeRequest))

  Seq(twirlView -> Twirl, nunjucksView -> Nunjucks).foreach {
    case (html, templatingSystem) =>
      s"$className$View (\$templatingSystem)" must {

        behave like normalPage(html, "$className;format="decap"$")

        behave like pageWithBackLink(html)
      }
  }
}
