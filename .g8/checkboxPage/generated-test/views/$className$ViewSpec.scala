package views

import forms.$className$FormProvider
import models.{$className$, NormalMode}
import .$className$Template
import .viewmodels.CheckboxViewModel
import play.api.Application
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat

import views.behaviours.CheckboxViewBehaviours
import views.html.$className$View

class $className$ViewSpec extends CheckboxViewBehaviours[$className$]  {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  Seq(, Twirl).foreach { templatingSystem =>

    s"$className$ (\$templatingSystem) view" must {

      val view = viewFor[$className$View](Some(emptyUserAnswers))

      def applyView(form: Form[Set[$className$]]): HtmlFormat.Appendable =
        if (templatingSystem == ) {
          await(Renderer.render($className$Template, Json.toJsObject(CheckboxViewModel($className$.options(form), form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[$className$View](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like checkboxPage(form, applyView, messageKeyPrefix, $className$.options(form))
    }
  }
}

