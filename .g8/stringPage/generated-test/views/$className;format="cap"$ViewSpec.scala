package views

import controllers.routes
import forms.$className;format="cap"$FormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.StringViewBehaviours
import views.html.$className;format="cap"$View

class $className;format="cap"$ViewSpec extends StringViewBehaviours  {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className;format="cap"$FormProvider()()

    s"$className;format="cap"$View" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
          val view = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like stringPage(form, applyView, messageKeyPrefix, routes.$className;format="cap"$Controller.onSubmit(NormalMode).url)
    }
}
