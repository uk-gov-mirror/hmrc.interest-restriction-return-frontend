package views

import forms.$className$FormProvider
import models.{$className$, NormalMode}
import nunjucks.$className$Template
import nunjucks.viewmodels.CheckboxViewModel
import play.api.Application
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.behaviours.CheckboxViewBehaviours
import views.html.$className;format="decap"$View

class $className;format="decap"$ViewSpec extends CheckboxViewBehaviours[$className$] with NunjucksSupport {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"$className$ (\$templatingSystem) view" must {

      val view = viewFor[$className;format="decap"$View](Some(emptyUserAnswers))

      def applyView(form: Form[Set[$className$]]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render($className$Template, Json.toJsObject(CheckboxViewModel($className$.options(form), form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[$className;format="decap"$View](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like checkboxPage(form, applyView, messageKeyPrefix, $className$.options(form))
    }
  }
}

