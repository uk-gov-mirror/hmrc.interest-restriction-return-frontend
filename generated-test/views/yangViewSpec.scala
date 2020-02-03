package views

import controllers.routes
import forms.yangFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.YesNoViewBehaviours
import views.html.yangView

class yangViewSpec extends YesNoViewBehaviours  {

  val messageKeyPrefix = "yang"

  val form = new yangFormProvider()()

    s"yangView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[yangView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.yangController.onSubmit(NormalMode).url)
    }
  }
}
